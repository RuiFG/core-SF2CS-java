package in.bugr.common.message;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author BugRui
 * @date 2020/3/25 下午12:36
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DetectTask implements Serializable {
    private static final Gson GSON = new Gson();

    byte[] imgData;


    public Object toJson() {
        return GSON.toJson(this);
    }

    public static DetectTask fromJson(String json) {
        return GSON.fromJson(json, DetectTask.class);
    }
}
