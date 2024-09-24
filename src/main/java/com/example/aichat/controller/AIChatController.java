package com.example.aichat.controller;

import com.example.aichat.conversation.Conversation;
import com.example.aichat.conversation.Robot;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
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
    public JFXListView<Label> conversationJFXListView;

    @FXML
    private JFXTextArea inputField;

    @FXML
    private VBox chatBox;

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
        Robot robot1 = new Robot("AI助手", "你是一个智能助手，帮助人们解决各种问题。", 0);
        Robot robot2 = new Robot("AI助手2", "你是一个智能助手，帮助人们解决各种问题。", 1);
        Conversation conversation = new Conversation(20, Arrays.asList(robot1, robot2));
        conversationMap.put("会话1", conversation);
        holdConversation = conversation;

        Robot robot3 = new Robot("AI助手3", "你是一个智能助手，帮助人们解决各种问题。", 0);
        Robot robot4 = new Robot("AI助手4", "你是一个智能助手，帮助人们解决各种问题。", 1);
        Conversation conversation2 = new Conversation(20, Arrays.asList(robot3, robot4));
        conversationMap.put("会话2", conversation2);

        Label label1 = new Label("会话1");

        Label label2 = new Label("会话2");

        conversationJFXListView.getItems().addAll(label1, label2);
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
    public void handleFlush() {
        List<HBox> chatBoxList = holdConversation.getChatBoxList();
        Platform.runLater(() -> {
            for (HBox hBox : chatBoxList) {
                if (!chatBox.getChildren().contains(hBox)) {
                    chatBox.getChildren().add(hBox);
                }
            }
            // 滚动到最底部
            scrollPane.layout();
            scrollPane.setVvalue(1.0);
        });
    }

    @FXML
    public void handleConversationChange() {
        Label label = conversationJFXListView.getSelectionModel().getSelectedItem();
        if (label != null) {
            holdConversation = conversationMap.get(label.getText());
            chatBox.getChildren().clear();
            handleFlush();
        }
    }
}
