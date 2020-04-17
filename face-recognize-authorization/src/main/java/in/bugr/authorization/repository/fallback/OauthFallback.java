package in.bugr.authorization.repository.fallback;

import in.bugr.authorization.repository.OauthRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author BugRui
 * @date 2020/4/14 下午10:28
 **/
@Component
public class OauthFallback implements OauthRepository {
    @Override
    public HashMap<String, ?> postAccessToken(Map<String, String> parameters) {
        System.out.println("降级");
        return null;
    }

    @Override
    public HashMap<String, ?> checkToken(String value) {
        return null;
    }
}
