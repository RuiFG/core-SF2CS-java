package in.bugr.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * @author BugRui
 * @date 2020/3/23 下午7:26
 **/
@Entity(name = "gather_person")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
@Data
public class GatherPerson extends BaseEntity {

    /**
     * 组id
     */
    @NotNull(message = "组织ID不能为空")
    private Long gatherId = -1L;
    /**
     * 人脸Id
     */
    @NotNull(message = "人类ID不能为空")
    private Long personId = -1L;
    /**
     * 人脸数据在班级中的下标
     */
    private Integer gatherFaceIndex = -1;

    private Boolean control = false;
}
