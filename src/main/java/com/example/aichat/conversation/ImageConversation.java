package com.example.aichat.conversation;

import java.util.List;

public class ImageConversation extends BaseConversation{
  public ImageConversation(String subject, List<IRobot> robotList) {
    super(subject, robotList);
  }

  @Override
  protected void doPreAddMemory(String input) {

  }

  @Override
  protected void doPostAddMemory(String reply) {

  }

  @Override
  protected void doClearChatMemory() {

  }
}
