package br.com.fiap.climby.controller;

import br.com.fiap.climby.service.AiChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/ai")
public class AiController {

    @Autowired
    private AiChatService aiChatService;

    @GetMapping("/chat")
    public String chatPage() {
        return "aiChat";
    }

    @PostMapping("/ask")
    public String askQuestion(@RequestParam String question, Model model) {
        String answer = aiChatService.getAnswer(question);
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "aiChat";
    }
}