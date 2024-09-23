package com.example.aichat.conversation;

public class Robot {
  private String id;

  /**
   * 系统提示词
   */
  private String systemPrompt;

  /**
   * 发言顺序
   */
  private int order;

  public String getSystemPrompt() {
    return systemPrompt;
  }
}
