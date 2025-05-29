package br.com.fiap.climby.controller;

import br.com.fiap.climby.dto.ShelterRequest;
import br.com.fiap.climby.model.Shelter;
import br.com.fiap.climby.service.ShelterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/shelter")
public class ShelterController {
    @Autowired
    ShelterService shelterService;

    @GetMapping("/cadastro")
    public String shelterCadastro(Model model) {
        model.addAttribute("shelterRequest", new ShelterRequest());
        return "shelterRegister";
    }

    @PostMapping("/cadastrar")
    public String cadastrarShelter(@Valid @ModelAttribute("shelterRequest") ShelterRequest shelterRequest, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "shelterRegister";
        }
        shelterService.salvarShelter(shelterRequest);
        return "redirect:/shelter/lista";
    }

    @GetMapping("/lista")
    public String listarShelters(Model model){
        List<Shelter> shelters = shelterService.buscarShelters();
        model.addAttribute("listaShelters", shelters);
        return "shelterList";
    }

    @GetMapping("/edicao/{id}")
    public String shelterEdicao(@PathVariable Long id, Model model){
        Shelter shelterEntity = shelterService.buscarShelter(id);
        if (shelterEntity == null) {
            return "redirect:/shelter/lista";
        }
        model.addAttribute("idShelter", id);
        model.addAttribute("shelterRequest", shelterService.shelterToRequest(shelterEntity));
        return "shelterEdit";
    }

    @PostMapping("/editar/{id}")
    public String editarShelter(@PathVariable Long id,
                                @Valid @ModelAttribute("shelterRequest") ShelterRequest shelterRequest,
                                BindingResult result,
                                Model model){
        if(result.hasErrors()) {
            model.addAttribute("idShelter", id);
            return "shelterEdit";
        }
        shelterService.atualizarShelter(id, shelterRequest);
        return "redirect:/shelter/lista";
    }

    @GetMapping("/deletar/{id}")
    public String deletarShelter(@PathVariable Long id, Model model){
        shelterService.deletarShelter(id);
        return "redirect:/shelter/lista";
    }
}