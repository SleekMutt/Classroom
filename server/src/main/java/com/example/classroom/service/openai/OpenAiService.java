package com.example.classroom.service.openai;

import org.springframework.ai.chat.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class OpenAiService {

  private final ChatClient chatClient;

  public OpenAiService(ChatClient chatClient) {
    this.chatClient = chatClient;
  }
  public String processPrompt(String prompt) {
    return chatClient.call(prompt);
  }
}