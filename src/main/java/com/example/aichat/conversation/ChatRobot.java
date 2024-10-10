package com.example.aichat.conversation;

import com.example.aichat.util.AIUtil;
import com.example.aichat.util.FixedSizeQueue;
import com.jfoenix.controls.JFXTextArea;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import org.springframework.ai.chat.messages.Message;

public class ChatRobot implements IRobot {
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

  public ChatRobot(String name, String systemPrompt, int order) {
    this.name = name;
    this.systemPrompt = systemPrompt;
    this.order = order;
  }

  public String getSystemPrompt() {
    return "你现在正处在一个多人聊天会议室里。用户会首先抛出一个主题，然后其余每个人扮演自己的角色轮流发言。你是第" + order + "个发言的人。在发言之前，首先介绍自己，并且说明自己是第" + order + "个发言的人。" + systemPrompt;
  }

  public String getName() {
    return name;
  }

  @Override
  public int getOrder() {
    return order;
  }

  @Override
  public RobotGenerateResponse generate(String input, FixedSizeQueue<Message> chatMemory) {
    String reply = name + "：" + AIUtil.chat(getSystemPrompt(), chatMemory.toList());
    HBox messageBox = new HBox();
    messageBox.setAlignment(Pos.BASELINE_LEFT);
    messageBox.setSpacing(10);

    // 消息内容
    JFXTextArea messageTextArea = new JFXTextArea(reply);
    messageTextArea.setWrapText(true);
    messageTextArea.setEditable(false);
    messageTextArea.setPrefRowCount(reply.split("\n").length);
    messageTextArea.setStyle("-fx-background-color: rgba(190, 250, 250, 0.6); -fx-padding: 10; -fx-background-radius: 10;");

    messageTextArea.textProperty().addListener((observable, oldValue, newValue) -> messageTextArea.setPrefRowCount(newValue.split("\n").length));
    messageBox.getChildren().add(messageTextArea);
    return new RobotGenerateResponse(reply, messageBox);
  }
}
