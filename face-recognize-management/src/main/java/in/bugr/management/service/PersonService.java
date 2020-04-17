package in.bugr.management.service;

import in.bugr.entity.Person;

/**
 * @author BugRui
 * @date 2020/4/4 下午2:00
 **/
public interface PersonService {
    /**
     * 通过oauth Id注册
     *
     * @param person
     * @return result
     */
    Person registerByOauth(Person person);

    /**
     * 修改person信息 只能修改别名
     *
     * @param person 用户信息
     * @return 修改后的信息
     */
    Person updatePerson(Person person);
}
