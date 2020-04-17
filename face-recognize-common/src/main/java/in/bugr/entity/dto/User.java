package in.bugr.entity.dto;

import in.bugr.entity.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Access;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author BugRui
 * @date 2020/4/17 下午2:03
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class User extends Person {
    public enum Role {
        // role status
        ROLE_STUDENT,
        ROLE_TEACHER,
        ROLE_ADMIN;
    }

    String tokenType;
    String accessToken;
    String refreshToken;
    String scope;
    Long expiresIn;
    List<Role> authorities;

    public static User build(HashMap<String, ?> oauthMap, Person person) {
        User user = new User();
        user.setTokenType((String) oauthMap.get("token_type"));
        user.setAccessToken((String) oauthMap.get("access_token"));
        user.setRefreshToken((String) oauthMap.get("refresh_token"));
        user.setExpiresIn(Long.parseLong(String.valueOf(oauthMap.get("expires_in"))));
        user.setScope((String) oauthMap.get("scope"));
        List<Role> authorities = (ArrayList<Role>) oauthMap.get("authorities");
        user.setAuthorities(authorities);
        user.setAlias(person.getAlias());
        user.setId(person.getId());
        user.setFaceData(person.getFaceData());vim
        return user;
    }

}
