package br.com.fiap.climby.consumer;

import br.com.fiap.climby.configuration.RabbitMQConfig;
import br.com.fiap.climby.model.Shelter;
import br.com.fiap.climby.service.ShelterService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShelterMessageConsumer {

    @Autowired
    private ShelterService shelterService;

    @RabbitListener(queues = RabbitMQConfig.SHELTER_CREATED_QUEUE_NAME)
    public void receiveShelterCreatedMessage(Long shelterId) {
        System.out.println("<<< CONSUMIDOR: Mensagem recebida de RabbitMQ - ID do Abrigo Criado: " + shelterId);
        Shelter shelter = shelterService.buscarShelter(shelterId);
        if (shelter != null) {
            System.out.println("<<< CONSUMIDOR: Detalhes do Abrigo: " + shelter.getName() + ", Email: " + shelter.getEmail());
        } else {
            System.err.println("### CONSUMIDOR: Abrigo com ID " + shelterId + " não encontrado.");
        }
    }
}