package com.example.aichat.conversation;

import com.example.aichat.util.AIUtil;
import com.example.aichat.util.FixedSizeQueue;
import com.jfoenix.controls.JFXTextArea;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
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
    String reply = name + "：" + AIUtil.mockChat(getSystemPrompt(), chatMemory.toList());
    HBox messageBox = new HBox();
    messageBox.setAlignment(Pos.BASELINE_LEFT);
    messageBox.setSpacing(10);

    // 消息内容
    TextArea textArea = new TextArea(reply);
    textArea.setWrapText(true);
    textArea.setEditable(false); // 设置为不可编辑
    textArea.setStyle("-fx-background-color: rgba(190, 250, 250, 0.6); -fx-padding: 10; -fx-background-radius: 10;");

    // 设置 TextArea 的宽度和高度
    textArea.setPrefWidth(500);
    // 动态调整 TextArea 的高度
    textArea.setPrefRowCount(calculateRowCount(textArea, reply));

    messageBox.getChildren().add(textArea);
    return new RobotGenerateResponse(reply, messageBox);
  }

  private int calculateRowCount(TextArea textArea, String text) {
    int rowCount = 1;
    int wrapWidth = (int) textArea.getPrefWidth();
    int textWidth = (int) calculateTextWidth(textArea, text);

    if (textWidth > wrapWidth) {
      rowCount = (int) Math.ceil((double) textWidth / wrapWidth);
    }

    return rowCount;
  }

  private double calculateTextWidth(TextArea textArea, String text) {
    // 这里可以使用 Text 组件来计算文本的宽度
    javafx.scene.text.Text tempText = new javafx.scene.text.Text(text);
    tempText.setFont(textArea.getFont());
    return tempText.getLayoutBounds().getWidth();
  }
}
