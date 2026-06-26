package com.kametude.request_service.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PROPOSAL_ACCEPTED_QUEUE = "proposal.accepted";
    public static final String PROPOSAL_REOPENED_QUEUE = "proposal.reopened";

    @Bean
    public Queue proposalAcceptedQueue() {
        return new Queue(PROPOSAL_ACCEPTED_QUEUE, true);
    }

    @Bean
    public Queue proposalReopenedQueue() {
        return new Queue(PROPOSAL_REOPENED_QUEUE, true);
    }
}