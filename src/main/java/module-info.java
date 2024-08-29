module com.example.aichat {
  requires javafx.controls;
  requires javafx.fxml;

  requires org.kordamp.bootstrapfx.core;

  opens com.example.aichat to javafx.fxml;
  exports com.example.aichat;
}