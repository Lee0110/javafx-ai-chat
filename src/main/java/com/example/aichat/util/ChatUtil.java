package com.example.aichat.util;

import com.example.aichat.controller.AIChatController;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
public class ChatUtil implements BeanFactoryAware {
  private static final Logger log = LoggerFactory.getLogger(AIChatController.class);

  private static ChatClient chatClient;

  private static final AtomicInteger chatId = new AtomicInteger(0);

  private static ChatMemory chatMemory;

  public static final int DEFAULT_CHAT_MEMORY_RESPONSE_SIZE = 20;

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    chatClient = beanFactory.getBean(ChatClient.class);
    chatMemory = beanFactory.getBean(ChatMemory.class);
  }

  public static String chat(String input) {
    long start = System.currentTimeMillis();
    log.info("开始对话，对话id：{}，用户输入：{}", chatId, input);
    String reply;
    try {
      ChatResponse chatResponse = chatClient.prompt().user(input).advisors(a -> a
          .param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
          .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, DEFAULT_CHAT_MEMORY_RESPONSE_SIZE)).call().chatResponse();
      log.info("对话结束，详细信息：{}", chatResponse);
      BigDecimal time = new BigDecimal(System.currentTimeMillis() - start).divide(new BigDecimal(1000), 1, RoundingMode.HALF_UP);
      reply = chatResponse.getResult().getOutput().getContent() + "\n" + "耗时：" + time + "秒";
      log.info("对话生成完毕，耗时：{}秒", time);
    } catch (Exception e) {
      log.error("对话出错", e);
      reply = "对话出错，请稍后再试";
    }
    return reply;
  }

  public static void clearChatMemory() {
    chatMemory.clear(String.valueOf(chatId.getAndIncrement()));
  }
}
