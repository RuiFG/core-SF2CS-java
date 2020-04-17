package in.bugr;

import in.bugr.entity.Person;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author BugRui
 * @date 2020/4/15 下午1:56
 **/
public class AuthHelper {
    public enum Role {


        /**
         * role table
         */
        TEACHER("teacher"),

        STUDENT("student"),

        ADMIN("admin"),
        NULL("");

        private String value;

        Role(String value) {
            this.value = value;
        }
    }

    private static final String CUSTOM_AUTHORIZATION = "SF2CS-Authorization";

    private static HttpServletRequest request() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtils.isNotEmpty(requestAttributes)) {
            return requestAttributes.getRequest();
        }
        return null;
    }

    public static boolean isAuth() {
        HttpServletRequest request = request();
        if (ObjectUtils.isNotEmpty(request)) {
            if (isOauth()) {
                String custom = request.getHeader(CUSTOM_AUTHORIZATION);
                return !StringUtils.isBlank(custom);
            }
        }
        return false;
    }

    public static boolean isOauth() {
        HttpServletRequest request = request();
        if (ObjectUtils.isNotEmpty(request)) {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            return !StringUtils.isBlank(header);
        }
        return false;

    }


    public static Person getMe() {
        Person person = new Person();
        HttpServletRequest request = request();
        if (ObjectUtils.isNotEmpty(request)) {
            if (isAuth()) {
                String header = request.getHeader(CUSTOM_AUTHORIZATION);
                String[] split = StringUtils.split(header, ",");
                if (split.length == 3) {
                    person.setId(Long.parseLong(split[0]));
                    person.setAlias(split[1]);
                }
            }
        }
        //missing face data
        return person;
    }

    public static String getAccessToken() {
        HttpServletRequest request = request();
        if (ObjectUtils.isNotEmpty(request)) {
            return request.getHeader(HttpHeaders.AUTHORIZATION);
        }
        return "";
    }


    public static Role getRole() {
        HttpServletRequest request = request();
        if (ObjectUtils.isNotEmpty(request)) {
            if (isAuth()) {
                String header = request.getHeader(CUSTOM_AUTHORIZATION);
                String[] split = StringUtils.split(header, ",");
                if (split.length == 3) {
                    String roleName = split[2].toUpperCase();
                    return Role.valueOf(roleName);
                }
            }
        }
        return Role.NULL;
    }
}
