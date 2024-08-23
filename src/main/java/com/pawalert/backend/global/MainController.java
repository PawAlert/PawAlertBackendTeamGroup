package com.pawalert.backend.global;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController { // 클래스 이름도 명확하게 변경

    @GetMapping("/login")
    public String index() {
        return "login"; // 뷰 이름 반환
    }

    @GetMapping("/")
    public String main() {
        return "loginOk"; // 뷰 이름 반환
    }

}