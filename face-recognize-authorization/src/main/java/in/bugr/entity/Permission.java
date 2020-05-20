package in.bugr.entity;

import in.bugr.common.entity.BaseEntity;
import in.bugr.common.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Entity;

/**
 * @author BugRui
 * @date 2020/4/19 下午8:03
 **/
@Entity(name = "permission")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
@Data
public class Permission extends BaseEntity {

    private String url;

    private String method;

    private User.Role role;
}
