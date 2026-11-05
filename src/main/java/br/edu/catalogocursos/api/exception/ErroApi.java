package br.edu.catalogocursos.api.exception;

import java.time.OffsetDateTime;

public record ErroApi(int status, String message, String path, OffsetDateTime timestamp) {
}
