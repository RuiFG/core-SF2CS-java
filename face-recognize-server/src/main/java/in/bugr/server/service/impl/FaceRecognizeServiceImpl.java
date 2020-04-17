package in.bugr.server.service.impl;

import in.bugr.entity.Gather;
import in.bugr.entity.GatherPerson;
import in.bugr.entity.Person;
import in.bugr.exception.CommonException;
import in.bugr.jni.model.FaceDataBaseData;
import in.bugr.jni.model.FaceInfoArray;
import in.bugr.jni.model.ImageData;
import in.bugr.jni.model.PointFloatArray;
import in.bugr.repository.GatherPersonRepository;
import in.bugr.repository.GatherRepository;
import in.bugr.repository.PersonRepository;
import in.bugr.server.component.FaceEngineFacedPool;
import in.bugr.server.service.FaceRecognizeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * @author BugRui
 * @date 2020/3/12 下午3:55
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class FaceRecognizeServiceImpl implements FaceRecognizeService {

    private @NotNull
    final FaceEngineFacedPool faceEngineFacedPool;

    private @NotNull
    final PersonRepository personRepository;

    private @NotNull
    final GatherPersonRepository gatherPersonRepository;

    private @NotNull
    final GatherRepository gatherRepository;


    @Override
    public void detect(byte[] imgBytes) {
        faceEngineFacedPool.start(faceEngineFacade -> {
            try {
                FaceInfoArray faceInfoArray = faceEngineFacade.detectFace(imgBytes);
                if (ObjectUtils.isEmpty(faceInfoArray)) {
                    throw new CommonException("未检测到人脸");
                }
                if (faceInfoArray.size != 1) {
                    throw new CommonException("图片人脸数量大于1,请重新上传");
                }
            } catch (IOException e) {
                log.info("识别失败");
                throw new CommonException("识别失败");
            }
            return true;
        });
    }


    @Override
    public void compare(Long personId1, byte[] imgBytes) {
        Person person1 = personRepository.findById(personId1).orElseThrow(() -> new CommonException("person不存在"));
        faceEngineFacedPool.start(faceEngineFacade -> {
            try {
                ImageData faceImg = ImageData.ModelMapper.toImageData(imgBytes);
                FaceInfoArray faceInfoArray = faceEngineFacade.detectFace(faceImg);
                if (ObjectUtils.isEmpty(faceInfoArray)) {
                    throw new CommonException("未检测到人脸");
                }
                if (faceInfoArray.size != 1) {
                    throw new CommonException("图片人脸数量大于1,请重新上传");
                }
                PointFloatArray pointFloatArray = faceEngineFacade.detectPoints(faceImg, faceInfoArray.faceInfos[0]);
                ImageData cropFaceImg = faceEngineFacade.crop(faceImg, pointFloatArray);
                float compare = faceEngineFacade.compareByCroppedFace(
                        ImageData.ModelMapper.toImageData(person1),
                        cropFaceImg);
                boolean flag = faceEngineFacedPool.convertCompare(compare);
                if (BooleanUtils.isFalse(flag)) {
                    throw new CommonException("人脸匹配未通过");
                }
            } catch (IOException e) {
                throw new CommonException("识别失败");
            }
            return true;
        });
    }

    @Override
    @Transactional(rollbackFor = CommonException.class, isolation = Isolation.SERIALIZABLE)
    public void register(Long personId, Long gatherId) {
        Gather gather = gatherRepository.findById(gatherId).orElseThrow(() -> new CommonException("gather不存在"));
        Person person = personRepository.findById(personId).orElseThrow(() -> new CommonException("person不存在"));
        GatherPerson gatherPerson = gatherPersonRepository.findByGatherIdAndPersonId(gatherId, personId).orElse(new GatherPerson());
        if (gatherPerson.getGatherFaceIndex() != -1) {
            return;
        }
        faceEngineFacedPool.start(faceEngineFacade -> {
            boolean load = true;
            if (gather.getSize() != 0) {
                load = faceEngineFacade.load(FaceDataBaseData.ModelMapper.toFaceDataBaseData(gather));
            }
            if (BooleanUtils.isFalse(load)) {
                throw new CommonException("加载FDB失败");
            }
            int index;
            try {
                index = faceEngineFacade.registerByCroppedFace(ImageData.ModelMapper.toImageData(person));
            } catch (IOException e) {
                throw new CommonException("注册出错");
            }
            gatherPerson.setGatherId(gatherId);
            gatherPerson.setPersonId(personId);
            gatherPerson.setGatherFaceIndex(index);
            gatherPersonRepository.save(gatherPerson);
            FaceDataBaseData faceDataBaseData = faceEngineFacade.save();
            gather.setData(faceDataBaseData.data).setSize(faceDataBaseData.size);
            gatherRepository.save(gather);
            return true;
        });


    }

    @Override
    public void deletePersonFromGather(Long personId, Long gatherId) {
        Gather gather = gatherRepository.findById(gatherId).orElseThrow(() -> new CommonException("gather不存在"));
        GatherPerson gatherPerson = gatherPersonRepository.findByGatherIdAndPersonId(gatherId, personId).orElseThrow(() -> new CommonException("person未在gather中"));
        if (gather.getSize() == 0) {
            throw new CommonException("gather中人脸为空");
        }
        faceEngineFacedPool.start(faceEngineFacade -> {
            boolean load = faceEngineFacade.load(FaceDataBaseData.ModelMapper.toFaceDataBaseData(gather));
            if (BooleanUtils.isFalse(load)) {
                throw new CommonException("加载FDB失败");
            }
            boolean flag = faceEngineFacade.delete(gatherPerson.getGatherFaceIndex());
            if (BooleanUtils.isFalse(flag)) {
                throw new CommonException("从gather中删除失败");
            }
            gatherPersonRepository.deleteById(gatherPerson.getId());
            FaceDataBaseData faceDataBaseData = faceEngineFacade.save();
            gather.setData(faceDataBaseData.data).setSize(faceDataBaseData.size);
            gatherRepository.save(gather);
            return true;
        });
    }
}
