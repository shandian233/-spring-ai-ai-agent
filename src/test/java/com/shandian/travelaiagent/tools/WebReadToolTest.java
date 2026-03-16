package com.shandian.travelaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebReadToolTest {

    @Test
    void readWebPage() {
        WebReadTool webScrapingTool = new WebReadTool();
        String url = "https://blog.csdn.net/weixin_74066883";
        String result = webScrapingTool.readWebPage(url);
        Assertions.assertNotNull(result);
    }
}