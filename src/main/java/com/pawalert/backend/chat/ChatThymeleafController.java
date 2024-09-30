package com.pawalert.backend.chat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatThymeleafController {

    @GetMapping("/chat")
    public String chatPage() {
        return "chat";
    }
}
