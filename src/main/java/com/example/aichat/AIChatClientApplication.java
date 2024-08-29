package com.example.aichat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AIChatClientApplication extends Application {

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("ai-chat-client.fxml"));
    Scene scene = new Scene(loader.load());

    primaryStage.setTitle("Chat Client");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
