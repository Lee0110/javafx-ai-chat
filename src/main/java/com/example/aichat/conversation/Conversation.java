package com.example.aichat.conversation;

import com.example.aichat.util.ChatUtil;
import com.example.aichat.util.FixedSizeQueue;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Conversation {
  /**
   * 聊天记录
   */
  private final FixedSizeQueue<Message> chatMemory;

  /**
   * 机器人列表
   */
  private final List<Robot> robotList;

  /**
   * 聊天框列表
   */
  private final List<HBox> chatBoxList;

  public Conversation(int chatMemorySize, List<Robot> robotList) {
    this.chatMemory = new FixedSizeQueue<>(chatMemorySize);
    this.robotList = robotList;
    this.chatBoxList = Collections.synchronizedList(new ArrayList<>());
  }

  public void chat(String input) {
    chatMemory.add(new UserMessage(input));
    chatBoxList.add(getMessageHBox(input, Pos.BASELINE_RIGHT));
    String lastReply = input;
    for (Robot robot : robotList) {
      String reply = ChatUtil.chat(lastReply, robot.getSystemPrompt(), chatMemory.toList());
      chatMemory.add(new UserMessage(reply));
      chatBoxList.add(getMessageHBox(reply, Pos.BASELINE_LEFT));
      lastReply = reply;
    }
  }

  private HBox getMessageHBox(String message, Pos alignment) {
    HBox messageBox = new HBox();
    messageBox.setAlignment(alignment);
    messageBox.setSpacing(10);

    // 头像
    ImageView avatar = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/default_avatar.png"))));
    avatar.setFitHeight(40);
    avatar.setFitWidth(40);

    // 消息内容
    Label messageLabel = new Label(message);
    messageLabel.setWrapText(true);
    if (alignment == Pos.BASELINE_RIGHT) {
      messageLabel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.4); -fx-padding: 10; -fx-background-radius: 10;");
    } else {
      messageLabel.setStyle("-fx-background-color: rgba(190, 250, 250, 0.6); -fx-padding: 10; -fx-background-radius: 10;");
    }

    // 添加右键菜单
    ContextMenu contextMenu = new ContextMenu();
    MenuItem copyItem = new MenuItem("复制");
    copyItem.setOnAction(event -> {
      Clipboard clipboard = Clipboard.getSystemClipboard();
      ClipboardContent content = new ClipboardContent();
      content.putString(messageLabel.getText());
      clipboard.setContent(content);
    });
    contextMenu.getItems().add(copyItem);
    messageLabel.setOnContextMenuRequested(event -> contextMenu.show(messageLabel, event.getScreenX(), event.getScreenY()));
    return messageBox;
  }

  public void clearChatMemory() {
    chatMemory.clear();
  }
}
