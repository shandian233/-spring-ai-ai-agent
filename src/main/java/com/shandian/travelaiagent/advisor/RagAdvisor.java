package com.shandian.travelaiagent.advisor;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;

@Configuration
@Slf4j
public class RagAdvisor {


    public static Advisor createRagAdvisor() {
        // 1. 构建 API 客户端
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey("xxxxx")
                .build();

        // 2. 构建检索器 (使用知识库 ID)
        DocumentRetriever documentRetriever = new DashScopeDocumentRetriever(
                dashScopeApi,
                DashScopeDocumentRetrieverOptions.builder()
                        .withIndexName("xxxxx") // 这里填知识库名字
                        .build()
        );

        // 3. 返回 Advisor
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
                .build();
    }
}
