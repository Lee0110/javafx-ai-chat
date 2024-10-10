package com.example.aichat.controller;

import com.example.aichat.context.Context;
import com.example.aichat.conversation.ChatRobot;
import com.example.aichat.conversation.IRobot;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NewConversationController {

  @FXML
  public ScrollPane robotScrollPane;

  @FXML
  private ChoiceBox<String> typeChoiceBox;

  @FXML
  private TextField subjectTextField;

  @FXML
  private Slider chatMemorySlider;

  @FXML
  private Label sliderValueLabel;

  @FXML
  private VBox robotListVBox;

  private int order = 0;

  @FXML
  public void initialize() {
    // 初始化类型选择框
    typeChoiceBox.getItems().addAll("聊天", "文生图");
    typeChoiceBox.setValue("聊天");

    // 更新 Slider 值
    chatMemorySlider.valueProperty().addListener((observable, oldValue, newValue) -> sliderValueLabel.setText(String.valueOf(newValue.intValue())));
  }

  @FXML
  private void handleAddRobot() {
    // 创建新的机器人输入表单
    Label orderTextLabel = new Label("发言顺序：");
    Label orderLabel = new Label(++order + "");
    HBox orderHBox = new HBox(5, orderTextLabel, orderLabel);
    Label nameLabel = new Label("名称");
    TextField nameField = new TextField();
    nameField.setPromptText("名称");
    Label systemPromptLabel = new Label("系统提示词");
    TextArea systemPromptArea = new TextArea();
    systemPromptArea.setPromptText("系统提示词");
    systemPromptArea.setPrefRowCount(3);
    Separator separator = new Separator();

    VBox robotBox = new VBox(5, orderHBox, nameLabel, nameField, systemPromptLabel, systemPromptArea, separator);
    robotListVBox.getChildren().add(robotBox);

    robotScrollPane.layout();
    robotScrollPane.setVvalue(1.0);
  }

  @FXML
  private void handleConfirm() {
    // 获取主题
    String subject = subjectTextField.getText();

    // 获取 Slider 值
    int chatMemorySize = (int) chatMemorySlider.getValue();

    // 获取机器人列表
    List<IRobot> chatRobotList = robotListVBox.getChildren().stream().map(node -> {
      VBox robotBox = (VBox) node;
      HBox orderHbox = (HBox) robotBox.getChildren().get(0);
      Label orderLabel = (Label) orderHbox.getChildren().get(1);
      TextField nameField = (TextField) robotBox.getChildren().get(2);
      TextArea systemPromptTextArea = (TextArea) robotBox.getChildren().get(4);
      String name = nameField.getText();
      String systemPrompt = systemPromptTextArea.getText();
      int order = Integer.parseInt(orderLabel.getText());
      return new ChatRobot(name, systemPrompt, order);
    }).sorted(Comparator.comparingInt(IRobot::getOrder)).collect(Collectors.toList());

    Context.addNewConversation(chatMemorySize, chatRobotList, subject);
    robotScrollPane.getScene().getWindow().hide();
  }

}
