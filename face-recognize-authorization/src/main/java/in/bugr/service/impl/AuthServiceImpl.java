package in.bugr.service.impl;

import in.bugr.common.repository.UserRepository;
import in.bugr.entity.Permission;
import in.bugr.repository.PermissionRepository;
import in.bugr.service.AuthService;
import in.bugr.common.entity.User;
import in.bugr.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.List;

/**
 * @author BugRui
 * @date 2020/4/12 下午7:04
 **/
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();


    /**
     * take me and refresh cache
     *
     * @return user
     */
    @Override
    public User getMe() {
        return userRepository.findBySessionKey()
                .orElseThrow(() -> new CommonException(401, "用户已过期"));

    }


    @Override
    public Boolean hasPermission(String method, String url) {
        User user = userRepository.findBySessionKey().orElseThrow(() ->
                new CommonException(401, "未找到用户"));
        List<Permission> permissionList =
                permissionRepository.findByMethodAndRoleIn(method, user.getAuthorities());
        for (Permission permission :
                permissionList) {
            if (pathMatcher.match(permission.getUrl(), url) &&
                    permission.getMethod().equals(method)) {
                return true;
            }
        }
        return true;
    }

    @Override
    public String refreshToken() {
        return "token";
    }

    @Override
    public void logout() {
        User user = userRepository.findBySessionKey()
                .orElseThrow(() -> new CommonException(401, "用户已过期"));
        userRepository.delete(user);
    }


}
