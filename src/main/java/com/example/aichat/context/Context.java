package com.example.aichat.context;

import com.example.aichat.component.ConversationLabel;
import com.example.aichat.conversation.Conversation;
import com.example.aichat.conversation.Robot;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.scene.control.ScrollPane;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Context {
  public static JFXListView<ConversationLabel> conversationJFXListView;

  /**
   * key: 会话id value: 会话
   */
  private static final Map<String, Conversation> conversationMap = new ConcurrentHashMap<>();

  /**
   * 当前会话
   */
  private static Conversation holdConversation;

  private static ScrollPane scrollPane;

  public static void init(JFXListView<ConversationLabel> conversationJFXListView, ScrollPane scrollPane) {
    if (Context.conversationJFXListView == null) {
      Context.conversationJFXListView = conversationJFXListView;
    } else {
      throw new RuntimeException("conversationJFXListView has been initialized");
    }
    if (Context.scrollPane == null) {
      Context.scrollPane = scrollPane;
    } else {
      throw new RuntimeException("scrollPane has been initialized");
    }
  }

  public static void addNewConversation(Conversation conversation) {
    conversationMap.put(conversation.getId(), conversation);
    conversationJFXListView.getItems().addAll(conversation.getConversationLabel());
    conversationJFXListView.getSelectionModel().select(conversationJFXListView.getItems().size() - 1);
    setHoldConversation(conversation);
  }

  public static void addNewConversation(int chatMemorySize, List<Robot> robotList, String subject) {
    addNewConversation(new Conversation(chatMemorySize, robotList, subject));
  }

  public static void setHoldConversation(Conversation conversation) {
    holdConversation = conversation;
    Platform.runLater(() -> scrollPane.setContent(holdConversation.getChatBox()));
  }

  public static Conversation getHoldConversation() {
    return holdConversation;
  }

  public static void putConversation(String id, Conversation conversation) {
    conversationMap.put(id, conversation);
  }

  public static void setHoldConversation(String conversationId) {
    if (conversationMap.containsKey(conversationId)) {
      setHoldConversation(conversationMap.get(conversationId));
    } else {
      throw new RuntimeException("conversationId not found");
    }
  }

  public static void clearCurrentChatMemory() {
    holdConversation.clearChatMemory();
  }
}
