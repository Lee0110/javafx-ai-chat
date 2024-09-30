package com.example.aichat.component;

import javafx.scene.control.Label;

public class ConversationLabel extends Label {
  private final String conversationId;

  public ConversationLabel(String conversationId, String text) {
    super(text);
    this.conversationId = conversationId;
    this.setPrefHeight(60);
  }

  public String getConversationId() {
    return conversationId;
  }
}
