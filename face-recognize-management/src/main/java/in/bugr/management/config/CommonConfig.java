package in.bugr.management.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author BugRui
 * @date 2020/4/8 下午4:08
 **/
@Configuration
@EntityScan({"in.bugr.entity", "in.bugr.management.entity"})
@EnableJpaRepositories({"in.bugr.repository", "in.bugr.management.repository"})
@ComponentScan({"in.bugr.component"})
@EnableJpaAuditing
public class CommonConfig {
}
