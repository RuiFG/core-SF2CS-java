package in.bugr.management.entity;

import in.bugr.entity.BaseEntity;
import in.bugr.management.component.ListLongConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author BugRui
 * @date 2020/4/4 下午3:53
 **/
@Entity(name = "attendance")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
@Data
public class Attendance extends BaseEntity {

    @NotNull(message = "考勤名不能为空")
    private String name;
    @NotNull(message = "gatherId不能为空")

    private Long gatherId;

    private String gatherName;

    @Convert(converter = ListLongConverter.class)
    private List<Long> notDetectPersonIds = new CopyOnWriteArrayList<>();

    @Convert(converter = ListLongConverter.class)
    private List<Long> detectPersonIds = new CopyOnWriteArrayList<>();

    private Boolean finish = false;
}
