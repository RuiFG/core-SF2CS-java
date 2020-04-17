package in.bugr.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Entity;

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
    private Long gatherId = -1L;
    /**
     * 人脸Id
     */
    private Long personId = -1L;
    /**
     * 人脸数据在班级中的下标
     */
    private Integer gatherFaceIndex = -1;
}
