package com.example.aichat;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AIChatController {
  @FXML
  private TextField inputField;

  @FXML
  private VBox chatBox;

  @FXML
  private ScrollPane scrollPane;

  // 模拟的聊天回复方法
  private String chat(String input) {
    // 这里应该是你实现好的聊天回复逻辑
    return "回复: " + input;
  }

  @FXML
  private void handleSend() {
    String inputText = inputField.getText();
    if (!inputText.isEmpty()) {
      // 显示用户输入的消息
      addMessage(inputText, Pos.BASELINE_LEFT);

      // 获取回复消息并显示
      String reply = chat(inputText);
      addMessage(reply, Pos.BASELINE_RIGHT);

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
