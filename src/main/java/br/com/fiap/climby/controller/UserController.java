package br.com.fiap.climby.controller;

import br.com.fiap.climby.dto.UserRequest;
import br.com.fiap.climby.model.User;
import br.com.fiap.climby.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/cadastro")
    public String userCadastro(Model model) {
        model.addAttribute("userRequest", new UserRequest());
        return "userRegister";
    }

    @PostMapping("/cadastrar")
    public String cadastrarUser(@Valid @ModelAttribute("userRequest") UserRequest userRequest, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "userRegister";
        }
        userService.salvarUser(userRequest);
        return "redirect:/user/lista";
    }

    @GetMapping("/lista")
    public String listarUsers(Model model){
        List<User> users = userService.buscarUsers();
        model.addAttribute("listaUsers", users);
        return "userList";
    }

    @GetMapping("/edicao/{id}")
    public String userEdicao(@PathVariable Long id, Model model){
        User userEntity = userService.buscarUser(id);
        if (userEntity == null) {
            return "redirect:/user/lista";
        }
        model.addAttribute("idUser", id);
        model.addAttribute("user", userService.userToRequest(userEntity));
        return "userEdit";
    }

    @PostMapping("/editar/{id}")
    public String editarUser(@PathVariable Long id,
                             @Valid @ModelAttribute("user") UserRequest userRequest,
                             BindingResult result,
                             Model model){
        if(result.hasErrors()) {
            model.addAttribute("idUser", id);
            return "userEdit";
        }
        userService.atualizarUser(id, userRequest);
        return "redirect:/user/lista";
    }

    @GetMapping("/deletar/{id}")
    public String deletarUser(@PathVariable Long id, Model model){
        userService.deletarUser(id);
        return "redirect:/user/lista";
    }
}