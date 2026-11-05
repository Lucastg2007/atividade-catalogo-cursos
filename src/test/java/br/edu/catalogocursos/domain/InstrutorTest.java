package br.edu.catalogocursos.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InstrutorTest {

    @Test
    void adicionaCursoMantendoOsDoisLadosEImpedindoDuplicidadeDaMesmaInstancia() {
        Instrutor instrutor = new Instrutor("Marina Costa", "marina@example.com");
        Curso curso = new Curso("Spring Boot", 40, NivelCurso.INTERMEDIARIO, instrutor);

        instrutor.adicionarCurso(curso);
        instrutor.adicionarCurso(curso);

        assertEquals(1, instrutor.getCursos().size());
        assertSame(instrutor, curso.getInstrutor());
        assertSame(curso, instrutor.getCursos().getFirst());
    }

    @Test
    void rejeitaCursoNulo() {
        Instrutor instrutor = new Instrutor("Marina Costa", "marina@example.com");

        assertThrows(NullPointerException.class, () -> instrutor.adicionarCurso(null));
    }
}
