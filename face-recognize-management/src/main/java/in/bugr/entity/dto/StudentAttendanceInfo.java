package in.bugr.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Access;
import java.time.LocalDateTime;

/**
 * @author BugRui
 * @date 2020/5/20 下午9:04
 **/
@Data
@Accessors(chain = true)
public class StudentAttendanceInfo {
    private String attendanceName;
    private String gatherName;
    private String face;
    private float score;
}
