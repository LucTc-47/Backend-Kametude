package com.kametude.payment.provider;

import com.kametude.payment.dto.PaymentRequestDTO;
import com.kametude.payment.dto.PaymentResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class FedapayProvider implements PaymentProvider {

    @Value("${fedapay.secret-key}")
    private String secretKey;

    @Value("${fedapay.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public PaymentResponseDTO initierPaiement(PaymentRequestDTO requestDTO) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + secretKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> currency = new HashMap<>();
        currency.put("iso", "XOF");

        Map<String, Object> body = new HashMap<>();
        body.put("amount", requestDTO.getAmount());
        body.put("description", "Paiement Kametud - commande " + requestDTO.getOrderId());
        body.put("currency", currency);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

       ResponseEntity<Map> response = restTemplate.exchange(
        baseUrl + "/transactions",
        HttpMethod.POST,
        request,
        Map.class
);

        Map<String, Object> responseBody = response.getBody();
        Map<String, Object> transactionData = (Map<String, Object>) responseBody.get("v1/transaction");
        String transactionId = String.valueOf(transactionData.get("id"));
        String status = String.valueOf(transactionData.get("status"));

        return PaymentResponseDTO.builder()
            .status(status.toUpperCase())
            .externalReference(transactionId)
            .message("Paiement initié (FedaPay sandbox) pour la commande " + requestDTO.getOrderId())
            .build();
    }

    @Override
    public String verifierStatut(String externalReference) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + secretKey);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl + "/transactions/" + externalReference,
                HttpMethod.GET,
                request,
                Map.class
        );

        Map<String, Object> responseBody = response.getBody();
        return String.valueOf(responseBody.get("status"));
    }

    @Override
    public String getProviderName() {
        return "FedaPay";
    }
}