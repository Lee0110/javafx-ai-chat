package com.example.aichat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AIChatClientApplication extends Application {

  @Override
  public void init() {
    SpringApplication.run(AIChatClientApplication.class);
  }

  @Override
  public void stop() throws Exception {
    super.stop();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    // 从Spring上下文中获取你的控制器或其他Bean
    FXMLLoader loader = new FXMLLoader(AIChatClientApplication.class.getResource("ai-chat-client.fxml"));
    Scene scene = new Scene(loader.load());

    primaryStage.setTitle("Chat Client");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
