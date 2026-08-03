package com.raoni.chamaja.service;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class GatewayDePagamentoService {

    public String cobrarCartao(BigDecimal valorTransacao, String tokenCartao, String emailCliente, String bandeira) {

        //classe oficial do mercado pago para fazer pagamentos
        PaymentClient client = new PaymentClient();

        // Montamos o corpo da requisição com os dados da cobrança
        PaymentCreateRequest request = PaymentCreateRequest.builder()
                .transactionAmount(valorTransacao)
                .token(tokenCartao) // O token seguro que estará no seu CartaoSalvo
                .description("Pagamento de serviço - ChamaJa")
                .installments(1) // Pagamento à vista (1 parcela)
                .paymentMethodId(bandeira.toLowerCase()) // ex: "visa", "master"
                .payer(PaymentPayerRequest.builder()
                        .email(emailCliente)
                        .build())
                .build();

        try {
            // Dispara a cobrança pro Mercado Pago
            Payment payment = client.create(request);
            // Retorna o status ("approved", "in_process", "rejected")
            return payment.getStatus();

        } catch (MPException | MPApiException e) {
            System.err.println("Erro ao comunicar com Mercado Pago: " + e.getMessage());
            return "erro_gateway";
        }
    }

}
