package br.com.fiap.climby.service;

import br.com.fiap.climby.dto.ShelterRequest;
import br.com.fiap.climby.model.Shelter;
import br.com.fiap.climby.repository.ShelterRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShelterService {
    @Autowired
    ShelterRepository shelterRepository;

    public Shelter salvarShelter(ShelterRequest shelterRequest) {
        Shelter shelter = requestToShelter(shelterRequest);
        return shelterRepository.save(shelter);
    }

    public void atualizarShelter(Long id, ShelterRequest shelterRequest) {
        Shelter shelter = buscarShelter(id);
        if (shelter != null) {
            BeanUtils.copyProperties(shelterRequest, shelter);
            shelterRepository.save(shelter);
        }
    }

    public void deletarShelter(Long id) {
        shelterRepository.deleteById(id);
    }

    public Shelter requestToShelter(ShelterRequest shelterRequest) {
        Shelter shelter = new Shelter();
        shelter.setName(shelterRequest.getName());
        shelter.setEmail(shelterRequest.getEmail());
        shelter.setPhone(shelterRequest.getPhone());
        shelter.setCountry(shelterRequest.getCountry());
        shelter.setCity(shelterRequest.getCity());
        shelter.setAdress(shelterRequest.getAdress());
        shelter.setAdressNumber(shelterRequest.getAdressNumber());
        shelter.setCep(shelterRequest.getCep());
        shelter.setDistrict(shelterRequest.getDistrict());
        return shelter;
    }

    public ShelterRequest shelterToRequest(Shelter shelter) {
        ShelterRequest shelterRequest = new ShelterRequest();
        BeanUtils.copyProperties(shelter, shelterRequest);
        return shelterRequest;
    }

    public List<Shelter> buscarShelters() {
        return shelterRepository.findAll();
    }

    public Shelter buscarShelter(Long id) {
        Optional<Shelter> shelter = shelterRepository.findById(id);
        return shelter.orElse(null);
    }

}