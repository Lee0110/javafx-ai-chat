package com.example.aichat.conversation;

import com.example.aichat.component.ConversationLabel;
import javafx.scene.layout.VBox;

public interface IConversation {
  VBox getChatBox();

  void chat(String input);

  String getId();

  ConversationLabel getConversationLabel();

  void clear();
}
