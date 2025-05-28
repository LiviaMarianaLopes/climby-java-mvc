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
    public String cadastrarShelter(@Valid @ModelAttribute ShelterRequest shelterRequest, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "shelterRegister";
        }
        shelterService.salvarShelter(shelterRequest);
        return listarShelters(model);
    }

    @GetMapping("/lista")
    public String listarShelters(Model model){
        List<Shelter> shelters = shelterService.buscarShelters();
        model.addAttribute("listaShelters", shelters);
        return"shelterList";
    }

    @GetMapping("/edicao/{id}")
    public String shelterEdicao(@PathVariable Long id, Model model){
        Shelter shelter = shelterService.buscarShelter(id);
        if (shelter == null) {
            return listarShelters(model);
        }
        model.addAttribute("idShelter", id);
        model.addAttribute("shelter", shelterService.shelterToRequest(shelter));
        return "shelterEdit";
    }

    @PostMapping("/editar/{id}")
    public String editarShelter(@PathVariable Long id, @Valid @ModelAttribute ShelterRequest shelterRequest, BindingResult result, Model model){
        if(result.hasErrors()) {
            return "shelter/edicao/"+id;
        }
        shelterService.atualizarShelter(id, shelterRequest);
        return listarShelters(model);
    }

    @GetMapping("/deletar/{id}")
    public String deletarShelter(@PathVariable Long id, Model model){
        shelterService.deletarShelter(id);
        return listarShelters(model);
    }
}