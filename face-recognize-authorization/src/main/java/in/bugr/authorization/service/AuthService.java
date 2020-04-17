package in.bugr.authorization.service;

import com.google.gson.JsonObject;
import in.bugr.entity.Person;
import in.bugr.entity.dto.User;

import java.util.HashMap;

/**
 * @author BugRui
 * @date 2020/4/12 下午7:04
 **/
public interface AuthService {
    User code(String code);

    User checkToken();

    User getMe();

    Person updatePerson(Person person);
}
