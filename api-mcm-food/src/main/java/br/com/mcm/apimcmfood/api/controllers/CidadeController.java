package br.com.mcm.apimcmfood.api.controllers;

import br.com.mcm.apimcmfood.application.service.CidadeService;
import br.com.mcm.apimcmfood.domain.entity.Cidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping(value = "/cidades")
public class CidadeController {

    private CidadeService cidadeService;

    public CidadeController(final CidadeService cidadeService) {
        this.cidadeService = Objects.requireNonNull(cidadeService);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cidade adicionar(final @RequestBody Cidade cidade) {
        return cidadeService.adicionar(cidade);
    }

    @GetMapping("/{cidadeId}")
    public ResponseEntity<Cidade> buscar(@PathVariable("cidadeId") Long id) {
        return ResponseEntity.ok(cidadeService.buscar(id));
    }

    @GetMapping
    public Page<Cidade> listar(Pageable pageable) {
        return cidadeService.listar(pageable);
    }

    @PutMapping("/{cidadeId}")
    public ResponseEntity<Cidade> atualizar(
            final @PathVariable("cidadeId") Long id,
            final @RequestBody Cidade cidade
    ) {
        return ResponseEntity.ok(cidadeService.atualizar(id, cidade));
    }

    @DeleteMapping("/{cidadeId}")
    public ResponseEntity<Cidade> remover(final @PathVariable("cidadeId") Long id) {
        this.cidadeService.remover(id);
        return ResponseEntity.noContent().build();
    }
}