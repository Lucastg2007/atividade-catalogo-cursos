package br.edu.catalogocursos.application;

import br.edu.catalogocursos.api.dto.InstrutorRequest;
import br.edu.catalogocursos.api.dto.InstrutorResponse;
import br.edu.catalogocursos.api.exception.ConflitoDeCadastroException;
import br.edu.catalogocursos.api.exception.RecursoNaoEncontradoException;
import br.edu.catalogocursos.api.mapper.InstrutorMapper;
import br.edu.catalogocursos.domain.Instrutor;
import br.edu.catalogocursos.repository.InstrutorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class InstrutorService {

    private final InstrutorRepository instrutorRepository;
    private final InstrutorMapper instrutorMapper;

    public InstrutorService(InstrutorRepository instrutorRepository, InstrutorMapper instrutorMapper) {
        this.instrutorRepository = instrutorRepository;
        this.instrutorMapper = instrutorMapper;
    }

    @Transactional
    public InstrutorResponse cadastrar(InstrutorRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if (instrutorRepository.existsByEmailIgnoreCase(email)) {
            throw new ConflitoDeCadastroException("Já existe um instrutor com este e-mail.");
        }
        Instrutor instrutor = new Instrutor(request.nome().trim(), email);
        return instrutorMapper.paraResponse(instrutorRepository.save(instrutor));
    }

    @Transactional(readOnly = true)
    public InstrutorResponse buscarPorId(Long id) {
        return instrutorRepository.findById(id)
                .map(instrutorMapper::paraResponse)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Instrutor não encontrado."));
    }

    @Transactional(readOnly = true)
    public Instrutor exigirPorId(Long id) {
        return instrutorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Instrutor não encontrado."));
    }
}
