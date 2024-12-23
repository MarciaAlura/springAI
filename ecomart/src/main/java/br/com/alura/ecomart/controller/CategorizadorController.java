package br.com.alura.ecomart.controller;


import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import com.knuddels.jtokkit.api.ModelType;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
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

        var tokens = contarTokens(system, produto);
        System.out.println("QTD de tokens: " + tokens);

        return this.chatClient.prompt()
                .system(system)
                .user(produto)
                .options(ChatOptionsBuilder.builder()
                        .withTemperature(Double.valueOf(0.85f))
                        .withModel("gpt-4o-mini")
                        .build())
                .advisors(new SimpleLoggerAdvisor())
                .call()
                .content();
    }

    private int contarTokens(String system, String user) {
        var registry = Encodings.newDefaultEncodingRegistry();
        var enc = registry.getEncodingForModel(ModelType.GPT_4O_MINI);
        return enc.countTokens(system + user);
    }
}
