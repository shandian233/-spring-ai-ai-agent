
Travel AI Agent
智能旅游咨询助手 - 基于 Spring AI + Vue3 的 AI Agent 应用
项目简介
本项目是一个功能完善的 AI 智能旅游咨询系统，实现了两种 AI Agent 模式（ShandianManus 超级智能体 + ReAct 思考-执行模式），支持三种 RAG 检索增强方式，并提供 SSE 流式交互能力。前端采用 Vue3 + TypeScript 开发，支持实时流式输出。
---
技术栈
<img width="1281" height="697" alt="image" src="https://github.com/user-attachments/assets/b544ad94-8488-4818-bcc3-82bcb848b168" />
<img width="1296" height="230" alt="image" src="https://github.com/user-attachments/assets/4ca290f8-8b51-403d-81e7-8cea5b735553" />

---
核心功能
1. 两种 AI Agent 模式
ShandianManus (超级智能体)
- 继承自 ToolCallAgent
- 具备自主规划能力，可自主选择和组合工具完成任务
- 支持最多 20 步推理
- 内置 Logger Advisor 和 RAG Advisor
- 可通过 terminate 工具随时终止交互
ReActAgent (思考-执行模式)
- 实现 Think-Act 循环模式
- 先思考 (think)：分析用户需求，决定是否需要调用工具
- 再执行 (act)：调用工具并处理返回结果
- ToolCallAgent 是 ReActAgent 的具体实现类
- 支持多轮工具调用，直到任务完成
// ReAct 模式执行流程
@Override
public String step() {
    // 1. 先思考 - 决定是否需要调用工具
    boolean shouldAct = think();
    if (!shouldAct) {
        return "思考完成 - 无需行动";
    }
    // 2. 再执行 - 调用选定的工具
    return act();
}
2. 三种 RAG 实现方式
方式一：本地向量库 (SimpleVectorStore)
- 文档加载 → 关键词 enrichment → 向量化 → 存储
- 支持 Markdown 文档解析
- 支持自定义文本切分
// 配置类：TravelAppVectorStoreConfig.java
List<Document> documents = travelAppDocumentLoader.loadMarkdowns();
List<Document> enrichedDocuments = keywordEnrich.enrichDocuments(documents);
simpleVectorStore.add(enrichedDocuments);
方式二：阿里云知识库 (DashScopeDocumentRetriever)
- 直接调用阿里云 RAG 服务
- 使用知识库 ID 进行检索
- 支持 shandian 旅游知识库
// 配置类：TravelAppRagCloudAdvisorConfig.java
DashScopeDocumentRetrieverOptions.builder()
    .withIndexName("shandian旅游知识库")
    .build();
方式三：PGVector 向量存储
- 基于 PostgreSQL 的向量数据库
- 支持 HNSW 索引
- 支持 COSINE_DISTANCE 相似度计算
spring:
  ai:
    vectorstore:
      pgvector:
        index-type: HNSW
        dimensions: 1536
        distance-type: COSINE_DISTANCE
3. SSE 流式交互 (3种实现方式)
项目实现了 3 种不同的 SSE 流式输出方式：
方式一：Flux 直接返回
@GetMapping(value = "/travel_app/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public Flux<String> doChatWithTravelAppSSE(String message, String chatId) {
    return travelApp.doChatByStream(message, chatId);
}
方式二：ServerSentEvent 封装
@GetMapping(value = "/travel_app/chat/server_sent_event")
public Flux<ServerSentEvent<String>> doChatWithTravelAppServerSentEvent(String message, String chatId) {
    return travelApp.doChatByStream(message, chatId)
            .map(chunk -> ServerSentEvent.<String>builder()
                    .data(chunk)
                    .build());
}
方式三：SseEmitter (支持长连接)
@GetMapping(value = "/travel_app/chat/sse_emitter")
public SseEmitter doChatWithTravelAppServerSseEmitter(String message, String chatId) {
    SseEmitter sseEmitter = new SseEmitter(180000L); // 3 分钟超时
    travelApp.doChatByStream(message, chatId)
            .subscribe(chunk -> sseEmitter.send(chunk), ...);
    return sseEmitter;
}
4. 多种 Advisor
Advisor
LoggerAdvisor
RereadingAdvisor
RagAdvisor
MessageChatMemoryAdvisor
QuestionAnswerAdvisor
---
工具系统
项目内置多种 AI 工具：
工具
WebSearchTool
WebReadTool
FileOperationTool
PDFGenerationTool
ResourceDownloadTool
TerminalOperationTool
TerminateTool
---
快速开始
环境要求
- Java 21+
- Maven 3.6+
- Node.js 18+
- PostgreSQL 15+ (可选，用于 PGVector)
后端启动
# 1. 克隆项目
git clone https://github.com/shandian233/-spring-ai-ai-agent.git
cd travel-ai-agent
# 2. 配置 API Key
# 编辑 src/main/resources/application.yml
spring:
  ai:
    dashscope:
      apikey: "your-api-key"
# 3. 启动后端
./mvnw spring-boot:run
# 或
mvn spring-boot:run
前端启动
# frontend1 (Vue3 版本)
cd frontend1
npm install
npm run dev
# frontend2 (Vue3.5 + TS 版本)
cd frontend2
pnpm install
pnpm run dev
访问
- 后端 API：http://localhost:8123/api
- Swagger 文档：http://localhost:8123/api/doc.html
- 前端1：http://localhost:5173
- 前端2：http://localhost:5174
---
项目结构
travel-ai-agent/
├── src/main/java/com/shandian/travelaiagent/
│   ├── agent/                    # AI Agent 核心
│   │   ├── BaseAgent.java        # Agent 基类
│   │   ├── ReActAgent.java       # ReAct 模式抽象类
│   │   ├── ToolCallAgent.java    # 工具调用 Agent 实现
│   │   ├── ShandianManus.java    # 超级智能体
│   │   └── model/
│   │       └── AgentState.java   # Agent 状态枚举
│   ├── advisor/                  # Advisor 组件
│   │   ├── LoggerAdvisor.java    # 日志记录
│   │   ├── RagAdvisor.java       # RAG 增强
│   │   └── RereadingAdvisor.java # 重新阅读
│   ├── app/
│   │   └── TravelApp.java        # 旅游咨询应用
│   ├── controller/
│   │   └── AiController.java     # API 控制器
│   ├── rag/                      # RAG 相关
│   │   ├── cloud/                # 云端 RAG
│   │   │   └── TravelAppRagCloudAdvisorConfig.java
│   │   ├── local/                # 本地 RAG
│   │   │   ├── PGVevtor/
│   │   │   ├── TravelAppDocumentLoader.java
│   │   │   └── TravelAppVectorStoreConfig.java
│   │   ├── KeyWordEnrich.java
│   │   ├── QueryRewrite.java
│   │   └── TokenTextSplit.java
│   └── tools/                    # 工具集
│       ├── WebSearchTool.java
│       ├── WebReadTool.java
│       ├── FileOperationTool.java
│       ├── PDFGenerationTool.java
│       └── ...
├── src/main/resources/
│   ├── application.yml            # 配置文件
│   └── documents/                 # RAG 文档
├── frontend1/                     # Vue3 前端
├── frontend2/                     # Vue3.5 + TS 前端
└── pom.xml                        # Maven 依赖
---
API 接口
接口
/api/ai/travel_app/chat/sync
/api/ai/travel_app/chat/sse
/api/ai/travel_app/chat/server_sent_event
/api/ai/travel_app/chat/sse_emitter
/api/ai/manus/chat

---
许可证
MIT License
---
