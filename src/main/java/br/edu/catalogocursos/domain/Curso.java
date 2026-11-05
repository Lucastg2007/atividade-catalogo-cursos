package br.edu.catalogocursos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "curso")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(name = "carga_horaria", nullable = false)
    private Integer cargaHoraria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NivelCurso nivel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "instrutor_id", nullable = false)
    private Instrutor instrutor;

    protected Curso() {
    }

    public Curso(String titulo, Integer cargaHoraria, NivelCurso nivel, Instrutor instrutor) {
        this.titulo = Objects.requireNonNull(titulo, "título é obrigatório");
        this.cargaHoraria = Objects.requireNonNull(cargaHoraria, "carga horária é obrigatória");
        this.nivel = Objects.requireNonNull(nivel, "nível é obrigatório");
        Objects.requireNonNull(instrutor, "instrutor é obrigatório").adicionarCurso(this);
    }

    void definirInstrutor(Instrutor instrutor) {
        this.instrutor = Objects.requireNonNull(instrutor, "instrutor é obrigatório");
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Integer getCargaHoraria() {
        return cargaHoraria;
    }

    public NivelCurso getNivel() {
        return nivel;
    }

    public Instrutor getInstrutor() {
        return instrutor;
    }
}
