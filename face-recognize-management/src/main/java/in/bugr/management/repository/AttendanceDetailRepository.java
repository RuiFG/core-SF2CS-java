package in.bugr.management.repository;

import in.bugr.repository.BaseRepository;
import in.bugr.management.entity.AttendanceDetail;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author BugRui
 * @date 2020/3/23 下午8:21
 **/
@Repository
public interface AttendanceDetailRepository extends BaseRepository<AttendanceDetail, Long> {
    /**
     * 根据personId和 attendanceId查找
     *
     * @param personId
     * @param attendanceId
     * @return
     */
    Optional<AttendanceDetail> findAllByPersonIdAndAttendanceId(Long personId, Long attendanceId);

    /**
     * 根据attendanceId查找
     *
     * @param attendanceId
     * @return
     */
    List<AttendanceDetail> findAllByAttendanceId(Long attendanceId);

    /**
     * 根据attendanceId删除全部
     *
     * @param attendanceId
     */
    void deleteAllByAttendanceId(Long attendanceId);
}
