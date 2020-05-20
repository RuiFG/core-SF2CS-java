package in.bugr.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * @author BugRui
 * @date 2020/5/19 下午11:06
 **/
@Entity(name = "device")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Data
public class Device extends BaseEntity {
    @NotNull(message = "名字不能为空")
    String name;
    String token;
    Boolean enable = true;
}
