package com.shandian.travelaiagent.agent;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ShandianManusTest {

    @Resource
    private ShandianManus shandianManus;

    @Test
    void run() {
        String userPrompt = """  
                用中文简单介绍荣耀公司的流程it部门，
                然后保存为 txt和pdf 文件。""";
        String answer = shandianManus.run(userPrompt);
        Assertions.assertNotNull(answer);
    }
}