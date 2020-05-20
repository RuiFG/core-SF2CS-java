package in.bugr.service.impl;

import in.bugr.common.entity.Gather;
import in.bugr.common.entity.GatherPerson;
import in.bugr.common.entity.Person;
import in.bugr.common.entity.User;
import in.bugr.common.exception.CommonException;
import in.bugr.common.repository.GatherPersonRepository;
import in.bugr.common.repository.GatherRepository;
import in.bugr.common.repository.PersonRepository;
import in.bugr.common.repository.UserRepository;
import in.bugr.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author BugRui
 * @date 2020/4/20 下午9:42
 **/
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    @NotNull
    private final GatherRepository gatherRepository;

    @NotNull
    private final GatherPersonRepository gatherPersonRepository;

    @NotNull
    private final PersonRepository personRepository;
    @NotNull
    private final UserRepository userRepository;

    @Override
    public List<Gather> getGathers() {
        User user = userRepository.findBySessionKey().orElseThrow(() -> new CommonException(401, "未找到用户"));
        Person person = personRepository.findByOauthId(user.getOauthId()).orElseThrow(() -> new CommonException(401, "未找到person"));
        List<GatherPerson> gatherPersonList = gatherPersonRepository.findAllByPersonIdAndControl(person.getId(), true);
        List<Long> gatherIdList = gatherPersonList.stream().map(GatherPerson::getGatherId)
                .collect(Collectors.toList());
        return gatherRepository.findAllById(gatherIdList);
    }

}
