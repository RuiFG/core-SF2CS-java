package in.bugr.authorization.service.impl;

import com.google.gson.Gson;
import in.bugr.AuthHelper;
import in.bugr.CacheHelper;
import in.bugr.authorization.config.OauthProperty;
import in.bugr.authorization.repository.OauthRepository;
import in.bugr.authorization.service.AuthService;
import in.bugr.entity.Person;
import in.bugr.entity.dto.User;
import in.bugr.exception.CommonException;
import in.bugr.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

/**
 * @author BugRui
 * @date 2020/4/12 下午7:04
 **/
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService, CacheHelper<String, User> {
    private final OauthRepository oauthRepository;
    private final OauthProperty oauthProperty;
    private final PersonRepository personRepository;
    private final Gson GSON = new Gson();
    private final RedisTemplate<String, User> redisTemplate;

    private User toUser(HashMap<String, ?> oauthMap) {
        Object active = oauthMap.get("active");
        if (!(Boolean) ObjectUtils.defaultIfNull(active, true)) {
            throw new CommonException(401, "帐号已被禁用");
        }
        Long oauthId = Long.parseLong(String.valueOf(oauthMap.get("user_id")));
        Person person = personRepository.findByOauthId(oauthId).or(
                //register
                () -> {
                    String userName = (String) oauthMap.get("user_name");
                    Person newPerson = new Person()
                            .setAlias(userName)
                            .setOauthId(oauthId);
                    return Optional.of(personRepository.save(newPerson));
                }).orElseThrow(() -> new CommonException(401, "无法找到用户"));

        User user = User.build(oauthMap, person);
        return set(user.getAccessToken(), user)
                .setRefreshToken(null);
    }

    @Override
    public User code(String code) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("code", code);
        hashMap.put("grant_type", "authorization_code");
        hashMap.put("redirect_uri", oauthProperty.getRedirectUri());
        HashMap<String, ?> oauthMap = oauthRepository.postAccessToken(hashMap);
        return toUser(oauthMap);
    }

    @Override
    public User checkToken() {
        String accessToken = AuthHelper.getAccessToken();
        String[] splitBearer = accessToken.split(",");
        User user = get(accessToken);
        if (ObjectUtils.isEmpty(user)) {
            HashMap<String, ?> oauthMap = oauthRepository.checkToken(splitBearer[1]);
            return toUser(oauthMap);
        }
        return user;
    }


    @Override
    public User getMe() {
        return get(AuthHelper.getAccessToken());
    }

    @Override
    public Person updatePerson(Person person) {
        Person oldPerson = personRepository.findById(person.getId())
                .orElseThrow(() -> new CommonException("未找到用户"));
        oldPerson.setAlias(person.getAlias());
        oldPerson.setFaceData(person.getFaceData());
        return personRepository.save(oldPerson);
    }

    @Override
    public RedisTemplate<String, User> redisTemplate() {
        return redisTemplate;
    }
}
