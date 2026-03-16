package com.shandian.travelaiagent.app;


import com.shandian.travelaiagent.advisor.LoggerAdvisor;
import com.shandian.travelaiagent.advisor.RereadingAdvisor;
import com.shandian.travelaiagent.rag.QueryRewrite;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;

import java.util.List;

@Component
@Slf4j
public class TravelApp {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "你是一位南京旅游咨询方面的专家，下面用户会问你相关的问题，" +
            "";

    /**
     * 初始化 ChatClient
     *
     * @param dashscopeChatModel
     */
    public TravelApp(ChatModel dashscopeChatModel) {
//        // 初始化基于文件的对话记忆
//        String fileDir = System.getProperty("user.dir") + "/tmp/chat-memory";
//        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);
        // 初始化基于内存的对话记忆
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(20)
                .build();
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                        // 自定义日志 Advisor，可按需开启
                        //new MyLoggerAdvisor()
//                        // 自定义推理增强 Advisor，可按需开启
                       ,new RereadingAdvisor()
                )
                .build();
    }

    /**
     * AI 基础对话（支持多轮对话记忆）
     *
     * @param message
     * @param chatId
     * @return
     */
    public String doChat(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        //log.info("content: {}", content);
        //System.out.println(content);
        return content;
    }

    /**
     * 和 RAG 知识库进行对话
     *
     * @param message
     * @param chatId
     * @return
     */
    @Resource
    private Advisor travelAppRagCloudAdvisor;
    @Resource
    private QueryRewrite queryRewrite;
    @Resource
    private VectorStore pgVectorStore;
    @Resource
    private VectorStore travelAppVectorStore;
    public String doChatWithRag(String message, String chatId) {
        // 查询重写
       // String rewrittenMessage = queryRewrite.doQueryRewrite(message);
        ChatResponse chatResponse = chatClient
                .prompt()
                // 使用改写后的查询
                //.user(rewrittenMessage)
                .user(message)  //不用改写
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                // 开启日志，便于观察效果
                //.advisors(new MyLoggerAdvisor())
                // 应用 RAG 知识库问答
                //.advisors(new QuestionAnswerAdvisor(travelAppVectorStore))
                // 应用 RAG 检索增强服务（基于阿里云知识库服务）
                .advisors(travelAppRagCloudAdvisor)
                // 应用 RAG 检索增强服务（基于 PgVector 向量存储）
                //.advisors(new QuestionAnswerAdvisor(pgVectorStore))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        //log.info("content: {}", content);
        //System.out.println(content);
        return content;
    }
    // AI 调用工具能力
    @Resource
    private ToolCallback[] allTools;

    /**
     * （支持调用工具）
     *
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithTools(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                // 开启日志，便于观察效果
                //.advisors(new MyLoggerAdvisor())
                // 阿里云rag
                .advisors(travelAppRagCloudAdvisor)
                .toolCallbacks(allTools)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }
    public Flux<String> doChatByStream(String message, String chatId) {
        return chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .advisors(travelAppRagCloudAdvisor)
                //.toolCallbacks(allTools)         //后面加的
                .stream()
                .content();
    }


    @Resource
    private ToolCallbackProvider toolCallbackProvider;

    /**
     * AI （调用 MCP 服务）
     *
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithMcp(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                // 开启日志，便于观察效果
                //.advisors(new LoggerAdvisor())
                //阿里云rag服务
                .advisors(travelAppRagCloudAdvisor)
                .toolCallbacks(toolCallbackProvider)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

}