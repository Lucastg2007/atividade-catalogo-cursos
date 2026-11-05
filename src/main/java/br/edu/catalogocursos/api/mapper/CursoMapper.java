package br.edu.catalogocursos.api.mapper;

import br.edu.catalogocursos.api.dto.CursoResponse;
import br.edu.catalogocursos.api.dto.CursoResumoResponse;
import br.edu.catalogocursos.api.dto.InstrutorResumoResponse;
import br.edu.catalogocursos.domain.Curso;
import org.springframework.stereotype.Component;

@Component
public class CursoMapper {

    public CursoResponse paraResponse(Curso curso) {
        InstrutorResumoResponse instrutor = new InstrutorResumoResponse(
                curso.getInstrutor().getId(), curso.getInstrutor().getNome());
        return new CursoResponse(curso.getId(), curso.getTitulo(), curso.getCargaHoraria(), curso.getNivel(), instrutor);
    }

    public CursoResumoResponse paraResumo(Curso curso) {
        return new CursoResumoResponse(curso.getId(), curso.getTitulo(), curso.getCargaHoraria(), curso.getNivel());
    }
}
