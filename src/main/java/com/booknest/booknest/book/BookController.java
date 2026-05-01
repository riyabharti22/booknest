package com.booknest.booknest.book;

import com.booknest.booknest.author.AuthorRepository;
import com.booknest.booknest.userbooks.UserBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final UserBookRepository userBookRepository;
    private final WebClient.Builder webClientBuilder;

    @GetMapping("/book/{bookId}")
    public String getBook(@PathVariable String bookId,
                          Model model,
                          @AuthenticationPrincipal OAuth2User principal) {

        try {
            WebClient client = webClientBuilder.build();

            // Fetch book from Open Library API
            Map book = client.get()
                    .uri("https://openlibrary.org/works/" + bookId + ".json")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (book == null) {
                return "book-not-found";
            }

            model.addAttribute("book", book);

            // Fetch author if available
            if (book.get("authors") != null) {
                java.util.List authors = (java.util.List) book.get("authors");
                if (!authors.isEmpty()) {
                    Map authorRef = (Map) authors.get(0);
                    String authorKey = (String) ((Map) authorRef.get("author")).get("key");
                    Map author = client.get()
                            .uri("https://openlibrary.org" + authorKey + ".json")
                            .retrieve()
                            .bodyToMono(Map.class)
                            .block();
                    if (author != null) {
                        model.addAttribute("authorName", author.get("name"));
                    }
                }
            }

        } catch (Exception e) {
            return "book-not-found";
        }

        return "book";
    }
}