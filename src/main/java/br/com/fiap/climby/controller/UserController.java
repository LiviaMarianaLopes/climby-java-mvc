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
    public String cadastrarUser(@Valid @ModelAttribute UserRequest userRequest, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "userRegister";
        }
        userService.salvarUser(userRequest);
        return listarUsers(model);
    }

    @GetMapping("/lista")
    public String listarUsers(Model model){
        List<User> users = userService.buscarUsers();
        model.addAttribute("listaUsers", users);
        return"userList";
    }

    @GetMapping("/edicao/{id}")
    public String userEdicao(@PathVariable Long id, Model model){
        User user = userService.buscarUser(id);
        if (user == null) {
            return listarUsers(model);
        }
        model.addAttribute("idUser", id);
        model.addAttribute("user", userService.userToRequest(user));
        return "userEdit";
    }

    @PostMapping("/editar/{id}")
    public String editarUser(@PathVariable Long id, @Valid @ModelAttribute UserRequest userRequest, BindingResult result, Model model){
        if(result.hasErrors()) {
            return "user/edicao/"+id;
        }
        userService.atualizarUser(id, userRequest);
        return listarUsers(model);
    }

    @GetMapping("/deletar/{id}")
    public String deletarUser(@PathVariable Long id, Model model){
        userService.deletarUser(id);
        return listarUsers(model);
    }
}