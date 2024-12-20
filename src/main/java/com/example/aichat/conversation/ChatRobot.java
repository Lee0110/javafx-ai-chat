package com.example.aichat.conversation;

import com.example.aichat.util.AIUtil;
import com.example.aichat.util.CommonUtil;
import com.example.aichat.util.FixedSizeQueue;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import reactor.core.publisher.Flux;

import java.util.concurrent.CompletableFuture;

public class ChatRobot implements IRobot {

  private static final Logger log = LoggerFactory.getLogger(ChatRobot.class);

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
    Flux<ChatResponse> fluxReply = AIUtil.streamChat(getSystemPrompt(), chatMemory);
    HBox messageBox = new HBox();
    messageBox.setAlignment(Pos.BASELINE_LEFT);
    messageBox.setSpacing(10);

    // 消息内容
    TextArea textArea = new TextArea();
    textArea.setWrapText(true);
    textArea.setEditable(false); // 设置为不可编辑
    textArea.setStyle("-fx-background-color: rgba(190, 250, 250, 0.6); -fx-padding: 10; -fx-background-radius: 10;");

    // 设置 TextArea 的宽度和高度
    textArea.setPrefWidth(500);
    textArea.setPrefHeight(10);

    // 订阅 Flux 并更新 TextArea
    fluxReply.subscribe(
        reply -> {
          Platform.runLater(() -> textArea.appendText(reply.getResult().getOutput().getContent()));
          textArea.setPrefHeight(CommonUtil.calculateHeight(textArea.getText(), 500));
        },
        error -> {
          log.error("Error occurred", error);
          Platform.runLater(() -> textArea.appendText("糟糕，对话出错了！"));
        },
        () -> {
          chatMemory.add(new AssistantMessage(textArea.getText()));
          log.info("对话生成结果：{}", textArea.getText());
        }
    );

    messageBox.getChildren().add(textArea);
    return new RobotGenerateResponse(textArea.getText(), messageBox);
  }
}
