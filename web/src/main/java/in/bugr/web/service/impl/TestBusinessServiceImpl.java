package in.bugr.web.service.impl;

import in.bugr.web.entity.Test;
import in.bugr.web.repository.TestRepository;
import in.bugr.web.service.TestBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

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
