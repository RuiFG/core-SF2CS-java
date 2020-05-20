package in.bugr.common.exception;

import lombok.Getter;

/**
 * @author BugRui
 * @date 2020/3/24 下午1:04
 **/
@Getter
public class CommonException extends RuntimeException {
    Integer code;
    String describe;


    public CommonException() {
        this.describe = "未知错误";
        this.code = 500;
    }

    public CommonException(String describe) {
        super(describe);
        this.describe = describe;
        this.code = 500;
    }

    public CommonException(Integer code, String describe) {
        super(describe);
        this.describe = describe;
        this.code = code;
    }
}
