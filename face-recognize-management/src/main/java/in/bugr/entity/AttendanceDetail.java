package in.bugr.entity;

import in.bugr.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import java.time.LocalDateTime;

/**
 * @author BugRui
 * @date 2020/3/23 下午8:04
 **/
@Entity(name = "attendance_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
@Data
public class AttendanceDetail extends BaseEntity {

    private Long attendanceId;

    private Long personId;

    private String face = "default";

    private float score;

    private LocalDateTime time;

}
