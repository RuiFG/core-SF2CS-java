package in.bugr.component;

import com.google.gson.Gson;
import in.bugr.common.ImageHelper;
import in.bugr.common.component.ContextSpringConfigurator;
import in.bugr.common.entity.Gather;
import in.bugr.jni.FaceEngineFacade;
import in.bugr.jni.model.FaceDataBaseData;
import in.bugr.jni.model.FaceInfoArray;
import in.bugr.jni.model.ImageData;
import in.bugr.jni.model.PointFloatArray;
import in.bugr.jni.model.QueryResult;
import in.bugr.common.message.Constant;
import in.bugr.common.message.RecognitionResult;
import in.bugr.common.repository.GatherPersonRepository;
import in.bugr.common.repository.GatherRepository;
import in.bugr.common.entity.GatherPerson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author BugRui
 * @date 2020/3/31 下午1:32
 **/
@ServerEndpoint(value = "/detect/{gatherId}", configurator = ContextSpringConfigurator.class)
@Component
@RequiredArgsConstructor
@Slf4j
public class DetectSocketHandler {

    private final static Gson GSON = new Gson();

    @NotNull
    private final FaceEngineFacedPool faceEngineFacedPool;
    @NotNull
    private final GatherRepository gatherRepository;
    @NotNull
    private final GatherPersonRepository gatherPersonRepository;

    private final static Map<Long, Session> SESSION_MAP = new ConcurrentHashMap<>(16);
    private final Map<Long, FaceEngineFacade> engineFacadeMap = new ConcurrentHashMap<>(16);
    private final Map<Long, ExecutorService> engineExecutorMap = new ConcurrentHashMap<>(16);

    @OnOpen
    public void onOpen(@PathParam("gatherId") Long gatherId, Session session) throws IOException {
        if (SESSION_MAP.containsKey(gatherId)) {
            log.info("重复连接,关闭连接");
            session.close();
            return;
        }
        Gather gather = gatherRepository.findById(gatherId).orElse(null);
        if (ObjectUtils.isEmpty(gather)) {
            log.info("未找到gather,关闭连接");
            session.close();
            return;
        }
        FaceEngineFacade faceEngineFacade = faceEngineFacedPool.borrowO();
        if (!faceEngineFacade.load(FaceDataBaseData.ModelMapper.toFaceDataBaseData(gather))) {
            log.error("faceEngine数据出错,关闭连接");
            faceEngineFacade.death();
            faceEngineFacedPool.returnO(faceEngineFacade);
            session.close();
            return;
        }
        SESSION_MAP.put(gatherId, session);
        engineFacadeMap.put(gatherId, faceEngineFacade);
        engineExecutorMap.put(gatherId, Executors.newSingleThreadExecutor());
    }

    @OnClose
    public void onClose(@PathParam("gatherId") Long gatherId) {
        log.info("关闭");
        if (SESSION_MAP.containsKey(gatherId)) {
            SESSION_MAP.remove(gatherId);
            faceEngineFacedPool.returnO(engineFacadeMap.remove(gatherId));
            engineExecutorMap.remove(gatherId).shutdown();
        }
    }

    @OnError
    public void onError(@PathParam("gatherId") Long gatherId, Throwable error) {
        log.error("websocket出现错误");
        if (SESSION_MAP.containsKey(gatherId)) {
            engineFacadeMap.get(gatherId).death();
        }
        error.printStackTrace();
    }

    @OnMessage(maxMessageSize = Constant.MAX_MESSAGE_SIZE)
    public void onMessage(@PathParam("gatherId") Long gatherId, byte[] imgData, Session session) throws IOException {
        long begin = System.currentTimeMillis();
        List<GatherPerson> gatherPersonList = gatherPersonRepository.findAllByGatherIdAndControl(gatherId, false);
        if (CollectionUtils.isEmpty(gatherPersonList)) {
            log.error("gather 为空");
            session.close();
            return;
        }
        //获取属于当前gather的gatherPerson Map 以faceIndex为键
        Map<Integer, GatherPerson> gatherPersonMap = gatherPersonList.stream()
                .collect(Collectors.toMap(GatherPerson::getGatherFaceIndex, gatherPerson -> gatherPerson));
        FaceEngineFacade faceEngineFacade = engineFacadeMap.get(gatherId);
        if (BooleanUtils.isFalse(faceEngineFacade.isLive())) {
            log.info("engine已死");
            session.close();
            return;
        }
        ExecutorService executorService = engineExecutorMap.get(gatherId);
        //初始化resultEntity 并赋予初值
        RecognitionResult recognitionResult = new RecognitionResult();
        executorService.submit(() -> {
            try {
                FaceInfoArray faceInfoArray = faceEngineFacade.detectFace(imgData);
                if (ObjectUtils.isNotEmpty(faceInfoArray)) {
                    for (int index = 0; index < faceInfoArray.size; index++) {
                        PointFloatArray pointFloatArray = faceEngineFacade.detectPoints(ImageData.ModelMapper.toImageData(imgData), faceInfoArray.faceInfos[index]);
                        ImageData cropFaceData = faceEngineFacade.crop(ImageData.ModelMapper.toImageData(imgData), pointFloatArray);
                        QueryResult result = faceEngineFacade.queryByCroppedFace(cropFaceData);
                        if (ObjectUtils.isNotEmpty(result) || result.index != -1) {
                            GatherPerson gatherPerson = gatherPersonMap.get(result.index);
                            recognitionResult.add(gatherPerson.getPersonId(),
                                    result.score, ImgRepository.writeFace(ImageHelper.bgrToBufferedImage(cropFaceData.data, cropFaceData.width, cropFaceData.height)));
                        }
                    }
                }
            } catch (IOException e) {
                //TODO 异常
                log.error("识别失败");
            } finally {
                sendObject(session, recognitionResult);
            }
            log.info("识别耗时:" + (System.currentTimeMillis() - begin) + "ms");
        });
    }

    private static void sendObjectToAll(Object o) {
        SESSION_MAP.forEach((id, session) -> {
            sendObject(session, o);
        });
    }

    private static void sendObject(Session session, Object o) {
        try {
            session.getBasicRemote().sendText(GSON.toJson(o));
        } catch (IOException e) {
            e.printStackTrace();
            log.error("发送信息失败");
        }
    }
}
