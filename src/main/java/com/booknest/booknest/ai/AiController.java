package com.booknest.booknest.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AiController {

    @Value("${groq.api.key}")
    private String groqApiKey;

    private final WebClient groqClient = WebClient.builder()
            .baseUrl("https://api.groq.com")
            .build();

    @GetMapping("/ai-recommend")
    public String aiRecommendPage() {
        return "ai-recommend";
    }

    @PostMapping("/ai-recommend")
    public String getRecommendations(@RequestParam String mood,
                                     Model model) {
        model.addAttribute("mood", mood);
        try {
            Map<String, Object> requestBody = Map.of(
                "model", "llama3-8b-8192",
                "messages", List.of(
                    Map.of(
                        "role", "system",
                        "content", "You are a helpful book recommendation assistant. When a user describes their mood or interest, suggest 5 specific books with their author names and a one line reason why. Format each book as: 📖 Book Title by Author — reason"
                    ),
                    Map.of(
                        "role", "user",
                        "content", mood
                    )
                )
            );

            Map<?, ?> response = groqClient.post()
                    .uri("/openai/v1/chat/completions")
                    .header("Authorization", "Bearer " + groqApiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            List<Map<?, ?>> choices = (List<Map<?, ?>>) response.get("choices");
            Map<?, ?> message = (Map<?, ?>) choices.get(0).get("message");
            String recommendation = (String) message.get("content");

            model.addAttribute("recommendations", recommendation);

        } catch (Exception e) {
            System.out.println("❌ GROQ ERROR: " + e.getMessage());
            model.addAttribute("recommendations", "Sorry, could not get recommendations. Try again!");
        }

        return "ai-recommend";
    }
}