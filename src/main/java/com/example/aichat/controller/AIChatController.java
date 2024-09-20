package com.example.aichat.controller;

import com.example.aichat.util.ChatUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class AIChatController implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(AIChatController.class);

    @FXML
    public JFXButton sendButton;

    @FXML
    private JFXTextArea inputField;

    @FXML
    private VBox chatBox;

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
    }

    @FXML
    private void handleSend() {
        String inputText = inputField.getText();
        if (!inputText.isEmpty()) {
            // 显示用户输入的消息
            addMessage(inputText, Pos.BASELINE_RIGHT);
            // 获取回复消息并显示
            CompletableFuture
                    .supplyAsync(() -> ChatUtil.chat(inputText))
                    .thenAccept(reply -> Platform.runLater(() -> addMessage(reply, Pos.BASELINE_LEFT)));
        }
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

        Platform.runLater(() -> {
            // 根据消息类型设置布局
            if (alignment == Pos.BASELINE_RIGHT) {
                messageBox.getChildren().addAll(messageLabel, avatar);
                // 清空输入框
                inputField.clear();
                sendButton.setDisable(true);
            } else {
                messageBox.getChildren().addAll(avatar, messageLabel);
                sendButton.setDisable(false);
            }

            chatBox.getChildren().add(messageBox);
            // 滚动到最底部
            scrollPane.layout();
            scrollPane.setVvalue(1.0);
        });
    }
}
