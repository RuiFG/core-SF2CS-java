package in.bugr.server.service;

import in.bugr.entity.Student;

/**
 * @author BugRui
 * @date 2020/3/12 下午3:55
 **/
public interface FaceRecognizeService {

    boolean compare(Student student1, Student student2);
}
