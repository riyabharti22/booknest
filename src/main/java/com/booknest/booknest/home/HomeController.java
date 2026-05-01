package com.booknest.booknest.home;

import com.booknest.booknest.userbooks.UserBook;
import com.booknest.booknest.userbooks.UserBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserBookRepository userBookRepository;

    @GetMapping("/")
    public String home(Model model,
                       @AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            String userId = principal.getAttribute("login");
            List<UserBook> books = userBookRepository.findAllByKeyUserId(userId);
            model.addAttribute("books", books);
            model.addAttribute("username", principal.getAttribute("name"));
        }
        return "home";
    }
}