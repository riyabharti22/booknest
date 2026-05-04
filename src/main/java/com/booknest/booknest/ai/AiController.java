package com.booknest.booknest.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Controller
public class AiController {

    @Value("${groq.api.key}")
    private String groqApiKey;

    @GetMapping("/ai-recommend")
    public String aiRecommendPage() {
        return "ai-recommend";
    }

    @PostMapping("/ai-recommend")
    public String getRecommendations(@RequestParam String mood, Model model) {
        model.addAttribute("mood", mood);
        try {
            WebClient client = WebClient.builder()
                    .baseUrl("https://api.groq.com")
                    .build();

            Map<String, Object> requestBody = Map.of(
                "model", "llama3-8b-8192",
                "messages", List.of(
                    Map.of("role", "system",
                           "content", "You are a helpful book recommendation assistant. Suggest 5 books based on the user's mood. Format each as: 📖 Book Title by Author — one line reason"),
                    Map.of("role", "user",
                           "content", mood)
                )
            );

            Map<?, ?> response = client.post()
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

        } catch (WebClientResponseException e) {
            // FIX: Print full response body to see exact Groq error
            System.out.println("❌ GROQ STATUS: " + e.getStatusCode());
            System.out.println("❌ GROQ BODY: " + e.getResponseBodyAsString());
            model.addAttribute("recommendations", "Error: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.out.println("❌ GROQ ERROR: " + e.getMessage());
            model.addAttribute("recommendations", "Error: " + e.getMessage());
        }
        return "ai-recommend";
    }
}