package in.bugr.common.repository;

import in.bugr.common.entity.Device;
import org.springframework.stereotype.Repository;

/**
 * @author BugRui
 * @date 2020/5/19 下午11:07
 **/
@Repository
public interface DeviceRepository extends BaseRepository<Device, Long> {
}
