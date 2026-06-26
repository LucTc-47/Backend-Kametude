package com.kametude.request_service.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EventPublisher {

    @Autowired(required = false)
    private RabbitTemplate rabbitTemplate;

    public void publishProposalAccepted(Long proposalId, Long jobRequestId, Long studentId, Long clientId) {
        if (rabbitTemplate == null) return;
        Map<String, Object> event = Map.of(
                "proposalId", proposalId,
                "jobRequestId", jobRequestId,
                "studentId", studentId,
                "clientId", clientId
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.PROPOSAL_ACCEPTED_QUEUE, event);
    }

    public void publishProposalReopened(Long jobRequestId) {
        if (rabbitTemplate == null) return;
        Map<String, Object> event = Map.of("jobRequestId", jobRequestId);
        rabbitTemplate.convertAndSend(RabbitMQConfig.PROPOSAL_REOPENED_QUEUE, event);
    }
}