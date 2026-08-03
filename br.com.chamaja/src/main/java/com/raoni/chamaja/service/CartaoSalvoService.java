package com.raoni.chamaja.service;

import com.raoni.chamaja.dto.Cartao.CartaoSalvoResponseDTO;
import com.raoni.chamaja.dto.MercadoPago.MercadoPagoCardResponse;
import com.raoni.chamaja.dto.MercadoPago.MercadoPagoCustomerResponse;
import com.raoni.chamaja.dto.MercadoPago.MercadoPagoCustomerSearchResponse;
import com.raoni.chamaja.model.CartaoSalvo;
import com.raoni.chamaja.model.Usuario;
import com.raoni.chamaja.repository.CartaoSalvoRepository;
import com.raoni.chamaja.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartaoSalvoService {

    private final CartaoSalvoRepository cartaoSalvoRepository;
    private final UsuarioRepository usuarioRepository;
    private final RestClient mercadoPagoRestClient;


    private Long obterIdUsuarioLogado (){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(authentication.getName());
    }

    private String obterOuCriarCustomer(Usuario usuario) {

        String customerIdSalvo = usuario.getMercadoPagoCustomerId();

        if (customerIdSalvo != null && !customerIdSalvo.isBlank()) {
            return customerIdSalvo;
        }

        MercadoPagoCustomerSearchResponse pesquisa =
                mercadoPagoRestClient
                        .get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/v1/customers/search")
                                .queryParam("email", usuario.getEmail())
                                .build()
                        )
                        .retrieve()
                        .body(MercadoPagoCustomerSearchResponse.class);

        if (pesquisa != null
                && pesquisa.results() != null
                && !pesquisa.results().isEmpty()) {

            MercadoPagoCustomerResponse customerExistente =
                    pesquisa.results().getFirst();

            usuario.setMercadoPagoCustomerId(
                    customerExistente.id()
            );

            usuarioRepository.save(usuario);

            return customerExistente.id();
        }

        String[] partesNome = usuario
                .getNome()
                .trim()
                .split("\\s+", 2);

        Map<String, Object> body = new HashMap<>();

        body.put("email", usuario.getEmail());
        body.put("first_name", partesNome[0]);

        if (partesNome.length > 1) {
            body.put("last_name", partesNome[1]);
        }

        MercadoPagoCustomerResponse novoCustomer =
                mercadoPagoRestClient
                        .post()
                        .uri("/v1/customers")
                        .body(body)
                        .retrieve()
                        .body(MercadoPagoCustomerResponse.class);

        if (novoCustomer == null || novoCustomer.id() == null) {
            throw new IllegalStateException(
                    "O Mercado Pago não retornou o ID do cliente"
            );
        }

        usuario.setMercadoPagoCustomerId(novoCustomer.id());
        usuarioRepository.save(usuario);

        return novoCustomer.id();
    }


    @Transactional
    public void salvarCartao(String token) {
        Long idUsuario = obterIdUsuarioLogado();

        Usuario usuario = usuarioRepository
                .findById(idUsuario)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Impossível encontrar esse usuário"
                        )
                );

        String customerId = obterOuCriarCustomer(usuario);

        Map<String, String> body = Map.of(
                "token",
                token
        );

        MercadoPagoCardResponse mpCard =
                mercadoPagoRestClient
                        .post()
                        .uri(
                                "/v1/customers/{customerId}/cards",
                                customerId
                        )
                        .body(body)
                        .retrieve()
                        .body(MercadoPagoCardResponse.class);

        if (mpCard == null || mpCard.id() == null) {
            throw new IllegalStateException(
                    "O Mercado Pago não retornou o cartão salvo"
            );
        }

        CartaoSalvo cartao = createCartao(usuario, mpCard);

        cartao.setUsuario(usuario);

        cartaoSalvoRepository.save(cartao);
    }

    private static @NonNull CartaoSalvo createCartao(Usuario usuario, MercadoPagoCardResponse mpCard) {
        CartaoSalvo cartao = new CartaoSalvo();

        cartao.setUsuario(usuario);
        cartao.setMercadoPagoCardId(mpCard.id());
        cartao.setUltimosDigitos(mpCard.lastFourDigits());
        cartao.setBandeira(mpCard.paymentMethod().id());
        cartao.setNomeTitular(mpCard.cardholder().name());

        cartao.setMesAnoVencimento(
                String.format(
                        "%02d/%02d",
                        mpCard.expirationMonth(),
                        mpCard.expirationYear() % 100
                )
        );
        return cartao;
    }

    private CartaoSalvoResponseDTO transformarEmResponse (CartaoSalvo cartaoSalvo) {
        return new CartaoSalvoResponseDTO(cartaoSalvo.getId(),cartaoSalvo.getNomeTitular(), cartaoSalvo.getUltimosDigitos(),cartaoSalvo.getMesAnoVencimento(), cartaoSalvo.getBandeira());
    }


    public List<CartaoSalvoResponseDTO> listarCartoesSalvos() {
        Long idUsuario = obterIdUsuarioLogado();

        return cartaoSalvoRepository.findAllByUsuario_Id(idUsuario)
                .stream()
                .map(this::transformarEmResponse)
                .toList();
    }


    @Transactional
    public void excluirCartao(Long idCartao) {
        Long idUsuario = obterIdUsuarioLogado();

        CartaoSalvo cartao = cartaoSalvoRepository
                .findByIdAndUsuario_Id(idCartao, idUsuario)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Cartão não encontrado para este usuário"
                        )
                );

        Usuario usuario = cartao.getUsuario();

        mercadoPagoRestClient
                .delete()
                .uri(
                        "/v1/customers/{customerId}/cards/{cardId}",
                        usuario.getMercadoPagoCustomerId(),
                        cartao.getMercadoPagoCardId()
                )
                .retrieve()
                .toBodilessEntity();

        cartaoSalvoRepository.delete(cartao);
    }

}
