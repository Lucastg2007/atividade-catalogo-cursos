package br.edu.catalogocursos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "instrutor", uniqueConstraints =
        @UniqueConstraint(name = "uk_instrutor_email", columnNames = "email"))
public class Instrutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 160)
    private String email;

    @OneToMany(mappedBy = "instrutor", fetch = FetchType.LAZY)
    private List<Curso> cursos = new ArrayList<>();

    protected Instrutor() {
    }

    public Instrutor(String nome, String email) {
        this.nome = Objects.requireNonNull(nome, "nome é obrigatório");
        this.email = Objects.requireNonNull(email, "email é obrigatório");
    }

    public void adicionarCurso(Curso curso) {
        Objects.requireNonNull(curso, "curso não pode ser nulo");
        if (curso.getInstrutor() != null && curso.getInstrutor() != this) {
            throw new IllegalStateException("O curso já pertence a outro instrutor.");
        }
        if (!cursos.contains(curso)) {
            cursos.add(curso);
        }
        curso.definirInstrutor(this);
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public List<Curso> getCursos() {
        return Collections.unmodifiableList(cursos);
    }
}
