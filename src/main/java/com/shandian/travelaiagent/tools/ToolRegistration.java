package com.shandian.travelaiagent.tools;

import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 集中的工具注册类
 */
@Configuration
public class ToolRegistration {

    @Value("${search-api.api-key}")
    private String searchApiKey;

    @Bean
    public ToolCallback[] allTools() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        WebSearchTool webSearchTool = new WebSearchTool(searchApiKey);
        WebReadTool webScrapingTool = new WebReadTool();
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        TerminalOperationTool terminalOperationTool = new TerminalOperationTool();
        PDFGenerationTool pdfGenerationTool = new PDFGenerationTool();
        TerminateTool terminateTool = new TerminateTool();
        return ToolCallbacks.from(
                fileOperationTool,     //读写本地文件
                webSearchTool,         //调用 SearchAPI 做百度搜索，返回前 5 条自然结果（JSON 字符串拼接）。
                webScrapingTool,       //抓取网页并返回 HTML。
                resourceDownloadTool,  //从 URL 下载资源到本地
                terminalOperationTool, //在系统终端执行命令并返回输出
                pdfGenerationTool,     //把给定文本生成 PDF 文件
                terminateTool          //终止任务（被上层识别为“结束交互”的信号）
        );
    }
}

