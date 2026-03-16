package com.shandian.travelaiagent;

import com.shandian.travelaiagent.app.TravelApp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeAudioSpeechAutoConfiguration;
import com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeAudioTranscriptionAutoConfiguration;
import reactor.core.publisher.Flux;

import java.util.Scanner;
import java.util.UUID;

@SpringBootApplication(exclude = {
		// 排除语音相关自动配置，避免依赖缺失报错
		DashScopeAudioSpeechAutoConfiguration.class,
		DashScopeAudioTranscriptionAutoConfiguration.class

})
public class TravelAiAgentApplication {

	public static void main(String[] args) {
		// 1. 启动 Spring 容器
		ConfigurableApplicationContext context = SpringApplication.run(TravelAiAgentApplication.class, args);

		System.out.println("==================================================");
		System.out.println("✅ 旅行 AI 助手已启动！");
		System.out.println("💬 模式：普通对话 (如需切换 RAG 模式，请修改代码中的方法名)");
		System.out.println("🆔 会话 ID: 已自动生成 (保持上下文记忆)");
		System.out.println("🛑 输入 'quit' 或 'exit' 退出程序");
		System.out.println("==================================================");

		// 2. 获取 TravelApp 实例
		TravelApp travelApp;
		travelApp = context.getBean(TravelApp.class);
//		try {
//			travelApp = context.getBean(TravelApp.class);
//		} catch (Exception e) {
//			System.err.println("❌ 错误：无法找到 TravelApp 组件。请确保该类添加了 @Component 或 @Service 注解。");
//			e.printStackTrace();
//			context.close();
//			return;
//		}

		// 3. 生成一个唯一的会话 ID (UUID)，用于保持多轮对话记忆
		String chatId = UUID.randomUUID().toString();
		System.out.println("🔑 当前会话 ID: " + chatId.substring(0, 8) + "...");

		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.print("\n👤 你: ");
			String input = scanner.nextLine().trim();

			// 退出判断
			if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit")) {
				System.out.println("👋 再见！正在关闭系统...");
				break;
			}

			if (input.isEmpty()) {
				continue;
			}

			try {
				//这里可以选择Chat方法
				String response = travelApp.doChatWithTools(input, chatId);
				//Flux<String>response=travelApp.doChatByStream(input,chatId);
				System.out.println("🤖 AI: " + response);

			} catch (Exception e) {
				System.err.println("❌ 发生错误: " + e.getMessage());
				// 如果需要调试，可以取消下面这行的注释
				 //e.printStackTrace();
			}
		}

		scanner.close();
		context.close();
		System.exit(0);
	}
}