package in.bugr.jni;

import in.bugr.jni.model.FaceDataBaseData;
import in.bugr.jni.model.FaceInfoArray;
import in.bugr.jni.model.ImageData;
import in.bugr.jni.model.PointFloatArray;
import in.bugr.jni.model.QueryResult;
import in.bugr.jni.model.QueryResultArray;
import in.bugr.jni.model.Rect;

/**
 * @author BugRui
 * @date 2020/3/9 下午1:52
 **/
class FaceEngine {
    /**
     * 初始化，指定人脸识别模型文件目录，该目录下应当包括这3个文件：
     *
     * @param modelPath 模型路径
     * @return 初始化结果
     */
    native boolean init(String modelPath, int width, int height, int device, int id);

    /**
     * 识别人脸 返回图片存在的人脸信息
     *
     * @param img 图片数据
     * @return 人脸信息
     */
    native FaceInfoArray detectFace(ImageData img);

    /**
     * 5点或 81点检测
     *
     * @param img  图片数据
     * @param rect 人脸信息
     * @return 点数据
     */
    native PointFloatArray detectPoints(ImageData img, Rect rect);

    /**
     * 比较人脸相识度
     *
     * @param img1  图片数据
     * @param rect1 人脸信息
     * @param img2  图片数据
     * @param rect2 人脸信息
     * @return 相识度
     */
    native float compare(ImageData img1, Rect rect1, ImageData img2, Rect rect2);

    /**
     * 比较裁剪后的人脸相识度
     *
     * @param img1 人脸图片
     * @param img2 人脸图片
     * @return 相识度
     */
    native float compareByCroppedFace(ImageData img1, ImageData img2);

    /**
     * 裁剪人脸信息
     *
     * @param img             人脸图片
     * @param pointFloatArray 人脸点位信息
     * @return 裁剪后的人脸图片数据
     */
    native ImageData crop(ImageData img, PointFloatArray pointFloatArray);

    /**
     * 注册人脸到FDB
     *
     * @param img 人脸图片
     * @return 下标
     */
    native int registerByCroppedFace(ImageData img);

    /**
     * 在FDB中搜索人脸
     *
     * @param faceImg 人脸图片
     * @return 结果
     */
    native QueryResult queryByCroppedFace(ImageData faceImg);

    /**
     * 查询前n个匹配的人脸
     *
     * @param faceImg 人脸图片
     * @param n       N
     * @return 结果
     */
    native QueryResultArray queryTopByCroppedFace(ImageData faceImg, int n);

    /**
     * 查询大于阈值的人脸
     *
     * @param faceImg   人脸图片
     * @param threshold 阈值
     * @param n         N
     * @return 结果
     */
    native QueryResultArray queryAboveByCroppedFace(ImageData faceImg, float threshold, int n);

    /**
     * 当前人脸数据库中的人脸大小
     *
     * @return 结果
     */
    native int count();

    /**
     * Clear face database
     */
    native void clear();

    /**
     * 保存FDB中的人脸数据信息
     *
     * @return 数据信息
     */
    native FaceDataBaseData save();

    /**
     * 加载FDB人脸数据信息
     *
     * @param data 人脸数据信息
     * @return 加载结果
     */
    native boolean load(FaceDataBaseData data);

    /**
     * 设置属性
     *
     * @return 结果
     */
    native void set(int property, double value);

    /**
     * 获取属性
     *
     * @return 属性值
     */
    native double get(int property);


}
