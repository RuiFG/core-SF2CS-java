package in.bugr.service.impl;

import in.bugr.common.entity.Device;
import in.bugr.common.entity.Gather;
import in.bugr.common.entity.GatherPerson;
import in.bugr.common.entity.Person;
import in.bugr.common.exception.CommonException;
import in.bugr.common.repository.DeviceRepository;
import in.bugr.common.repository.GatherPersonRepository;
import in.bugr.common.repository.GatherRepository;
import in.bugr.common.repository.PersonRepository;
import in.bugr.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author BugRui
 * @date 2020/4/20 下午9:42
 **/
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    @NotNull
    private final GatherRepository gatherRepository;
    @NotNull
    private final PersonRepository personRepository;

    @NotNull
    private final GatherPersonRepository gatherPersonRepository;

    @NotNull
    private final DeviceRepository deviceRepository;

    @Override
    public List<Gather> getGathers() {
        return gatherRepository.findAll();
    }

    @Override
    public Gather createGather(Gather gather) {
        gather.setData(null);
        gather.setSize(0);
        gather.setId(null);
        return gatherRepository.save(gather);
    }

    @Override
    public Gather updateGather(Long id, Gather gather) {
        Gather oldGather = gatherRepository.findById(id)
                .orElseThrow(() -> new CommonException("未找到gather"));
        oldGather.setName(gather.getName());
        return gatherRepository.save(oldGather);
    }


    @Override
    public Person updatePerson(Long id, Person person) {
        Person oldPerson = personRepository.findById(id)
                .orElseThrow(() -> new CommonException("未找到用户"));
        if (ObjectUtils.isNotEmpty(person.getAlias())) {
            oldPerson.setAlias(person.getAlias());
        }
        if (ObjectUtils.isNotEmpty(person.getAvatar())) {
            oldPerson.setAvatar(person.getAvatar());
        }
        return personRepository.save(oldPerson);
    }

    @Override
    public GatherPerson addGatherManager(Long gatherId, Long personId) {
        GatherPerson gatherPerson = gatherPersonRepository
                .findByGatherIdAndPersonId(gatherId, personId)
                .orElse(new GatherPerson().setGatherFaceIndex(-1)
                        .setPersonId(personId)
                        .setGatherId(gatherId)
                        .setControl(true));
        return gatherPersonRepository.save(gatherPerson);
    }

    @Override
    public void delGatherManager(Long gatherId, Long personId) {
        GatherPerson gatherPerson = gatherPersonRepository
                .findByGatherIdAndPersonId(gatherId, personId)
                .orElseThrow(() -> new CommonException(400, "该person没有管理班级"));
        gatherPersonRepository.delete(gatherPerson);

    }

    @Override
    public List<Device> getDevices() {
        return deviceRepository.findAll();
    }

    @Override
    public void delDevice(Long id) {
        deviceRepository.deleteById(id);
    }

    @Override
    public Device createDevice(Device device) {
        device.setToken(UUID.randomUUID().toString())
                .setEnable(true);
        return deviceRepository.save(device);
    }

    @Override
    public Device disableDevice(Long id) {
        Device device = deviceRepository.findById(id).orElseThrow(() -> new CommonException("资源未找到"));
        device.setEnable(false);
        return deviceRepository.save(device);
    }

}
