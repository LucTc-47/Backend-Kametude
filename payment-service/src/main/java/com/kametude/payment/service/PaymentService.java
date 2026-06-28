package com.kametude.payment.service;

import com.kametude.payment.dto.PaymentRequestDTO;
import com.kametude.payment.dto.PaymentResponseDTO;
import com.kametude.payment.entity.PaymentTransaction;
import com.kametude.payment.provider.PaymentProvider;
import com.kametude.payment.repository.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentProvider paymentProvider;
    private final PaymentTransactionRepository paymentTransactionRepository;

    public PaymentResponseDTO traiterPaiement(PaymentRequestDTO requestDTO) {

        // 1. On appelle le provider pour initier le paiement
        PaymentResponseDTO reponseProvider = paymentProvider.initierPaiement(requestDTO);

        // 2. On sauvegarde la transaction dans notre propre base de données
        PaymentTransaction transaction = PaymentTransaction.builder()
                .orderId(requestDTO.getOrderId())
                .amount(requestDTO.getAmount())
                .status(reponseProvider.getStatus())
                .provider(paymentProvider.getProviderName())
                .externalReference(reponseProvider.getExternalReference())
                .phone(requestDTO.getPhone())
                .build();

        PaymentTransaction transactionSauvegardee = paymentTransactionRepository.save(transaction);

        // 3. On renvoie la réponse avec l'ID réel généré par notre base
        return PaymentResponseDTO.builder()
                .transactionId(transactionSauvegardee.getId())
                .status(transactionSauvegardee.getStatus())
                .externalReference(transactionSauvegardee.getExternalReference())
                .message(reponseProvider.getMessage())
                .build();
    }
}