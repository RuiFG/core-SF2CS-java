package in.bugr.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

/**
 * @author BugRui
 * @date 2020/3/12 下午4:04
 **/
@Entity(name = "gather")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
@Data
public class Gather extends BaseEntity {
    /**
     * fdb 数据
     */
    @Lob
    @JsonIgnore
    @Column(columnDefinition = "longblob")
    private byte[] data;
    /**
     * 数量
     */
    private int size;

    @NotNull(message = "组织名不能为空")
    private String name;
}
