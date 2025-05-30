package br.com.fiap.climby.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String SHELTER_EXCHANGE_NAME = "climby.shelter.exchange";

    public static final String SHELTER_CREATED_QUEUE_NAME = "climby.shelter.created.queue";

    public static final String SHELTER_CREATED_ROUTING_KEY = "shelter.event.created";

    @Bean
    public TopicExchange shelterExchange() {
        return new TopicExchange(SHELTER_EXCHANGE_NAME);
    }

    @Bean
    public Queue shelterCreatedQueue() {
        return new Queue(SHELTER_CREATED_QUEUE_NAME, true);
    }

    @Bean
    public Binding shelterCreatedBinding(Queue shelterCreatedQueue, TopicExchange shelterExchange) {
        return BindingBuilder.bind(shelterCreatedQueue).to(shelterExchange).with(SHELTER_CREATED_ROUTING_KEY);
    }
}