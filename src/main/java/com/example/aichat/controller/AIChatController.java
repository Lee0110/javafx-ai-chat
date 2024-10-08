package com.example.aichat.controller;

import com.example.aichat.component.ConversationLabel;
import com.example.aichat.context.Context;
import com.example.aichat.conversation.ChatConversation;
import com.example.aichat.conversation.ChatRobot;
import com.example.aichat.conversation.ImageConversation;
import com.example.aichat.conversation.ImageRobot;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class AIChatController implements Initializable {

  private static final Logger log = LoggerFactory.getLogger(AIChatController.class);

  @FXML
  private JFXButton sendButton;

  @FXML
  private JFXListView<ConversationLabel> conversationJFXListView;

  @FXML
  private JFXTextArea inputField;

  @FXML
  private ScrollPane scrollPane;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // 设置输入框的回车事件，按下shift+回车发送消息
    inputField.setOnKeyPressed(event -> {
      if (event.isShiftDown() && event.getCode().getName().equals("Enter") && !sendButton.isDisable()) {
        handleSend();
      }
    });

    // 初始化对话列表
    Context.init(conversationJFXListView, scrollPane);

    // 官方预置的对话
    ChatRobot chatRobot1 = new ChatRobot("java助手-派蒙", "你是一名十年经验java工程师。你擅长给用户解决各种编程相关的问题。你解决问题的风格通常是先给出解决的思路，再由用户发出继续的命令后，然后一步一步解决问题。同时你的说话风格是一名二次元美少女，喜欢用颜文字来表达自己的情绪，你要扮演游戏原神里的派蒙，以派蒙这个名字来回答问题。", 1);
    ChatConversation chatConversation = new ChatConversation(20, List.of(chatRobot1), "java问题解答");
    Context.putConversation(chatConversation.getId(), chatConversation);
    Context.setHoldConversation(chatConversation);

    ChatRobot chatRobot3 = new ChatRobot("铃音-决策者", "你的名字叫铃音，你是一名二次元御姐。你的风格是严肃、敏锐、官方。你担任的角色是决策者，对于用户的问题，你不会直接给出答案，而是用非常简洁的语言给出一个解决问题的大纲或者解决问题的步骤或者解决思路即可。", 1);
    ChatRobot chatRobot4 = new ChatRobot("雪乃-执行者", "你的名字叫雪乃，你是一名二次元美少女。你的风格是活泼、可爱、俏皮。你担任的角色是执行者。用户提问，然后第一个发言的人是决策者，你需要综合之前用户提问和决策者提出的方案，来进行具体的执行。", 2);
    ChatConversation chatConversation2 = new ChatConversation(20, Arrays.asList(chatRobot3, chatRobot4), "脑洞大开会议室");
    Context.putConversation(chatConversation2.getId(), chatConversation2);

    ImageRobot imageRobot = new ImageRobot();
    ImageConversation imageConversation = new ImageConversation("文生图", List.of(imageRobot));
    Context.putConversation(imageConversation.getId(), imageConversation);

    conversationJFXListView.getItems().addAll(chatConversation.getConversationLabel(), chatConversation2.getConversationLabel(), imageConversation.getConversationLabel());
    conversationJFXListView.getSelectionModel().select(0);
    handleSelectConversation();
  }

  @FXML
  private void handleSend() {
    String inputText = inputField.getText();
    inputField.clear();
    if (!inputText.isEmpty()) {
      Context.getHoldConversation().chat(inputText);
    }
  }

  @FXML
  public void handleSelectConversation() {
    ConversationLabel label = conversationJFXListView.getSelectionModel().getSelectedItem();
    if (label != null) {
      label.setText(label.getText().replaceFirst("（新）", ""));
      Context.setHoldConversation(label.getConversationId());
    }
  }

  @FXML
  public void handleClearConversation() {
    Context.clear();
  }

  @FXML
  public void handleAddConversation() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/aichat/newConversation.fxml"));
      Parent root = loader.load();
      Stage stage = new Stage();
      stage.setTitle("Add New Conversation");
      stage.setScene(new Scene(root));
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.showAndWait();
    } catch (IOException e) {
      log.error("Failed to open new conversation dialog", e);
    }
  }
}
