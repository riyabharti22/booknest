package com.booknest.booknest.search;
 
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
 
import java.util.List;
import java.util.Map;
 
@Controller
@RequiredArgsConstructor
public class SearchController {
 
    private final WebClient.Builder webClientBuilder;
 
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String query, Model model) {
        if (query != null && !query.isBlank()) {
            WebClient client = webClientBuilder.build();
 
            try {
                Map<?, ?> result = client.get()
                        // FIX: encode the query properly and request only needed fields
                        .uri("https://openlibrary.org/search.json?q="
                                + query.replace(" ", "+")
                                + "&limit=20&fields=key,title,author_name,cover_i,first_publish_year")
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block();
 
                if (result != null) {
                    model.addAttribute("books", result.get("docs"));
                }
            } catch (Exception e) {
                // On error just show empty results — don't crash
                model.addAttribute("books", List.of());
            }
        }
        model.addAttribute("query", query);
        return "search";
    }
}
 