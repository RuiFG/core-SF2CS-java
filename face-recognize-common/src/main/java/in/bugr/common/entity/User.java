package in.bugr.common.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author BugRui
 * @date 2020/4/17 下午2:03
 **/
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
@RedisHash("user")
public class User  {
    public enum Role implements Serializable {
        // role status
        ROLE_STUDENT("ROLE_STUDENT"),
        ROLE_TEACHER("ROLE_TEACHER"),
        ROLE_ADMIN("ROLE_ADMIN");
        String value;

        Role(String value) {
            this.value = value;
        }
    }

    /**
     * token type
     */
    String tokenType;
    /**
     * access Token
     */
    @Id
    String accessToken;
    /**
     * refresh Token
     */
    String refreshToken;
    /**
     * scope
     */
    String scope;
    /**
     * expires time
     */
    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    Long expiresIn;

    Set<Role> authorities;

    Long oauthId;

}
