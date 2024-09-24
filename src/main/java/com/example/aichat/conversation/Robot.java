package com.example.aichat.conversation;

public class Robot {
  private String id;

  private final String name;

  /**
   * 系统提示词
   */
  private final String systemPrompt;

  /**
   * 发言顺序
   */
  private final int order;

  public Robot(String name, String systemPrompt, int order) {
    this.name = name;
    this.systemPrompt = systemPrompt;
    this.order = order;
  }

  public String getSystemPrompt() {
    return systemPrompt;
  }

  public String getName() {
    return name;
  }
}
