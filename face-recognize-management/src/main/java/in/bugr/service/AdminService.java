package in.bugr.service;

import in.bugr.common.entity.Device;
import in.bugr.common.entity.Gather;
import in.bugr.common.entity.GatherPerson;
import in.bugr.common.entity.Person;

import java.util.List;

/**
 * @author BugRui
 * @date 2020/4/20 下午9:42
 **/
public interface AdminService {
    List<Gather> getGathers();

    Gather createGather(Gather gather);

    /**
     * update Gather By Id
     *
     * @param id     gather Id
     * @param gather changed {@link Gather}
     * @return {@link Gather}
     */
    Gather updateGather(Long id, Gather gather);

    /**
     * update person By id
     *
     * @param id     person Id
     * @param person changed {@link Person}
     * @return {@link Person}
     */
    Person updatePerson(Long id, Person person);


    GatherPerson addGatherManager(Long gatherId, Long personId);

    void delGatherManager(Long gatherId, Long personId);

    List<Device> getDevices();

    void delDevice(Long id);

    Device createDevice(Device device);

    Device disableDevice(Long id);
}
