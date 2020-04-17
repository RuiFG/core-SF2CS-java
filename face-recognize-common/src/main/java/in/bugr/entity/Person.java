package in.bugr.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Column;
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
@ToString(callSuper = true)
@Data
public class Person extends BaseEntity {
    /**
     * 裁剪后的人脸数据bgr
     */
    @Column(columnDefinition = "longblob")
    private byte[] faceData;

    private Integer channels;

    private Integer width;

    private Integer height;

    @NotNull(message = "oauthId不能为空")
    private Long oauthId;
    @NotNull(message = "用户名不能为空")
    private String alias;
}
