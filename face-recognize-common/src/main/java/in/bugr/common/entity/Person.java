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
 * @date 2020/3/12 下午3:57
 **/
@Entity(name = "person")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Data
public class Person extends BaseEntity {
    private String avatar = "default";

    @NotNull(message = "oauthId不能为空")
    private Long oauthId;
    @NotNull(message = "用户名不能为空")
    private String alias;

}
