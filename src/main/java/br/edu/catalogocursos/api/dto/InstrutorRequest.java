package br.edu.catalogocursos.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record InstrutorRequest(
        @NotBlank(message = "nome é obrigatório")
        @Size(min = 3, max = 120, message = "nome deve ter entre 3 e 120 caracteres")
        String nome,

        @NotBlank(message = "email é obrigatório")
        @Email(message = "email deve ser válido")
        @Size(max = 160, message = "email deve ter no máximo 160 caracteres")
        String email
) {
}
