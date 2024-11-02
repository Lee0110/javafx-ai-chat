package com.example.aichat.conversation;

import javafx.scene.Node;

public class RobotGenerateResponse {
  private final String reply;

  private final Node node;

  public RobotGenerateResponse(String reply, Node node) {
    this.reply = reply;
    this.node = node;
  }

  public RobotGenerateResponse(Node node) {
    this.reply = null;
    this.node = node;
  }

  public String getReply() {
    return reply;
  }

  public Node getNode() {
    return node;
  }
}
