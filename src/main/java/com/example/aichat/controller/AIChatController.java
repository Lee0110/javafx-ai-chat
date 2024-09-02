package com.example.aichat.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

@Component
@Slf4j
public class AIChatController implements Initializable {
    @FXML
    private TextArea inputField;

    @FXML
    private VBox chatBox;

    @FXML
    private ScrollPane scrollPane;

    private final ChatClient chatClient;

    public AIChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 设置背景图片
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/img/background.jpg")).toExternalForm());
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        scrollPane.setBackground(new Background(background));
    }

    private String chat(String input) {
        long start = System.currentTimeMillis();
        log.info("开始对话，用户输入：{}", input);
        String reply;
        try {
            reply = chatClient.prompt().user(input).call().content();
            log.info("对话生成完毕，耗时：{}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("对话出错", e);
            reply = "对话出错，请稍后再试";
        }
        return reply;
    }

    @FXML
    private void handleSend() {
        String inputText = inputField.getText();
        if (!inputText.isEmpty()) {
            // 显示用户输入的消息
            addMessage(inputText, Pos.BASELINE_RIGHT);

            // 获取回复消息并显示
            String reply = chat(inputText);
            addMessage(reply, Pos.BASELINE_LEFT);

            // 清空输入框
            inputField.clear();
        }
    }

    @FXML
    private void handleClearChat() {
        chatBox.getChildren().clear();
    }

    @FXML
    private void handleExportChat() {
        // todo
    }

    @FXML
    private void handleOpenSettings() {
        Stage settingsStage = new Stage();
        settingsStage.setTitle("设置");
        // 可以在这里添加更多的UI元素到settingsStage
        settingsStage.setScene(new Scene(new Pane(), 400, 300));
        settingsStage.show();
    }

    @FXML
    private void handleOpenAbout() {
        Stage aboutStage = new Stage();
        aboutStage.setTitle("关于");
        // 可以在这里添加更多的UI元素到aboutStage
        aboutStage.setScene(new Scene(new Pane(), 400, 300));
        aboutStage.show();
    }

    private void addMessage(String message, Pos alignment) {
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
        messageLabel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5); -fx-padding: 10; -fx-background-radius: 10;");
        messageLabel.getStyleClass().add("alert alert-info");

        // 根据消息类型设置布局
        if (alignment == Pos.BASELINE_RIGHT) {
            messageBox.getChildren().addAll(messageLabel, avatar);
        } else {
            messageBox.getChildren().addAll(avatar, messageLabel);
        }

        chatBox.getChildren().add(messageBox);
        // 滚动到最底部
        scrollPane.layout();
        scrollPane.setVvalue(1.0);
    }
}
