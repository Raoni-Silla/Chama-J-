package com.raoni.chamaja.service;

import com.raoni.chamaja.Erros.EntityAlreadyExistsException;
import com.raoni.chamaja.dto.Cadastro.CadastroInicialRequestDTO;
import com.raoni.chamaja.dto.Cadastro.CadastroResponseDTO;
import com.raoni.chamaja.dto.usuario.UsuarioResponseDTO;
import com.raoni.chamaja.enums.StatusCadastro;
import com.raoni.chamaja.enums.TipoUsuario;
import com.raoni.chamaja.model.CadastroTemporario;
import com.raoni.chamaja.model.Usuario;
import com.raoni.chamaja.repository.CadastroTemporarioRepository;
import com.raoni.chamaja.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class CadastroService {

    private final UsuarioRepository userRepo;
    private final CadastroTemporarioRepository cadastroTemporarioRepository;
    private final SmsService smsService;

    private String normalizarNome(String nome) {
        nome = nome.trim().toLowerCase();

        String[] partes = nome.split(" ");
        StringBuilder resultado = new StringBuilder();

        for (String parte : partes) {
            if (!parte.isEmpty()) {
                resultado.append(Character.toUpperCase(parte.charAt(0)))
                        .append(parte.substring(1))
                        .append(" ");
            }
        }

        return resultado.toString().trim();
    }

    private String gerarNumeroAleatorioValidacao() {
        int codigo = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(codigo);
    }

    private boolean isCpfValido(String cpf) {

        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int soma = 0;
            int peso = 10;
            for (int i = 0; i < 9; i++) {
                int num = cpf.charAt(i) - '0';
                soma += (num * peso);
                peso--;
            }

            int resto = 11 - (soma % 11);
            int primeiroDigitoCalculado = (resto == 10 || resto == 11) ? 0 : resto;

            if (primeiroDigitoCalculado != (cpf.charAt(9) - '0')) {
                return false;
            }

            soma = 0;
            peso = 11;
            for (int i = 0; i < 10; i++) {
                int num = cpf.charAt(i) - '0';
                soma += (num * peso);
                peso--;
            }

            resto = 11 - (soma % 11);
            int segundoDigitoCalculado = (resto == 10 || resto == 11) ? 0 : resto;

            return segundoDigitoCalculado == (cpf.charAt(10) - '0');

        } catch (Exception e) {
            return false;
        }
    }

    private boolean validarDataNascimento(LocalDate data) {
        if (data == null) return false;

        LocalDate hoje = LocalDate.now();

        if (data.isAfter(hoje)) return false;

        if (data.isBefore(LocalDate.of(1900, 1, 1))) return false;

        int idade = Period.between(data, hoje).getYears();

        return idade >= 18;
    }

    //padrão PRECISA ser +5518996985449 pro twillo entender
    private String formatarTelefoneProTwilio(String telefoneDigitado) {
        String numeroLimpo = telefoneDigitado.replaceAll("\\D", "");

        // o número limpo terá 13 dígitos. A gente só põe o "+" na frente.
        // 2. Se o usuário já digitou o 55 no começo (ex: 55 18 99698-5449),
        if (numeroLimpo.startsWith("55") && numeroLimpo.length() == 13) {
            return "+" + numeroLimpo;
        }

        // 3. Se o número limpo tem 11 dígitos (DDD + 9 dígitos do celular)
        if (numeroLimpo.length() == 11) {
            return "+55" + numeroLimpo;
        }

        throw new IllegalArgumentException("O número digitado está incompleto ou inválido. Por favor, inclua o DDD.");
    }

    private void validarEmailDuplicado (String email){
        if (userRepo.existsByEmail(email)){
            throw new EntityAlreadyExistsException("Email já existente no ChamaJá");
        }
    }

    private void validarCpfDuplicado (String cpf){
        if (userRepo.existsByCpf(cpf)){
            throw new EntityAlreadyExistsException("Cpf já existente no ChamaJá");
        }
    }

    private void validarTelefoneDuplicado (String telefone){
        if (userRepo.existsByCpf(telefone)){
            throw new EntityAlreadyExistsException("Telefone já existente no ChamaJá");
        }
    }

    private CadastroResponseDTO gerarDtoCadastroResposta(CadastroTemporario cadastro){
        return new CadastroResponseDTO(
                cadastro.getId(),
                cadastro.getStatus().name()
        );
    }

    private UsuarioResponseDTO gerarDtoUsuarioResposta (Usuario usuario){
        return new UsuarioResponseDTO(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipoUsuario(),
                usuario.getNotaMedia(),
                usuario.getRaioAtuacao()
        );
    }


    @Transactional
    public CadastroResponseDTO iniciarCadastro(CadastroInicialRequestDTO dto) {
        CadastroTemporario cadastro = new CadastroTemporario();

        validarEmailDuplicado(dto.email());
        validarCpfDuplicado(dto.cpf());


        cadastro.setNome(normalizarNome(dto.nome()));
        cadastro.setEmail(dto.email().trim().toLowerCase());

        if (!isCpfValido(dto.cpf())) {
            throw new IllegalArgumentException("CPF inválido");
        }

        cadastro.setCpf(dto.cpf());

        if (!validarDataNascimento(dto.dataNascimento())) {
            throw new IllegalArgumentException("Data de nascimento inválida");
        }

        cadastro.setDataNascimento(dto.dataNascimento());
        cadastro.setSenha((dto.senha()));
        cadastro.setStatus(StatusCadastro.INICIADO);
        cadastroTemporarioRepository.save(cadastro);

        return gerarDtoCadastroResposta(cadastro);
    }

    @Transactional
    public CadastroResponseDTO adicionarTelefone(Long cadastroId, String telefone) {
        CadastroTemporario cadastro = cadastroTemporarioRepository.findById(cadastroId).orElseThrow(() -> new EntityNotFoundException("Não Encontramos o seu usuario"));
        validarTelefoneDuplicado(formatarTelefoneProTwilio(telefone));
        cadastro.setStatus(StatusCadastro.AGUARDANDO_TELEFONE);
        cadastro.setTelefone(formatarTelefoneProTwilio(telefone));
        cadastroTemporarioRepository.save(cadastro);
        return gerarDtoCadastroResposta(cadastro);
    }

    @Transactional
    public CadastroResponseDTO solicitarEnvioSms(Long cadastroId) {
        CadastroTemporario cadastro = cadastroTemporarioRepository.findById(cadastroId)
                .orElseThrow(() -> new EntityNotFoundException("Não encontramos o seu usuário"));

        String codigoGerado = gerarNumeroAleatorioValidacao();

        cadastro.setCodigoSms(codigoGerado);
        cadastro.setStatus(StatusCadastro.AGUARDANDO_SMS);

        try {
            smsService.enviarSms(cadastro.getTelefone(), codigoGerado);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar SMS: " + e.getMessage());
        }

        cadastroTemporarioRepository.save(cadastro);
        return gerarDtoCadastroResposta(cadastro);
    }

    @Transactional
    public CadastroResponseDTO confirmarCodigoSms(Long cadastroId, String codigoDigitadoPeloUsuario) {
        CadastroTemporario cadastro = cadastroTemporarioRepository.findById(cadastroId)
                .orElseThrow(() -> new EntityNotFoundException("Não encontramos o seu usuário"));

        if (cadastro.getCodigoSms().equals(codigoDigitadoPeloUsuario)) {
            cadastro.setStatus(StatusCadastro.TELEFONE_VALIDADO);
            cadastro.setTelefoneValidado(true);
            cadastroTemporarioRepository.save(cadastro);
            return gerarDtoCadastroResposta(cadastro);
        } else {
            throw new RuntimeException("Código de verificação inválido!");
        }
    }


    @Transactional
    public UsuarioResponseDTO usuarioOuPrestador(Long cadastroId, String tipoUsuario) {

        CadastroTemporario cadastro = cadastroTemporarioRepository.findById(cadastroId)
                .orElseThrow(() -> new EntityNotFoundException("Não encontramos o seu usuário"));

        TipoUsuario tipoEnum;

        try {
            tipoEnum = TipoUsuario.valueOf(tipoUsuario.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de usuário inválido: " + tipoUsuario);
        }

        Usuario usuario = new Usuario();
        usuario.setNome(cadastro.getNome());
        usuario.setEmail(cadastro.getEmail());
        usuario.setSenha(cadastro.getSenha());
        usuario.setCpf(cadastro.getCpf());
        usuario.setTipoUsuario(tipoEnum);
        usuario.setTelefone(cadastro.getTelefone());
        usuario.setDataDeNascimento(cadastro.getDataNascimento());

        Usuario usuarioSalvo = userRepo.save(usuario);

        cadastroTemporarioRepository.delete(cadastro);

        return gerarDtoUsuarioResposta(usuarioSalvo);
    }
}