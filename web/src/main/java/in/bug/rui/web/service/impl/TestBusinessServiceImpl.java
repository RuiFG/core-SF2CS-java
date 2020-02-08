package in.bug.rui.web.service.impl;

import in.bug.rui.web.entity.Test;
import in.bug.rui.web.repository.TestRepository;
import in.bug.rui.web.service.TestBusinessService;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Cache;
import org.springframework.stereotype.Service;

import javax.persistence.Cacheable;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author BugRui
 * @date 2020/2/4 下午2:02
 **/
@Service
@RequiredArgsConstructor
public class TestBusinessServiceImpl implements TestBusinessService {
    private @NotNull
    final TestRepository testRepository;

    @Override
    public String test() {
        Test test = testRepository.findById(1L).orElseThrow(RuntimeException::new);
        return test.toString();
    }
}
