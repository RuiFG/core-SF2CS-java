package in.bugr.service.impl;

import in.bugr.common.entity.GatherPerson;
import in.bugr.common.entity.Person;
import in.bugr.common.entity.User;
import in.bugr.common.exception.CommonException;
import in.bugr.common.repository.GatherPersonRepository;
import in.bugr.common.repository.GatherRepository;
import in.bugr.common.repository.PersonRepository;
import in.bugr.common.repository.UserRepository;
import in.bugr.service.SharedService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author BugRui
 * @date 2020/4/20 下午9:38
 **/
@Component
@RequiredArgsConstructor
public class SharedServiceImpl implements SharedService {
    @NotNull
    private final GatherPersonRepository gatherPersonRepository;

    @NotNull
    private final GatherRepository gatherRepository;

    @NotNull
    private final PersonRepository personRepository;

    @NotNull
    private final UserRepository userRepository;


    @Override
    public Person getMe() {
        User user = userRepository.findBySessionKey()
                .orElseThrow(() -> new CommonException(401, "用户已过期"));
        return personRepository.findByOauthId(user.getOauthId())
                .orElseThrow(() -> new CommonException(401, "未找到用户"));
    }

    @Override
    public Person setMe(Person person) {
        Person me = getMe();
        if (ObjectUtils.isNotEmpty(person.getAlias())) {
            me.setAlias(person.getAlias());
        }
        if (ObjectUtils.isNotEmpty(person.getAvatar())) {
            me.setAvatar(person.getAvatar());
        }
        return personRepository.save(me);
    }


    @Override
    public List<Person> getPeronByNotInGatherId(Long id) {
        List<GatherPerson> gatherPersonList = gatherPersonRepository.findAllByGatherId(id);
        List<Long> personIdList = gatherPersonList
                .stream()
                .map(GatherPerson::getPersonId)
                .collect(Collectors.toList());
        if (personIdList.size() == 0) {
            return personRepository.findAll();
        } else {
            return personRepository.findAllByIdNotIn(personIdList);
        }

    }

    @Override
    public List<Person> getStudentByGatherId(Long id) {
        List<GatherPerson> gatherPersonList = gatherPersonRepository.findAllByGatherIdAndControl(id, false);
        List<Long> personIdList = gatherPersonList
                .stream()
                .map(GatherPerson::getPersonId)
                .collect(Collectors.toList());
        return personRepository.findAllByIdIn(personIdList);
    }

    @Override
    public List<Person> getTeacherByGatherId(Long id) {
        List<GatherPerson> gatherPersonList = gatherPersonRepository.findAllByGatherIdAndControl(id, true);
        List<Long> personIdList = gatherPersonList
                .stream()
                .map(GatherPerson::getPersonId)
                .collect(Collectors.toList());
        return personRepository.findAllByIdIn(personIdList);
    }


}
