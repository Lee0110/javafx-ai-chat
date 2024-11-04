package com.example.aichat.conversation;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.*;

public class ChatConversation extends BaseConversation {
  public ChatConversation(int chatMemorySize, List<IRobot> robotList, String subject) {
    super(chatMemorySize, subject, robotList);
  }

  @Override
  protected void doPreAddMemory(String input) {
    chatMemory.add(new UserMessage(input));
  }

  @Override
  protected void doPostAddMemory(String reply) {
    chatMemory.add(new AssistantMessage(reply));
  }

  public void export() {
    // 将会话内容导出到markdown文件中
  }

  @Override
  protected void doClearChatMemory() {
    chatMemory.clear();
  }
}
