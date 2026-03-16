//package com.shandian.travelaiagent.rag.local.PGVevtor;
//
//
//import com.shandian.travelaiagent.rag.local.TravelAppDocumentLoader;
//import jakarta.annotation.Resource;
//import org.springframework.ai.document.Document;
//import org.springframework.ai.embedding.BatchingStrategy;
//import org.springframework.ai.embedding.EmbeddingModel;
//import org.springframework.ai.vectorstore.VectorStore;
//import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import java.util.List;
//
//import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
//import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;
//
//// 为方便开发调试和部署，临时注释，如果需要使用 PgVector 存储知识库，取消注释即可
//@Configuration
//public class PGVectorStoreConfig {
//
//    @Resource
//    private TravelAppDocumentLoader travelAppDocumentLoader;
//
//    @Bean
//    public VectorStore pgVectorVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel dashscopeEmbeddingModel) {
//        VectorStore vectorStore = PgVectorStore.builder(jdbcTemplate, dashscopeEmbeddingModel)
//                .dimensions(1536)                    // Optional: defaults to model dimensions or 1536
//                .distanceType(COSINE_DISTANCE)       // Optional: defaults to COSINE_DISTANCE
//                .indexType(HNSW)                     // Optional: defaults to HNSW
//                .initializeSchema(true)              // Optional: defaults to false
//                .schemaName("public")                // Optional: defaults to "public"
//                .vectorTableName("vector_store")     // Optional: defaults to "vector_store"
//                .maxDocumentBatchSize(10000)         // Optional: defaults to 10000
//                .build();
//        // 加载文档
//        List<Document> documents = travelAppDocumentLoader.loadMarkdowns();
//        vectorStore.add(documents);
//        return vectorStore;
//    }
//}
//


package com.shandian.travelaiagent.rag.local.PGVevtor;

import com.shandian.travelaiagent.rag.local.TravelAppDocumentLoader;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.ArrayList;

import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;

//@Configuration   二选一
public class PGVectorStoreConfig {

    @Resource
    private TravelAppDocumentLoader travelAppDocumentLoader;

    @Bean
    public VectorStore pgVectorVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel dashscopeEmbeddingModel) {
        // 1. 构建 VectorStore
        VectorStore vectorStore = PgVectorStore.builder(jdbcTemplate, dashscopeEmbeddingModel)
                .dimensions(1536)
                .distanceType(COSINE_DISTANCE)
                .indexType(HNSW)
                .initializeSchema(true)
                .schemaName("public")
                .vectorTableName("vector_store")
                // 这里的 maxDocumentBatchSize 主要控制 DB 写入批次，但为了保险起见也可以调小，
                // 不过核心限制在 Embedding 模型调用层，必须手动分批。
                .maxDocumentBatchSize(10000)
                .build();

        // 2. 加载文档
        List<Document> documents = travelAppDocumentLoader.loadMarkdowns();

        if (documents != null && !documents.isEmpty()) {
            System.out.println("开始加载文档到向量库，总文档数: " + documents.size());

            // 3. 【关键修改】手动分批，每批最多 25 个，以适配阿里云 DashScope 限制
            int batchSize = 25;
            for (int i = 0; i < documents.size(); i += batchSize) {
                int end = Math.min(i + batchSize, documents.size());
                List<Document> subList = documents.subList(i, end);

                System.out.println("正在处理批次: " + (i / batchSize + 1) + ", 数量: " + subList.size());
                try {
                    vectorStore.add(subList);
                } catch (Exception e) {
                    System.err.println("批次处理失败: " + e.getMessage());
                    e.printStackTrace();
                    // 可以选择抛出异常停止启动，或者记录日志继续尝试下一批
                    throw new RuntimeException("向量入库失败", e);
                }
            }
            System.out.println("文档加载完成。");
        }

        return vectorStore;
    }
}