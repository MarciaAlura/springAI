package br.com.alura.ecomart.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptionsBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("categorizador")
public class CategorizadorController {

    private final ChatClient chatClient;

    public CategorizadorController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping
    public String categorizar(String produto) {
        var system = """
                Você é um categorizador de produtos e deve responder apenas o nome da categoria do produto informado
                
                Escolha uma categoria dentro da lista abaixo:
                1. Higiene pessoal
                2. Eletrônicos
                3. Esportes
                4. Outros
                
                ###### exemplo de uso:
                
                Pergunta: Bola de futebol
                Resposta: Esportes
                """;

        return this.chatClient.prompt()
                .system(system)
                .user(produto)
                .options(ChatOptionsBuilder.builder()
                        .withTemperature(Double.valueOf(0.85f))
                        .build())
                .call()
                .content();
    }
}
