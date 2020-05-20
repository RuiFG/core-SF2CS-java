package in.bugr;

import in.bugr.common.entity.Person;
import in.bugr.common.entity.User;
import in.bugr.common.exception.CommonException;
import in.bugr.common.repository.PersonRepository;
import in.bugr.common.repository.UserRepository;
import in.bugr.config.OauthProperty;
import in.bugr.repository.OauthRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

/**
 * @author BugRui
 * @date 2020/4/20 下午12:41
 **/

public abstract class OauthProcessor {

    private static final String CODE_METHOD = "code";
    private static final String PASSWORD_METHOD = "password";
    //other method

    private static Holder Holder;
    private static final String[] METHODS = new String[]{CODE_METHOD, PASSWORD_METHOD};

    @Component
    @RequiredArgsConstructor
    static class Holder {
        private final Map<String, OauthProcessor> oauthProcessorHashMap;

        @PostConstruct
        public void init() {
            OauthProcessor.Holder = this;
        }

        OauthProcessor get(String name) {
            return oauthProcessorHashMap.get(name);
        }
    }
    static public HashMap<String, ?> login(HashMap<String, String> requestMap) {
        String type = requestMap.getOrDefault("type", "");
        for (String method : METHODS) {
            if (method.equals(type)) {
                return Holder.get(method).process(requestMap);
            }
        }
        throw new CommonException(400, "请求方式错误");
    }

    /**
     * login abstract method
     *
     * @param requestMap request's map
     * @return {@link User} and {@link Person}
     */
    abstract HashMap<String, ?> process(HashMap<String, String> requestMap);


    @Component(CODE_METHOD)
    @RequiredArgsConstructor
    static class Code extends OauthProcessor {
        private final OauthRepository oauthRepository;
        private final PersonRepository personRepository;
        private final OauthProperty oauthProperty;
        private final UserRepository userRepository;

        @Override
        public HashMap<String, ?> process(HashMap<String, String> requestMap) {
            String code = requestMap.get("code");
            if (StringUtils.isBlank(code)) {
                throw new CommonException(400, "请求码为空");
            }
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("code", code);
            hashMap.put("grant_type", "authorization_code");
            hashMap.put("redirect_uri", oauthProperty.getRedirectUri());
            HashMap<String, ?> oauthMap = oauthRepository.postAccessToken(hashMap);
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
            //generate user
            User user = new User();
            user.setTokenType((String) oauthMap.get("token_type"))
                    .setAccessToken((String) oauthMap.get("access_token"))
                    .setRefreshToken((String) oauthMap.get("refresh_token"))
                    .setExpiresIn(Long.parseLong(String.valueOf(oauthMap.get("expires_in"))))
                    .setScope((String) oauthMap.get("scope"))
                    .setOauthId(oauthId);
            ArrayList<String> authorities = (ArrayList<String>) oauthMap.get("authorities");
            HashSet<User.Role> roles = new HashSet<>();
            authorities.forEach(role -> roles.add(User.Role.valueOf(role)));
            user.setAuthorities(roles);
            //clear refresh Token
            user = userRepository.save(user).setRefreshToken(null);
            // return map that user and person
            HashMap<String, Object> map = new HashMap<>();
            map.put("user", user);
            map.put("person", person);
            return map;
        }

    }
}
