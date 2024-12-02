package com.example.aichat.util;

import javafx.scene.text.Text;

public class CommonUtil {
  public static double calculateHeight(String text, double max) {
    Text tempText = new Text(text);
    tempText.setWrappingWidth(500);
    return Math.min(tempText.getLayoutBounds().getHeight() + 20, max);
  }
}
