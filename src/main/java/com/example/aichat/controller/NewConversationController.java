package com.example.aichat.controller;

import com.example.aichat.context.Context;
import com.example.aichat.conversation.Robot;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

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

  private final List<Robot> robotList = new ArrayList<>();

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
  }

  @FXML
  private void handleConfirm() {
    // 获取主题
    String subject = subjectTextField.getText();

    // 获取 Slider 值
    int chatMemorySize = (int) chatMemorySlider.getValue();

    // 获取机器人列表
    robotList.clear();
    for (int i = 1; i < robotListVBox.getChildren().size(); i++) {
      HBox robotBox = (HBox) robotListVBox.getChildren().get(i);
      TextField nameField = (TextField) robotBox.getChildren().get(0);
      TextField systemPromptField = (TextField) robotBox.getChildren().get(1);
      TextField orderField = (TextField) robotBox.getChildren().get(2);

      String name = nameField.getText();
      String systemPrompt = systemPromptField.getText();
      int order = Integer.parseInt(orderField.getText());

      Robot robot = new Robot(name, systemPrompt, order);
      robotList.add(robot);

      robotScrollPane.layout();
      robotScrollPane.setVvalue(1.0);
    }

    Context.addNewConversation(chatMemorySize, robotList, subject);
  }
}
