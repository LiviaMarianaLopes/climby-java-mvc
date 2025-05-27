package br.com.fiap.climby;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @GetMapping("/ola")
    public String hello(Model model){
        model.addAttribute("message", "Hello World");
        return "hello";
    }
}
