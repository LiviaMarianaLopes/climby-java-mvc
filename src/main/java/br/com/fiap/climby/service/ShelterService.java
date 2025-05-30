package br.com.fiap.climby.service;

import br.com.fiap.climby.configuration.RabbitMQConfig;
import br.com.fiap.climby.dto.ShelterRequest;
import br.com.fiap.climby.model.Shelter;
import br.com.fiap.climby.repository.ShelterRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShelterService {
    @Autowired
    ShelterRepository shelterRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Shelter salvarShelter(ShelterRequest shelterRequest) {
        Shelter shelter = requestToShelter(shelterRequest);
        Shelter savedShelter = shelterRepository.save(shelter);

        try {
            Long shelterId = savedShelter.getId();
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.SHELTER_EXCHANGE_NAME,
                    RabbitMQConfig.SHELTER_CREATED_ROUTING_KEY,
                    shelterId
            );
            System.out.println(">>> PRODUTOR: Mensagem enviada para RabbitMQ - Abrigo Criado ID: " + shelterId);
        } catch (Exception e) {
            System.err.println("### PRODUTOR: Erro ao enviar mensagem para RabbitMQ: " + e.getMessage());
        }

        return savedShelter;
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
        BeanUtils.copyProperties(shelterRequest, shelter);
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