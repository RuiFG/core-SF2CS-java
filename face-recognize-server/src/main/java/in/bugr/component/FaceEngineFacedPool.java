package in.bugr.component;

import in.bugr.config.FaceRecognizeProperty;
import in.bugr.jni.FaceEngineFacade;
import in.bugr.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

/**
 * @author BugRui
 * @date 2020/3/24 下午1:16
 **/
@Component
@RequiredArgsConstructor
public class FaceEngineFacedPool {

    static class FaceEngineFacedFactory implements PooledObjectFactory<FaceEngineFacade> {

        private final FaceRecognizeProperty property;

        public FaceEngineFacedFactory(FaceRecognizeProperty faceRecognizeProperty) {
            property = faceRecognizeProperty;
        }

        @Override
        public PooledObject<FaceEngineFacade> makeObject() {
            try {
                FaceEngineFacade faceEngineFacade = FaceEngineFacade.builder()
                        .setWidth(property.getCoreWidth())
                        .setHeight(property.getCoreHeight())
                        .setPoint(property.getPoint())
                        .setDevice(property.getDevice())
                        .setDeviceId(property.getDeviceId())
                        .setModelPath(property.getModelPath())
                        .setLibPath(property.getLibPath())
                        .build();
                return new DefaultPooledObject<>(faceEngineFacade);
            } catch (IllegalAccessException e) {
                throw new CommonException("初始化引擎失败");
            }
        }

        @Override
        public void destroyObject(PooledObject<FaceEngineFacade> pooledObject) {
            FaceEngineFacade facade = pooledObject.getObject();
            if (facade.isLive()) {
                facade.destroy();
            }
        }

        @Override
        public boolean validateObject(PooledObject<FaceEngineFacade> pooledObject) {
            return pooledObject.getObject().isLive();
        }

        @Override
        public void activateObject(PooledObject<FaceEngineFacade> pooledObject) {
        }

        @Override
        public void passivateObject(PooledObject<FaceEngineFacade> pooledObject) {
        }
    }

    @FunctionalInterface
    public interface Task<T>  {
        /**
         * 函数接口
         *
         * @param faceEngineFacade face engine
         * @return T
         */
        T run(FaceEngineFacade faceEngineFacade);
    }

    private @NotNull GenericObjectPool<FaceEngineFacade> pool;

    private @NotNull
    final FaceRecognizeProperty faceRecognizeProperty;

    @PostConstruct
    public void init() {
        GenericObjectPoolConfig<FaceEngineFacade> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(faceRecognizeProperty.getMaxPoolSize());
        poolConfig.setMinIdle(1);
        poolConfig.setMaxIdle(faceRecognizeProperty.getCorePoolSize());
        pool = new GenericObjectPool<>(new FaceEngineFacedFactory(faceRecognizeProperty), poolConfig);
    }

    public <T> T start(Task<T> task) {
        FaceEngineFacade faceEngineFacade = borrowO();
        try {
            return task.run(faceEngineFacade);
        } catch (Exception e) {
            throw e;
        } finally {
            returnO(faceEngineFacade);
        }
    }

    public FaceEngineFacade borrowO() {
        try {
            return pool.borrowObject(faceRecognizeProperty.getWaitSecond() * 1000);
        } catch (Exception e) {
            throw new CommonException("从对象池取出引擎失败:超时:" + faceRecognizeProperty.getWaitSecond());
        }
    }


    public void returnO(FaceEngineFacade faceEngineFacade) {
        faceEngineFacade.clear();
        if (faceEngineFacade.isLive()) {
            pool.returnObject(faceEngineFacade);
        } else {
            try {
                pool.invalidateObject(faceEngineFacade);
            } catch (Exception e) {
                throw new CommonException("不可再用失败");
            }
        }
    }

    public Integer getIdle() {
        return pool.getNumIdle();
    }

    public Integer getActive() {
        return pool.getNumActive();
    }

    public boolean convertCompare(float compare) {
        return compare >= faceRecognizeProperty.getThreshold();
    }
}
