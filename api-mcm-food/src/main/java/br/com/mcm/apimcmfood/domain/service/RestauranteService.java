package br.com.mcm.apimcmfood.domain.service;

import br.com.mcm.apimcmfood.api.model.restaurante.mapper.RestauranteRequestMapper;
import br.com.mcm.apimcmfood.domain.entity.*;
import br.com.mcm.apimcmfood.domain.exception.EntidadeEmUsoException;
import br.com.mcm.apimcmfood.domain.exception.EntidadeNaoEncontradaException;
import br.com.mcm.apimcmfood.domain.exception.NegocioException;
import br.com.mcm.apimcmfood.infrastructure.repository.RestauranteRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class RestauranteService {

    private static final String MSG_RESTAURANTE_NAO_ENCONTRADO
            = "Não existe um cadastro de restaurante com código %d";

    private static final String MSG_RESTAURANTE_COZINHA_INVALIDA
            = "Cozinha com código %d não existe, favor informar uma cozinha válida";

    private static final String MSG_COZINHA_EM_USO =
            "Não é possível remover o restaurante com o código %d, pois está associada a uma ou mais cozinhas.";


    @Autowired
    private RestauranteRequestMapper restauranteRequestMapper;
    private RestauranteRepository restauranteRepository;
    private CozinhaService cozinhaService;
    private CidadeService cidadeService;
    private FormaPagamentoService formaPagamentoService;
    private UsuarioService usuarioService;

    public RestauranteService(
            final RestauranteRepository restauranteRepository,
            final CozinhaService cozinhaService,
            final CidadeService cidadeService,
            final FormaPagamentoService formaPagamentoService,
            final UsuarioService usuarioService
    ) {
        this.restauranteRepository = Objects.requireNonNull(restauranteRepository);
        this.cozinhaService = Objects.requireNonNull(cozinhaService);
        this.cidadeService = Objects.requireNonNull(cidadeService);
        this.formaPagamentoService = Objects.requireNonNull(formaPagamentoService);
        this.usuarioService = Objects.requireNonNull(usuarioService);
    }


    public Restaurante adicionar(final Restaurante restaurante) {
        return salvarRestaurante(restaurante);
    }

    public Restaurante buscar(final Long id) {
        return buscarOuFalhar(id);
    }

    public List<Restaurante> listar() {
        return restauranteRepository.findAll();
    }

    public Restaurante atualizar(final Restaurante restaurante) {
        return salvarRestaurante(restaurante);
    }

    public void remover(final Long id) {
        try {
            restauranteRepository.deleteById(id);
            restauranteRepository.flush();
        } catch (EmptyResultDataAccessException e) {
            throw new EntidadeNaoEncontradaException(
                    toString().formatted(MSG_RESTAURANTE_NAO_ENCONTRADO, id)
            );
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_COZINHA_EM_USO, id)
            );
        }
    }

    @Transactional
    public void ativar(final Long id) {
        Restaurante restauranteAtual = buscarOuFalhar(id);
        restauranteAtual.ativar();
    }

    @Transactional
    public void inativar(final Long id) {
        Restaurante restauranteAtual = buscarOuFalhar(id);
        restauranteAtual.inativar();
    }

    @Transactional
    public void ativar(List<Long> restauranteIds) {
        restauranteIds.forEach(this::ativar);
    }

    @Transactional
    public void inativar(List<Long> restauranteIds) {
        restauranteIds.forEach(this::inativar);
    }

    @Transactional
    public void associarFormaPagamento(final Long restauranteId, Long formaPagamentoId) {
        Restaurante restaurante = buscarOuFalhar(restauranteId);
        FormaPagamento formaPagamento = formaPagamentoService.buscar(formaPagamentoId);

        restaurante.associarFormaPagamento(formaPagamento);
    }

    @Transactional
    public void desassociarFormaPagamento(final Long restauranteId, Long formaPagamentoId) {
        Restaurante restaurante = buscarOuFalhar(restauranteId);
        FormaPagamento formaPagamento = formaPagamentoService.buscar(formaPagamentoId);

        restaurante.desassociarFormaPagamento(formaPagamento);
    }

    @Transactional
    public void associarUsuarioResponsavel(final Long restauranteId, Long usuarioId) {
        Restaurante restaurante = buscarOuFalhar(restauranteId);
        Usuario usuario = usuarioService.buscar(usuarioId);

        restaurante.associarUsuarioResponsavel(usuario);
    }

    @Transactional
    public void desassociarUsuarioResponsavel(final Long restauranteId, Long usuarioId) {
        Restaurante restaurante = buscarOuFalhar(restauranteId);
        Usuario usuario = usuarioService.buscar(usuarioId);

        restaurante.desassociarUsuarioResponsavel(usuario);
    }

    @Transactional
    public void abrirRestaurante(final Long id) {
        Restaurante restauranteAtual = buscarOuFalhar(id);
        restauranteAtual.abrirRestaurante();
    }

    @Transactional
    public void fecharRestaurante(final Long id) {
        Restaurante restauranteAtual = buscarOuFalhar(id);
        restauranteAtual.fecharRestaurante();
    }

    private Restaurante buscarOuFalhar(final Long id) {
        return restauranteRepository.findById(id).orElseThrow(
                () -> new EntidadeNaoEncontradaException(
                        String.format(MSG_RESTAURANTE_NAO_ENCONTRADO, id)));
    }

    @Transactional
    private Restaurante salvarRestaurante(final Restaurante restaurante) {
        try {
            Long cozinhaId = restaurante.getCozinha().getId();
            Long cidadeId = restaurante.getEndereco().getCidade().getId();
            Cozinha cozinhaAtual = cozinhaService.buscar(cozinhaId);
            Cidade cidadeAtual = cidadeService.buscar(cidadeId);
            restaurante.setCozinha(cozinhaAtual);
            restaurante.getEndereco().setCidade(cidadeAtual);
            return restauranteRepository.save(restaurante);
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage());
        }
    }
}
