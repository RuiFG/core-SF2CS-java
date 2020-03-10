package in.bugr.web.entity;

import com.google.gson.JsonObject;
import in.bugr.web.commponent.JsonObjectConverter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author BugRui
 * @date 2020/2/4 下午1:53
 **/
@Entity(name = "test")
@Table(name = "test")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class Test extends BaseEntity {
    private String chars;
    @Convert(converter = JsonObjectConverter.class)
    private JsonObject json;
}
