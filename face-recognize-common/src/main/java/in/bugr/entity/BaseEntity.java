package in.bugr.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * @author BugRui
 * @date 2020/2/4 下午1:48
 **/
@MappedSuperclass
@Data
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 修改时间
     */
    @LastModifiedDate
    private LocalDateTime modifyTime;

    /**
     * 创建时间
     */
    @CreatedDate
    private LocalDateTime createTime;
}
