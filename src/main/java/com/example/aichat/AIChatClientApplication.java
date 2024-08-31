package com.example.aichat;

import com.example.aichat.controller.AIChatController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class AIChatClientApplication extends Application {

    private ApplicationContext context;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void init() {
        context = SpringApplication.run(AIChatClientApplication.class);
    }

    @Override
    public void stop() throws Exception {
      super.stop();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 从Spring上下文中获取你的控制器或其他Bean
        AIChatController controller = context.getBean(AIChatController.class);
        FXMLLoader loader = new FXMLLoader(AIChatClientApplication.class.getResource("ai-chat-client.fxml"));
        loader.setController(controller);
        Scene scene = new Scene(loader.load());

        primaryStage.setTitle("Chat Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
