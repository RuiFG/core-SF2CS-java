package in.bugr.management.service.impl;

import in.bugr.entity.Gather;
import in.bugr.exception.CommonException;
import in.bugr.repository.GatherRepository;
import in.bugr.management.service.GatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

/**
 * @author BugRui
 * @date 2020/4/4 下午4:13
 **/
@Service
@RequiredArgsConstructor
public class GatherServiceImpl implements GatherService {
    @NotNull
    private final GatherRepository gatherRepository;

    @Override
    public Gather updateGather(Gather gather) {
        Gather oldGather = gatherRepository.findById(gather.getId()).orElseThrow(() -> new CommonException("未找到gather"));
        oldGather.setName(gather.getName());
        return gatherRepository.save(oldGather);
    }
}
