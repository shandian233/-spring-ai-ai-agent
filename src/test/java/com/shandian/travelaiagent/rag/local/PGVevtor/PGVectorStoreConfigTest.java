package com.shandian.travelaiagent.rag.local.PGVevtor;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ai.document.Document;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class PGVectorStoreConfigTest {

    @Resource
    VectorStore pgVectorStore;

    @Test
    void test() {
        List<Document> documents = List.of(
                new Document("南京邮电大学真是好学校啊", Map.of("meta1", "meta1")),
                new Document("南京夏天炎热冬天寒冷"),
                new Document("秋天的南京栖霞山景色很好，适合赏花", Map.of("meta2", "meta2")));

        pgVectorStore.add(documents);

        List<Document> results = pgVectorStore.similaritySearch(SearchRequest.builder().query("什么时候去南京赏花？").topK(5).build());
        Assertions.assertNotNull(results);
    }
}