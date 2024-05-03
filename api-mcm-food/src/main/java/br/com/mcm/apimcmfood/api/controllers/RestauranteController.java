package br.com.mcm.apimcmfood.api.controllers;

import br.com.mcm.apimcmfood.domain.entity.Restaurante;
import br.com.mcm.apimcmfood.application.service.RestauranteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {

    private RestauranteService restauranteService;

    public RestauranteController(
            final RestauranteService restauranteService
    ) {
        this.restauranteService = Objects.requireNonNull(restauranteService);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Restaurante adicionar(@RequestBody Restaurante restaurante) {
        return restauranteService.adicionar(restaurante);
    }

    @GetMapping
    public Page<Restaurante> listar(Pageable pageable) {
        return restauranteService.listar(pageable);
    }

    @GetMapping("/{restauranteId}")
    public ResponseEntity<Restaurante> buscar(@PathVariable("restauranteId") Long id) {
        return ResponseEntity.ok(restauranteService.buscar(id));
    }

    @PutMapping("/{restauranteId}")
    public ResponseEntity<Restaurante> atualizar(
            final @PathVariable("restauranteId") Long id,
            final @RequestBody Restaurante restaurante
    ) {
        return ResponseEntity.ok(restauranteService.atualizar(id, restaurante));
    }

    @DeleteMapping("/{restauranteId}")
    public ResponseEntity<Restaurante> remover(@PathVariable("restauranteId") Long id) {
        this.restauranteService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
