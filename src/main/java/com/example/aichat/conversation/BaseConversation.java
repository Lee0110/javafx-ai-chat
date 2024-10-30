package com.example.aichat.conversation;

import com.example.aichat.component.ConversationLabel;
import com.example.aichat.util.FixedSizeQueue;
import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.ai.chat.messages.Message;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class BaseConversation implements IConversation {
  protected final String id;

  /**
   * 聊天框
   */
  protected final VBox chatBox;

  /**
   * 会话标签
   */
  protected final ConversationLabel conversationLabel;

  /**
   * 机器人列表
   */
  protected final List<IRobot> robotList;

  /**
   * 聊天记录
   */
  protected final FixedSizeQueue<Message> chatMemory;

  protected final ExecutorService executorService = Executors.newFixedThreadPool(10);

  protected BaseConversation(int chatMemorySize, String subject, List<IRobot> robotList) {
    this.id = UUID.randomUUID().toString();
    chatBox = new VBox();
    configureChatBox();
    this.conversationLabel = new ConversationLabel(id, subject);
    this.robotList = robotList;
    this.chatMemory = new FixedSizeQueue<>(chatMemorySize);
  }

  protected BaseConversation(String subject, List<IRobot> robotList) {
    this.id = UUID.randomUUID().toString();
    chatBox = new VBox();
    configureChatBox();
    this.conversationLabel = new ConversationLabel(id, subject);
    this.robotList = robotList;
    this.chatMemory = new FixedSizeQueue<>(0);
  }

  private void configureChatBox() {
    chatBox.setPrefHeight(400);
    chatBox.setPrefWidth(590);
    chatBox.setSpacing(5);
    chatBox.setPadding(new Insets(10, 10, 10, 10));
  }

  @Override
  public void chat(String input) {
    executorService.submit(() -> {
      doPreAddMemory(input);
      Platform.runLater(() -> chatBox.getChildren().add(getMessageHBox(input)));
      for (IRobot robot : robotList) {
        RobotGenerateResponse response = robot.generate(input, chatMemory);
        Platform.runLater(() -> chatBox.getChildren().add(response.getHBox()));
        Platform.runLater(() -> {
          if (!conversationLabel.getText().startsWith("（新）")) {
            this.conversationLabel.setText("（新）" + conversationLabel.getText());
          }
        });
        if (StringUtils.hasText(response.getReply())) {
          doPostAddMemory(response.getReply());
        }
      }
    });
  }

  protected abstract void doPreAddMemory(String input);

  protected abstract void doPostAddMemory(String reply);

  @Override
  public VBox getChatBox() {
    return chatBox;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public ConversationLabel getConversationLabel() {
    return conversationLabel;
  }

  @Override
  public void clear() {
    doClearChatMemory();
    chatBox.getChildren().clear();
  }

  protected abstract void doClearChatMemory();

  protected HBox getMessageHBox(String message) {
    HBox messageBox = new HBox();
    messageBox.setAlignment(Pos.BASELINE_RIGHT);
    messageBox.setSpacing(10);

    // 消息内容
    Label label = new Label(message);
    label.setWrapText(true);
    label.setStyle("-fx-background-color: rgba(255, 255, 255, 0.4); -fx-padding: 10; -fx-background-radius: 10;");

    // 创建右键菜单
    ContextMenu contextMenu = new ContextMenu();

    // 创建复制菜单项
    MenuItem copyItem = new MenuItem("复制");

    // 设置复制功能
    copyItem.setOnAction(event -> {
      Clipboard clipboard = Clipboard.getSystemClipboard();
      ClipboardContent content = new ClipboardContent();
      content.putString(message); // 将消息内容放入剪切板
      clipboard.setContent(content);
    });

    // 将复制菜单项添加到右键菜单
    contextMenu.getItems().add(copyItem);

    // 为label设置右键菜单
    label.setContextMenu(contextMenu);
    messageBox.getChildren().add(label);
    return messageBox;
  }
}
