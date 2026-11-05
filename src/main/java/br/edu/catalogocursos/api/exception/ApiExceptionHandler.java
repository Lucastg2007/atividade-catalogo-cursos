package br.edu.catalogocursos.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroApi> tratarNaoEncontrado(
            RecursoNaoEncontradoException exception, HttpServletRequest request) {
        return criarResposta(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(ConflitoDeCadastroException.class)
    public ResponseEntity<ErroApi> tratarConflito(
            ConflitoDeCadastroException exception, HttpServletRequest request) {
        return criarResposta(HttpStatus.CONFLICT, exception.getMessage(), request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroApi> tratarIntegridade(HttpServletRequest request) {
        return criarResposta(HttpStatus.CONFLICT,
                "O cadastro viola uma restrição de integridade.", request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroApi> tratarValidacao(
            MethodArgumentNotValidException exception, HttpServletRequest request) {
        String mensagem = exception.getBindingResult().getFieldErrors().stream()
                .sorted(Comparator.comparing(FieldError::getField))
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining("; "));
        return criarResposta(HttpStatus.BAD_REQUEST, mensagem, request);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, ConstraintViolationException.class,
            MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErroApi> tratarCorpoInvalido(Exception exception, HttpServletRequest request) {
        return criarResposta(HttpStatus.BAD_REQUEST,
                "O corpo da requisição é inválido ou contém um valor fora do formato esperado.", request);
    }

    private ResponseEntity<ErroApi> criarResposta(
            HttpStatus status, String mensagem, HttpServletRequest request) {
        ErroApi erro = new ErroApi(status.value(), mensagem, request.getRequestURI(), OffsetDateTime.now());
        return ResponseEntity.status(status).body(erro);
    }
}
