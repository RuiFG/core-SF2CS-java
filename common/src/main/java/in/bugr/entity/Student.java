package in.bugr.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author BugRui
 * @date 2020/3/12 下午3:57
 **/
@Entity(name = "student")
@Table(name = "student")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
@Data
public class Student extends BaseEntity {
    /**
     * 裁剪后的人脸数据bgr
     */
    private byte[] faceData;
    /**
     * 班级id
     */
    private Long classesId;
    /**
     * 人脸数据在班级中的下标
     */
    private Long classesFaceIndex;

}
