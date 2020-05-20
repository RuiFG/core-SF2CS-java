package in.bugr.service;

import in.bugr.common.entity.User;

import java.util.HashMap;

/**
 * @author BugRui
 * @date 2020/4/12 下午7:04
 **/
public interface AuthService {


    User getMe();

    Boolean hasPermission(String method, String url);

    String refreshToken();

    void logout();
}
