package in.bugr.repository;

import in.bugr.entity.GatherPerson;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author BugRui
 * @date 2020/3/23 下午7:32
 **/
@Repository
public interface GatherPersonRepository extends BaseRepository<GatherPerson, Long> {
    /**
     * 根据组ID查找
     *
     * @param gatherId 组id
     * @return result
     */
    List<GatherPerson> findAllByGatherId(Long gatherId);

    /**
     * @param gatherId
     * @param personId
     * @return
     */
    Optional<GatherPerson> findByGatherIdAndPersonId(Long gatherId, Long personId);
}
