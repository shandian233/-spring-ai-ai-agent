package com.shandian.travelaiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class PDFGenerationToolTest {

    @Test
    void generatePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "1234.pdf";
        String content = "测试中文显示：你好世界，Spring Boot 测试通过！";
        String result = tool.generatePDF(fileName, content);
        assertNotNull(result);
    }
}