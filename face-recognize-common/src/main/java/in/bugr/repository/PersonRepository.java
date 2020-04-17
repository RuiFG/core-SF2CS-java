package in.bugr.repository;

import in.bugr.entity.Person;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author BugRui
 * @date 2020/3/23 下午7:31
 **/
@Repository
public interface PersonRepository extends BaseRepository<Person, Long> {
    /**
     * 根据oauthId查找用户
     *
     * @param oauthId oauthId
     * @return optional
     */
    Optional<Person> findByOauthId(Long oauthId);
}
