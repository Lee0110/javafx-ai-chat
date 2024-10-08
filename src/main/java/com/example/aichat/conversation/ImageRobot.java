package com.example.aichat.conversation;

import com.example.aichat.util.AIUtil;
import com.example.aichat.util.FixedSizeQueue;
import com.example.aichat.util.ImageDownloader;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.springframework.ai.chat.messages.Message;

public class ImageRobot implements IRobot {

  @Override
  public RobotGenerateResponse generate(String input, FixedSizeQueue<Message> chatMemory) {

    String imageUrl = AIUtil.image(input);
    ImageDownloader.downloadImage(imageUrl);

    HBox messageBox = new HBox();
    messageBox.setAlignment(Pos.BASELINE_LEFT);
    messageBox.setSpacing(10);

    // 创建Image对象
    Image image = new Image(imageUrl);

    // 创建ImageView对象
    ImageView imageView = new ImageView(image);

    // 设置ImageView的宽度和高度
    imageView.setFitWidth(256);
    imageView.setFitHeight(256);

    // 保持宽高比
    imageView.setPreserveRatio(true);
    messageBox.getChildren().add(imageView);

    return new RobotGenerateResponse(messageBox);
  }
}
