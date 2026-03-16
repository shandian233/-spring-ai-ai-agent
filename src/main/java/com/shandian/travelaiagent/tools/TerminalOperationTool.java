package com.shandian.travelaiagent.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 终端操作工具
 */
public class TerminalOperationTool {

    @Tool(description = "Execute a command in the terminal")
    public String executeTerminalCommand(@ToolParam(description = "Command to execute in the terminal") String command) {
        StringBuilder output = new StringBuilder();   //用来收集子进程的标准输出（stdout）文本，最后拼成一个字符串返回。
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command); //构造子进程的“启动配置”。
//            Process process = Runtime.getRuntime().exec(command);
            Process process = builder.start();
            //代表已经启动的“操作系统子进程”。你可以通过它拿到：
//标准输出流：process.getInputStream()（被当作“输入流”是站在父进程角度来看）
//标准错误流：process.getErrorStream()
//标准输入流：process.getOutputStream()（对需要交互的命令，可以往里写）
//还可以调用 waitFor() 等待它结束，destroy() 结束它等。

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                output.append("Command execution failed with exit code: ").append(exitCode);
            }
        } catch (IOException | InterruptedException e) {
            output.append("Error executing command: ").append(e.getMessage());
        }
        return output.toString();
    }
}
