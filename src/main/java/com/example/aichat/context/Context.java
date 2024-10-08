package com.example.aichat.context;

import com.example.aichat.component.ConversationLabel;
import com.example.aichat.conversation.ChatConversation;
import com.example.aichat.conversation.IConversation;
import com.example.aichat.conversation.ChatRobot;
import com.example.aichat.conversation.IRobot;
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
  private static final Map<String, IConversation> conversationMap = new ConcurrentHashMap<>();

  /**
   * 当前会话
   */
  private static IConversation holdChatConversation;

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

  public static void addNewConversation(IConversation conversation) {
    conversationMap.put(conversation.getId(), conversation);
    conversationJFXListView.getItems().addAll(conversation.getConversationLabel());
    conversationJFXListView.getSelectionModel().select(conversationJFXListView.getItems().size() - 1);
    setHoldConversation(conversation);
  }

  public static void addNewConversation(int chatMemorySize, List<IRobot> robotList, String subject) {
    addNewConversation(new ChatConversation(chatMemorySize, robotList, subject));
  }

  public static void setHoldConversation(IConversation conversation) {
    holdChatConversation = conversation;
    Platform.runLater(() -> scrollPane.setContent(holdChatConversation.getChatBox()));
  }

  public static IConversation getHoldConversation() {
    return holdChatConversation;
  }

  public static void putConversation(String id, IConversation conversation) {
    conversationMap.put(id, conversation);
  }

  public static void setHoldConversation(String conversationId) {
    if (conversationMap.containsKey(conversationId)) {
      setHoldConversation(conversationMap.get(conversationId));
    } else {
      throw new RuntimeException("conversationId not found");
    }
  }

  public static void clear() {
    holdChatConversation.clear();
  }
}
