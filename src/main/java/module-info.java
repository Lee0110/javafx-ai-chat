module com.example.aichat {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;
    requires spring.context;
    requires spring.ai.core;
    requires spring.beans;
    requires spring.ai.azure.openai;
    requires spring.ai.spring.boot.autoconfigure;
    requires com.azure.ai.openai;
    requires spring.core;
    requires spring.boot.autoconfigure;
    requires spring.boot;

    // 向 javafx.fxml spring 模块开放
    opens com.example.aichat to javafx.fxml, spring.core, spring.beans, spring.context;
    opens com.example.aichat.controller to javafx.fxml, spring.core, spring.beans, spring.context;
    opens com.example.aichat.config to javafx.fxml, spring.core, spring.beans, spring.context;

    // 导出包
    exports com.example.aichat;
    exports com.example.aichat.controller;
    exports com.example.aichat.config;
}