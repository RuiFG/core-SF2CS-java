package in.bugr.common.repository;

import in.bugr.common.entity.User;
import in.bugr.common.exception.CommonException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author BugRui
 * @date 2020/4/19 下午5:04
 **/
@Repository
public interface UserRepository extends CrudRepository<User, String> {

    /**
     * take {@link HttpServletRequest} from request's Context
     *
     * @return {@link HttpServletRequest}
     */
    private HttpServletRequest request() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtils.isNotEmpty(requestAttributes)) {
            return requestAttributes.getRequest();
        }
        return null;
    }

    /**
     * judge auth from request' header and redis
     *
     * @return flag
     */
    default boolean isAuth() {
        String accessToken = getSessionKey();
        findById(accessToken).orElseThrow(() -> new CommonException(401, "用户已过期"));
        return true;
    }

    /**
     * take {@link User}' access token in request' header
     * private
     *
     * @return access token
     */
    private String getSessionKey() {
        HttpServletRequest request = request();
        if (ObjectUtils.isNotEmpty(request)) {
            return request.getHeader(HttpHeaders.AUTHORIZATION);
        }
        throw new CommonException(401, "从header获取token失败");
    }

    /**
     * find User from Session
     *
     * @return {@link Optional<User>}
     */
    default Optional<User> findBySessionKey() {
        String sessionKey = getSessionKey();
        if (StringUtils.isEmpty(sessionKey)) {
            return Optional.empty();
        }
        String[] split = sessionKey.split(" ");
        if (split.length != 2) {
            return Optional.empty();
        }
        return findById(split[1]);
    }
}
