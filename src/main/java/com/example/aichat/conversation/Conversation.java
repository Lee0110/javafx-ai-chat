package com.example.aichat.conversation;

import com.example.aichat.component.ConversationLabel;
import com.example.aichat.util.AIUtil;
import com.example.aichat.util.FixedSizeQueue;
import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Conversation {
  private final String id;

  /**
   * 聊天记录
   */
  private final FixedSizeQueue<Message> chatMemory;

  /**
   * 机器人列表
   */
  private final List<Robot> robotList;

  /**
   * 聊天框
   */
  private final VBox chatBox;

  /**
   * 会话标签
   */
  private final ConversationLabel conversationLabel;

  /**
   * 线程池
   */
  private final ExecutorService executorService;

  public Conversation(int chatMemorySize, List<Robot> robotList, String subject) {
    this.id = UUID.randomUUID().toString();
    this.chatMemory = new FixedSizeQueue<>(chatMemorySize);
    this.robotList = robotList;
    this.executorService = Executors.newFixedThreadPool(10);
    this.chatBox = new VBox();
    configureChatBox();
    this.conversationLabel = new ConversationLabel(id, subject);
  }

  private void configureChatBox() {
    this.chatBox.setPrefHeight(400);
    this.chatBox.setPrefWidth(590);
    this.chatBox.setSpacing(5);
    this.chatBox.setPadding(new Insets(10, 10, 10, 10));
  }

  public void chat(String input) {
    executorService.submit(() -> {
      chatMemory.add(new UserMessage(input));
      Platform.runLater(() -> this.chatBox.getChildren().add(getMessageHBox(input, Pos.BASELINE_RIGHT)));
      for (Robot robot : robotList) {
        String reply = robot.getName() + "：" + AIUtil.chat(robot.getSystemPrompt(), chatMemory.toList());
        Platform.runLater(() -> this.chatBox.getChildren().add(getMessageHBox(reply, Pos.BASELINE_LEFT)));
        Platform.runLater(() -> {
          if (!conversationLabel.getText().startsWith("（新）")) {
            this.conversationLabel.setText("（新）" + conversationLabel.getText());
          }
        });
        chatMemory.add(new UserMessage(reply));
      }
    });
  }

  public VBox getChatBox() {
    return chatBox;
  }

  private HBox getMessageHBox(String message, Pos alignment) {
    HBox messageBox = new HBox();
    messageBox.setAlignment(alignment);
    messageBox.setSpacing(10);

    // 消息内容
    JFXTextArea messageTextArea = new JFXTextArea(message);
    messageTextArea.setWrapText(true);
    messageTextArea.setEditable(false);
    messageTextArea.setPrefRowCount(message.split("\n").length);
    if (alignment == Pos.BASELINE_RIGHT) {
      messageTextArea.setStyle("-fx-background-color: rgba(255, 255, 255, 0.4); -fx-padding: 10; -fx-background-radius: 10;");
    } else {
      messageTextArea.setStyle("-fx-background-color: rgba(190, 250, 250, 0.6); -fx-padding: 10; -fx-background-radius: 10;");
    }
    messageTextArea.textProperty().addListener((observable, oldValue, newValue) -> messageTextArea.setPrefRowCount(newValue.split("\n").length));
    messageBox.getChildren().add(messageTextArea);
    return messageBox;
  }

  private void adjustTextAreaSize() {
    // todo
  }

  public void clearChatMemory() {
    chatMemory.clear();
    chatBox.getChildren().clear();
  }

  public String getId() {
    return id;
  }

  public ConversationLabel getConversationLabel() {
    return conversationLabel;
  }

  public void export() {
    // 将会话内容导出到markdown文件中

  }
}
