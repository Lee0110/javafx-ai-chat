package com.example.aichat.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageDownloader {

  private static final Logger log = LoggerFactory.getLogger(ImageDownloader.class);

  private static final String IMAGE_DOWNLOAD_FOLDER_PATH = "/Users/yqg/code/ai-chat/img";

  private static final ExecutorService executorService = Executors.newFixedThreadPool(1);

  public static void downloadImage(String imageUrl) {
    // 生成随机UUID作为文件名
    String fileName = UUID.randomUUID() + ".png";
    String filePath = Paths.get(IMAGE_DOWNLOAD_FOLDER_PATH, fileName).toString();
    executorService.submit(() -> {
      log.info("开始下载图片: {}", imageUrl);
      try (InputStream in = URI.create(imageUrl).toURL().openStream()) {
        Files.copy(in, Paths.get(filePath));
        log.info("图片下载成功: {}", filePath);
      } catch (IOException e) {
        log.error("下载图片失败", e);
      }
    });
  }
}
