package in.bugr.management.service.impl;

import in.bugr.entity.Person;
import in.bugr.exception.CommonException;
import in.bugr.repository.PersonRepository;
import in.bugr.management.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

/**
 * @author BugRui
 * @date 2020/4/4 下午2:06
 **/
@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    @NotNull
    private final PersonRepository personRepository;


    @Override
    public Person registerByOauth(Person person) {
        Person newPerson = personRepository.findByOauthId(person.getOauthId()).orElse(new Person());
        if (ObjectUtils.isNotEmpty(newPerson.getId())) {
            throw new CommonException("用户已注册");
        }
        newPerson.setOauthId(person.getOauthId())
                .setAlias(person.getAlias());
        return personRepository.save(newPerson);
    }

    @Override
    public Person updatePerson(Person person) {
        Person oldPerson = personRepository.findById(person.getId()).orElseThrow(() -> new CommonException("未找到用户"));
        oldPerson.setAlias(person.getAlias());
        return personRepository.save(oldPerson);
    }
}
