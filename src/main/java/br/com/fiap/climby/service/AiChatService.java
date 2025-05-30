package br.com.fiap.climby.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiChatService {

    private final ChatClient chatClient;

    public AiChatService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String getAnswer(String question) {
        String promptMessage = """
                Você é um assistente virtual da plataforma Climby, especializada em ajudar pessoas
                durante eventos climáticos extremos. Responda a seguinte pergunta de forma concisa:
                Pergunta: %s
                Resposta:
                """.formatted(question);
        try {
            return chatClient.prompt()
                    .user(promptMessage)
                    .call()
                    .content();
        } catch (Exception e) {
            System.err.println("Erro ao chamar o serviço de IA: " + e.getMessage());
            return "Desculpe, não consegui processar sua pergunta no momento.";
        }
    }
}