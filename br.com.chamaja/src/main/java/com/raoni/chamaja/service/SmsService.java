package com.raoni.chamaja.service;

import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
public class SmsService {
        // Puxando as chaves lá do application.properties
        @Value("${twilio.account.sid}")
        private String accountSid;

        @Value("${twilio.auth.token}")
        private String authToken;

        @Value("${twilio.phone.number}")
        private String twilioPhoneNumber;




        public void enviarSms(String numeroDestino, String codigoValidacao) {
            //Fazer login no Twilio usando suas chaves
            Twilio.init(accountSid, authToken);

            // Passo 2: Montar a mensagem e apertar o botão de "Enviar"
            Message message = Message.creator(
                    new PhoneNumber(numeroDestino), // Para quem vai
                    new PhoneNumber(twilioPhoneNumber), // De quem está indo (número do Twilio)
                    "Olá! Seu código de verificação do ChamaJá é: " + codigoValidacao // O texto do SMS
            ).create();

            System.out.println("Mensagem enviada com sucesso! ID: " + message.getSid());
        }


    }

