package in.bugr.service;

/**
 * @author BugRui
 * @date 2020/3/12 下午3:55
 **/
public interface FaceRecognizeService {
    /**
     * 检测是否只有一个人脸
     *
     * @param imgBytes
     */
    String detectAndCrop(byte[] imgBytes);

    /**
     * 比较两个人脸信息是否相似
     *
     * @param personId1 数据库id
     * @param imgBytes  img图片
     */
    void compare(Long personId1, byte[] imgBytes);

    /**
     * 注册人脸信息到FDB
     *
     * @param personId
     * @param gatherId
     */
    void register(Long personId, Long gatherId);

    /**
     * 从gather里面删除person
     *
     * @param personId person Id
     * @param gatherId gather
     */
    void deletePersonFromGather(Long personId, Long gatherId);
}
