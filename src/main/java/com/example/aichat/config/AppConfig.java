package com.example.aichat.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan("com.example.aichat")
public class AppConfig {

    @Bean
    public ChatClient chatClient(@Qualifier("azureOpenAiChatModel") ChatModel chatModel) {
        return ChatClient.create(chatModel);
    }

//    @Bean
//    @Primary
//    public OpenAIClient openAIClient() {
//        if (StringUtils.hasText(Properties.apiKey)) {
//            Assert.hasText(Properties.endpoint, "Endpoint must not be empty");
//            return (new OpenAIClientBuilder()).endpoint(Properties.endpoint).credential(new AzureKeyCredential(Properties.apiKey)).clientOptions((new ClientOptions()).setApplicationId("spring-ai")).buildClient();
//        }  else {
//            throw new IllegalArgumentException("Either API key or OpenAI API key must not be empty");
//        }
//    }
//
//    @Bean
//    @Primary
//    public AzureOpenAiEmbeddingModel azureOpenAiEmbeddingModel(OpenAIClient openAIClient) {
//        return new AzureOpenAiEmbeddingModel(openAIClient, MetadataMode.EMBED, AzureOpenAiEmbeddingOptions.builder().withDeploymentName(Properties.embeddingDeploymentName).build());
//    }
//
//    @Bean
//    @Primary
//    public FunctionCallbackContext springAiFunctionManager(ApplicationContext context) {
//        FunctionCallbackContext manager = new FunctionCallbackContext();
//        manager.setApplicationContext(context);
//        return manager;
//    }
//
//    @Bean
//    @Primary
//    public AzureOpenAiImageModel azureOpenAiImageClient(OpenAIClient openAIClient) {
//        return new AzureOpenAiImageModel(openAIClient, AzureOpenAiImageOptions.builder().withDeploymentName(Properties.imageDeploymentName).build());
//    }
}
