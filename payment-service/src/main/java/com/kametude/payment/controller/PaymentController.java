package com.kametude.payment.controller;

import com.kametude.payment.dto.PaymentRequestDTO;
import com.kametude.payment.dto.PaymentResponseDTO;
import com.kametude.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> creerPaiement(@RequestBody PaymentRequestDTO requestDTO) {
        PaymentResponseDTO reponse = paymentService.traiterPaiement(requestDTO);
        return ResponseEntity.ok(reponse);
    }
}