package com.example.aichat.util;

import javafx.scene.control.TextArea;

public class CommonUtil {
  public static int calculateRowCount(TextArea textArea, String text) {
    int rowCount = 1;
    int wrapWidth = (int) textArea.getPrefWidth();
    int textWidth = (int) calculateTextWidth(textArea, text);

    if (textWidth > wrapWidth) {
      rowCount = (int) Math.ceil((double) textWidth / wrapWidth);
    }

    return rowCount;
  }

  private static double calculateTextWidth(TextArea textArea, String text) {
    // 这里可以使用 Text 组件来计算文本的宽度
    javafx.scene.text.Text tempText = new javafx.scene.text.Text(text);
    tempText.setFont(textArea.getFont());
    return tempText.getLayoutBounds().getWidth();
  }
}
