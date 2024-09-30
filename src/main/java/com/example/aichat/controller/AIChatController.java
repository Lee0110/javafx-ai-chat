package com.example.aichat.controller;

import com.example.aichat.component.ConversationLabel;
import com.example.aichat.conversation.Conversation;
import com.example.aichat.conversation.Robot;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AIChatController implements Initializable {

  private static final Logger log = LoggerFactory.getLogger(AIChatController.class);

  @FXML
  public JFXButton sendButton;

  @FXML
  public JFXListView<ConversationLabel> conversationJFXListView;

  @FXML
  private JFXTextArea inputField;

  @FXML
  private ScrollPane scrollPane;

  /**
   * key: 会话id value: 会话
   */
  private Map<String, Conversation> conversationMap;

  /**
   * 当前会话
   */
  private Conversation holdConversation;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // 设置输入框的回车事件，按下shift+回车发送消息
    inputField.setOnKeyPressed(event -> {
      if (event.isShiftDown() && event.getCode().getName().equals("Enter") && !sendButton.isDisable()) {
        handleSend();
      }
    });

    // 官方预置的对话
    conversationMap = new ConcurrentHashMap<>();
    Robot robot1 = new Robot("java助手-派蒙", "你是一名十年经验java工程师。你擅长给用户解决各种编程相关的问题。你解决问题的风格通常是先给出解决的思路，再由用户发出继续的命令后，然后一步一步解决问题。同时你的说话风格是一名二次元美少女，喜欢用颜文字来表达自己的情绪，你要扮演游戏原神里的派蒙，以派蒙这个名字来回答问题。", 0);
    Conversation conversation = new Conversation(20, List.of(robot1), "java问题解答");
    conversationMap.put(conversation.getId(), conversation);
    holdConversation = conversation;

    Robot robot3 = new Robot("铃音-决策者", "你的名字叫铃音，你是一名二次元御姐。你的风格是严肃、敏锐、官方。你现在正在一个聊天室中，聊天室里，用户会首先提问，然后其余人挨个解答。你是除用户外第一个发言的人，你担任的角色是决策者，对于用户的问题，你不会直接给出答案，而是用非常简洁的语言给出一个解决问题的大纲或者解决问题的步骤或者解决思路即可。你在发言的时候会首先介绍自己叫铃音，是第一个发言的人。", 0);
    Robot robot4 = new Robot("雪乃-执行者", "你的名字叫雪乃，你是一名二次元美少女。你的风格是活泼、可爱、俏皮。你现在正在一个聊天室中，聊天室里，用户会首先提问，然后其余人挨个解答。你是除用户外第二个发言的人，你担任的角色是执行者。用户提问，然后第一个发言的人是决策者，你需要综合之前用户提问和决策者提出的方案，来进行具体的执行。你在发言的时候会首先介绍自己叫雪乃，是第二个发言的人。", 1);
    Conversation conversation2 = new Conversation(20, Arrays.asList(robot3, robot4), "脑洞大开会议室");
    conversationMap.put(conversation2.getId(), conversation2);

    conversationJFXListView.getItems().addAll(conversation.getConversationLabel(), conversation2.getConversationLabel());
    conversationJFXListView.getSelectionModel().select(0);
    handleSelectConversation();
  }

  @FXML
  private void handleSend() {
    String inputText = inputField.getText();
    inputField.clear();
    if (!inputText.isEmpty()) {
      holdConversation.chat(inputText);
    }
  }

  @FXML
  public void handleSelectConversation() {
    ConversationLabel label = conversationJFXListView.getSelectionModel().getSelectedItem();
    if (label != null) {
      label.setText(label.getText().replaceFirst("（新）", ""));
      holdConversation = conversationMap.get(label.getConversationId());
      Platform.runLater(() -> scrollPane.setContent(holdConversation.getChatBox()));
    }
  }

  @FXML
  public void handleClearConversation() {
    holdConversation.clearChatMemory();
  }
}
