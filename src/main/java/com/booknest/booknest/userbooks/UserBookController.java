package com.booknest.booknest.userbooks;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserBookController {

    private final UserBookRepository userBookRepository;

    @PostMapping("/save-book")
    public String saveBook(@RequestParam String bookId,
                           @RequestParam String status,
                           @RequestParam(required = false, defaultValue = "0") Integer rating,
                           @RequestParam(required = false) String bookName,
                           @RequestParam(required = false) String coverUrl,
                           @AuthenticationPrincipal OAuth2User principal) {

        if (principal == null) return "redirect:/oauth2/authorization/github";

        String userId = principal.getAttribute("login");

        UserBookKey key = new UserBookKey();
        key.setUserId(userId);
        key.setBookId(bookId);

        UserBook userBook = userBookRepository.findById(key).orElse(new UserBook());
        userBook.setKey(key);
        userBook.setReadingStatus(status);
        userBook.setBookName(bookName);
        userBook.setCoverUrl(coverUrl);
        userBook.setRating(rating);

        if ("READING".equals(status) && userBook.getStartedDate() == null)
            userBook.setStartedDate(LocalDate.now());
        if ("READ".equals(status) && userBook.getFinishedDate() == null)
            userBook.setFinishedDate(LocalDate.now());

        userBookRepository.save(userBook);

        // ✅ Redirect to my-books with success message
        return "redirect:/my-books?saved=true";
    }

    @PostMapping("/update-status")
    public String updateStatus(@RequestParam String bookId,
                               @RequestParam String status,
                               @AuthenticationPrincipal OAuth2User principal) {

        if (principal == null) return "redirect:/oauth2/authorization/github";

        String userId = principal.getAttribute("login");

        UserBookKey key = new UserBookKey();
        key.setUserId(userId);
        key.setBookId(bookId);

        UserBook userBook = userBookRepository.findById(key).orElse(null);
        if (userBook != null) {
            userBook.setReadingStatus(status);
            if ("READING".equals(status) && userBook.getStartedDate() == null)
                userBook.setStartedDate(LocalDate.now());
            if ("READ".equals(status) && userBook.getFinishedDate() == null)
                userBook.setFinishedDate(LocalDate.now());
            userBookRepository.save(userBook);
        }

        return "redirect:/my-books";
    }

    @PostMapping("/delete-book")
    public String deleteBook(@RequestParam String bookId,
                             @AuthenticationPrincipal OAuth2User principal) {

        if (principal == null) return "redirect:/oauth2/authorization/github";

        String userId = principal.getAttribute("login");

        UserBookKey key = new UserBookKey();
        key.setUserId(userId);
        key.setBookId(bookId);

        userBookRepository.deleteById(key);
        return "redirect:/my-books";
    }

    @GetMapping("/my-books")
    public String myBooks(Model model,
                          @AuthenticationPrincipal OAuth2User principal) {

        if (principal == null) return "redirect:/oauth2/authorization/github";

        String userId = principal.getAttribute("login");
        List<UserBook> books = userBookRepository.findAllByKeyUserId(userId);

        long readingCount = books.stream().filter(b -> "READING".equals(b.getReadingStatus())).count();
        long readCount = books.stream().filter(b -> "READ".equals(b.getReadingStatus())).count();
        long wantCount = books.stream().filter(b -> "WANT_TO_READ".equals(b.getReadingStatus())).count();

        model.addAttribute("books", books);
        model.addAttribute("readingCount", readingCount);
        model.addAttribute("readCount", readCount);
        model.addAttribute("wantCount", wantCount);

        return "my-books";
    }
}