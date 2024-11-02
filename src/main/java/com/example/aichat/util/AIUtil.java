package com.example.aichat.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Component
public class AIUtil implements BeanFactoryAware {
  private static final Logger log = LoggerFactory.getLogger(AIUtil.class);

  private static ChatClient chatClient;

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    chatClient = beanFactory.getBean(ChatClient.class);
  }

  public static String chat(String systemPrompt, List<Message> messages) {
    long start = System.currentTimeMillis();
    String reply;
    try {
      ChatResponse chatResponse = chatClient.prompt().user(messages.removeLast().getContent()).system(systemPrompt).messages(messages).call().chatResponse();
      log.info("对话结束，详细信息：{}", chatResponse);
      BigDecimal time = new BigDecimal(System.currentTimeMillis() - start).divide(new BigDecimal(1000), 1, RoundingMode.HALF_UP);
      reply = chatResponse.getResult().getOutput().getContent() + "\n" + "生成耗时：" + time + "秒";
      log.info("对话生成完毕，耗时：{}秒", time);
    } catch (Exception e) {
      log.error("对话出错", e);
      reply = "对话出错，请稍后再试";
    }
    return reply;
  }

  public static String mockChat(String systemPrompt, List<Message> messages) {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 100; i++) {
      sb.append("模拟对话：").append(UUID.randomUUID());
    }
    return "模拟对话：" + UUID.randomUUID() + "\n" + sb.toString();
  }

  public static String image(String msg) {
    throw new RuntimeException("Not implemented");
  }
}
