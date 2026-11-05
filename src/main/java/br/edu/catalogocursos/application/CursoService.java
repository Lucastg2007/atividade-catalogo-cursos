package br.edu.catalogocursos.application;

import br.edu.catalogocursos.api.dto.CursoRequest;
import br.edu.catalogocursos.api.dto.CursoResponse;
import br.edu.catalogocursos.api.dto.CursoResumoResponse;
import br.edu.catalogocursos.api.exception.RecursoNaoEncontradoException;
import br.edu.catalogocursos.api.mapper.CursoMapper;
import br.edu.catalogocursos.domain.Curso;
import br.edu.catalogocursos.domain.Instrutor;
import br.edu.catalogocursos.repository.CursoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;
    private final InstrutorService instrutorService;
    private final CursoMapper cursoMapper;

    public CursoService(CursoRepository cursoRepository, InstrutorService instrutorService, CursoMapper cursoMapper) {
        this.cursoRepository = cursoRepository;
        this.instrutorService = instrutorService;
        this.cursoMapper = cursoMapper;
    }

    @Transactional
    public CursoResponse cadastrar(CursoRequest request) {
        Instrutor instrutor = instrutorService.exigirPorId(request.instrutorId());
        Curso curso = new Curso(request.titulo().trim(), request.cargaHoraria(), request.nivel(), instrutor);
        return cursoMapper.paraResponse(cursoRepository.save(curso));
    }

    @Transactional(readOnly = true)
    public CursoResponse buscarPorId(Long id) {
        Curso curso = cursoRepository.buscarComInstrutorPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Curso não encontrado."));
        return cursoMapper.paraResponse(curso);
    }

    @Transactional(readOnly = true)
    public List<CursoResumoResponse> listarPorInstrutor(Long instrutorId) {
        instrutorService.exigirPorId(instrutorId);
        return cursoRepository.buscarPorInstrutorOrdenadosPorTitulo(instrutorId).stream()
                .map(cursoMapper::paraResumo)
                .toList();
    }
}
