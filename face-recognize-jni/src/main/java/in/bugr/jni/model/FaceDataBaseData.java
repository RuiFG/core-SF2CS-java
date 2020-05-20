package in.bugr.jni.model;

import in.bugr.common.entity.Gather;

/**
 * @author BugRui
 * @date 2020/3/10 下午6:23
 **/
public class FaceDataBaseData {
    public byte[] data;
    public int size;

    public static class ModelMapper {
        public static FaceDataBaseData toFaceDataBaseData(Gather gather) {
            FaceDataBaseData faceDataBaseData = new FaceDataBaseData();
            faceDataBaseData.data = gather.getData();
            faceDataBaseData.size = gather.getData().length;
            return faceDataBaseData;
        }

        public static Gather toGather(FaceDataBaseData faceDataBaseData) {
            return new Gather().setData(faceDataBaseData.data)
                    .setSize(faceDataBaseData.size);
        }
    }
}
