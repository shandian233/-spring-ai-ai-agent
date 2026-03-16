package com.shandian.travelaiagent.rag.cloud;

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
public class TravelAppRagCloudAdvisorConfig{
    @Value("${spring.ai.dashscope.api-key}")
    private String dashScopeApikey;

    @Bean
    public Advisor travelAppRagCloudAdvisor(){
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(dashScopeApikey)
                .build();
        final String KNOWLEDGE_INDEX = "shandian旅游知识库";
        DocumentRetriever documentRetriever = new DashScopeDocumentRetriever(dashScopeApi,
                DashScopeDocumentRetrieverOptions.builder()
                        .withIndexName(KNOWLEDGE_INDEX)
                        .build());
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
                .build();

    }
}