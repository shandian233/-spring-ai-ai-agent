package com.shandian.travelaiagent.rag.local;

import com.shandian.travelaiagent.rag.KeyWordEnrich;
import com.shandian.travelaiagent.rag.TokenTextSplit;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class TravelAppVectorStoreConfig {

    @Resource
    private TravelAppDocumentLoader travelAppDocumentLoader;

    @Resource
    private TokenTextSplit tokenTextSplit;

   @Resource
   private KeyWordEnrich keywordEnrich;
    @Bean
    VectorStore travelAppVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel).build();
        // 加载文档
        List<Document> documentList = travelAppDocumentLoader.loadMarkdowns();
        // 自主切分文档
//        List<Document> splitDocuments = tokenTextSplit.splitCustomized(documentList);
        // 自动补充关键词元信息
       List<Document> enrichedDocuments = keywordEnrich.enrichDocuments(documentList);
        simpleVectorStore.add(enrichedDocuments);
        return simpleVectorStore;
    }
}
