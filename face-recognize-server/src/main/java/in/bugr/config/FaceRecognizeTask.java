package in.bugr.config;

import in.bugr.jni.FaceEngineFacade;

/**
 * @author BugRui
 * @date 2020/3/12 下午4:12
 **/
@FunctionalInterface
public interface FaceRecognizeTask<T> {


    /**
     * 函数接口
     *
     * @param faceEngineFacade face engine
     * @return T
     */
    T run(FaceEngineFacade faceEngineFacade);

}
