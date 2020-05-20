package in.bugr.common.repository;

import in.bugr.common.entity.GatherPerson;
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
     * @param gatherId
     * @param personId
     * @return
     */
    Optional<GatherPerson> findByGatherIdAndPersonId(Long gatherId, Long personId);


    List<GatherPerson> findAllByGatherIdAndControl(Long gatherId, Boolean control);

    List<GatherPerson> findAllByGatherId(Long gatherId);

    /**
     * find gather person by controller
     * if control = true @return All the controllers of the gather
     * if control = false not support option
     *
     * @param personId
     * @param control
     * @return
     */
    List<GatherPerson> findAllByPersonIdAndControl(Long personId, Boolean control);
}
