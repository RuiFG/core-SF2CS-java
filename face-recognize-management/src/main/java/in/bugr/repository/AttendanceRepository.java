package in.bugr.repository;

import in.bugr.common.repository.BaseRepository;
import in.bugr.entity.Attendance;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author BugRui
 * @date 2020/4/4 下午4:06
 **/
@Repository
public interface AttendanceRepository extends BaseRepository<Attendance, Long> {
    /**
     * 根据gatherId查找
     *
     * @param gatherId
     * @return
     */
    List<Attendance> findAllByGatherId(Long gatherId);

    /**
     * find Attendance by finish
     *
     * @param finish
     * @return
     */
    List<Attendance> findByFinish(Boolean finish);

    List<Attendance> findAllByFinishAndGatherIdIn(Boolean finish, List<Long> gatherIds);
}
