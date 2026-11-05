package br.edu.catalogocursos.api.controller;

import br.edu.catalogocursos.api.dto.CursoResumoResponse;
import br.edu.catalogocursos.api.dto.InstrutorRequest;
import br.edu.catalogocursos.api.dto.InstrutorResponse;
import br.edu.catalogocursos.application.CursoService;
import br.edu.catalogocursos.application.InstrutorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/instrutores")
public class InstrutorController {

    private final InstrutorService instrutorService;
    private final CursoService cursoService;

    public InstrutorController(InstrutorService instrutorService, CursoService cursoService) {
        this.instrutorService = instrutorService;
        this.cursoService = cursoService;
    }

    @PostMapping
    public ResponseEntity<InstrutorResponse> cadastrar(@Valid @RequestBody InstrutorRequest request) {
        InstrutorResponse resposta = instrutorService.cadastrar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/instrutores/{id}")
                .buildAndExpand(resposta.id())
                .toUri();
        return ResponseEntity.created(location).body(resposta);
    }

    @GetMapping("/{id}")
    public InstrutorResponse buscar(@PathVariable Long id) {
        return instrutorService.buscarPorId(id);
    }

    @GetMapping("/{id}/cursos")
    public List<CursoResumoResponse> listarCursos(@PathVariable Long id) {
        return cursoService.listarPorInstrutor(id);
    }
}
