package in.bugr.entity;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author BugRui
 * @date 2020/3/12 下午4:04
 **/
@Entity(name = "classes")
@Table(name = "classes")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class Classes extends BaseEntity {
    /**
     * fdb 数据
     */
    private byte[] data;
}
