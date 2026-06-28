package com.kametude.payment.provider;

import com.kametude.payment.dto.PaymentRequestDTO;
import com.kametude.payment.dto.PaymentResponseDTO;

public interface PaymentProvider {

    /**
     * Initie une transaction de paiement auprès du fournisseur (Campay, FedaPay...).
     */
    PaymentResponseDTO initierPaiement(PaymentRequestDTO requestDTO);

    /**
     * Vérifie le statut d'une transaction auprès du fournisseur, via sa référence externe.
     */
    String verifierStatut(String externalReference);

    /**
     * Permet de savoir quel fournisseur est utilisé par cette implémentation (ex: "Campay", "FedaPay").
     */
    String getProviderName();
}