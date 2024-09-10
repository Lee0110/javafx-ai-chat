package com.example.aichat;

import com.example.aichat.controller.AIChatController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class AIChatClientApplication extends Application {

    private ApplicationContext context;

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
        FXMLLoader loader = new FXMLLoader(AIChatClientApplication.class.getResource("ai-chat-client.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        primaryStage.setTitle("Chat Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
