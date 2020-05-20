package in.bugr.common.repository;

import in.bugr.common.entity.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    List<Person> findAllByIdIn(List<Long> personIdList);

    List<Person> findAllByIdNotIn(List<Long> personIdList);
}
