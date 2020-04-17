package in.bugr.component;

import in.bugr.exception.CommonException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author BugRui
 * @date 2020/4/4 下午5:49
 **/
@RestControllerAdvice("in.bugr")
public class ExceptionAdvice {

    @ExceptionHandler(value = CommonException.class)
    public ResponseEntity<String> common(CommonException e) {
        return ResponseEntity.status(e.getCode())
                .body(e.getDescribe());
    }
}
