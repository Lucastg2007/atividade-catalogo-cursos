package br.edu.catalogocursos.api.dto;

import br.edu.catalogocursos.domain.NivelCurso;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CursoRequest(
        @NotBlank(message = "título é obrigatório")
        @Size(min = 3, max = 150, message = "título deve ter entre 3 e 150 caracteres")
        String titulo,

        @NotNull(message = "cargaHoraria é obrigatória")
        @Min(value = 1, message = "cargaHoraria deve ser de pelo menos 1 hora")
        @Max(value = 400, message = "cargaHoraria deve ser de no máximo 400 horas")
        Integer cargaHoraria,

        @NotNull(message = "nível é obrigatório")
        NivelCurso nivel,

        @NotNull(message = "instrutorId é obrigatório")
        @Positive(message = "instrutorId deve ser positivo")
        Long instrutorId
) {
}
