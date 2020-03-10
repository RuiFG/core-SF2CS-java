package in.bugr.jni.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author BugRui
 * @date 2020/3/9 下午3:40
 **/
@NoArgsConstructor
@Getter
public class FaceInfo {
    public Rect rect;
    float score;
}
