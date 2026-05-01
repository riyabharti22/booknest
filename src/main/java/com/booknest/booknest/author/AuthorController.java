package com.booknest.booknest.author;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorRepository authorRepository;
    private final WebClient.Builder webClientBuilder;

    @GetMapping("/author/{id}")
    public String getAuthor(@PathVariable String id, Model model) {

        WebClient client = webClientBuilder.build();

        // Fetch from OpenLibrary API
        Map data = client.get()
                .uri("https://openlibrary.org/authors/" + id + ".json")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (data != null) {
            Author author = new Author();

            author.setId(id);
            author.setName((String) data.get("name"));
            author.setPersonalName((String) data.get("personal_name"));

            Object bioObj = data.get("bio");

            if (bioObj instanceof Map) {
                author.setBio((String) ((Map) bioObj).get("value"));
            } else if (bioObj instanceof String) {
                author.setBio((String) bioObj);
            }

            authorRepository.save(author);

            model.addAttribute("author", author);
        }

        return "author";
    }
}