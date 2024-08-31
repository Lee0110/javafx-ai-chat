package com.example.aichat.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Component;

@Component
public class AIChatController {
  @FXML
  private TextField inputField;

  @FXML
  private VBox chatBox;

  @FXML
  private ScrollPane scrollPane;

  private final ChatModel chatModel;

  public AIChatController(ChatModel chatModel) {
    this.chatModel = chatModel;
  }

  // 模拟的聊天回复方法
  private String chat(String input) {
    // 这里应该是你实现好的聊天回复逻辑
    String call = chatModel.call(input);
    return "回复: " + call;
  }

  @FXML
  private void handleSend() {
    String inputText = inputField.getText();
    if (!inputText.isEmpty()) {
      // 显示用户输入的消息
      addMessage(inputText, Pos.BASELINE_RIGHT);

      // 获取回复消息并显示
      String reply = chat(inputText);
      addMessage(reply, Pos.BASELINE_LEFT);

      // 清空输入框
      inputField.clear();
    }
  }

  @FXML
  private void handleClearChat() {
    chatBox.getChildren().clear();
  }

  private void addMessage(String message, Pos alignment) {
    HBox messageBox = new HBox();
    messageBox.setAlignment(alignment);
    messageBox.getChildren().add(new Label(message));
    chatBox.getChildren().add(messageBox);

    // 滚动到最底部
    scrollPane.layout();
    scrollPane.setVvalue(1.0);
  }
}
