package com.example.aichat.conversation;

import javafx.scene.layout.HBox;

public class RobotGenerateResponse {
  private final String reply;

  private final HBox hBox;

  public RobotGenerateResponse(String reply, HBox hBox) {
    this.reply = reply;
    this.hBox = hBox;
  }

  public RobotGenerateResponse(HBox messageBox) {
    this.reply = null;
    this.hBox = messageBox;
  }

  public String getReply() {
    return reply;
  }

  public HBox getHBox() {
    return hBox;
  }
}
