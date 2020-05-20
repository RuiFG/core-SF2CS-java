package in.bugr.service;

import in.bugr.common.entity.Gather;
import in.bugr.common.entity.Person;
import in.bugr.entity.AttendanceDetail;

import java.util.List;

/**
 * @author BugRui
 * @date 2020/4/20 下午9:30
 **/
public interface SharedService {
    /**
     * get self  person' info
     *
     * @return {@link Person}
     */
    Person getMe();

    /**
     * set self person' info
     *
     * @param person changed person
     * @return {@link Person}
     */
    Person setMe(Person person);



    List<Person> getPeronByNotInGatherId(Long id);

    List<Person> getStudentByGatherId(Long id);

    List<Person> getTeacherByGatherId(Long id);
}
