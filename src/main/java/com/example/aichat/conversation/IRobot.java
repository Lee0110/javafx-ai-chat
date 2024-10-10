package com.example.aichat.conversation;

import com.example.aichat.util.FixedSizeQueue;
import org.springframework.ai.chat.messages.Message;

public interface IRobot {
  RobotGenerateResponse generate(String input, FixedSizeQueue<Message> chatMemory);

  int getOrder();
}
