package br.edu.catalogocursos.repository;

import br.edu.catalogocursos.domain.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    @Query("select c from Curso c join fetch c.instrutor where c.id = :id")
    Optional<Curso> buscarComInstrutorPorId(@Param("id") Long id);

    @Query("select c from Curso c join fetch c.instrutor where c.instrutor.id = :instrutorId order by c.titulo asc")
    List<Curso> buscarPorInstrutorOrdenadosPorTitulo(@Param("instrutorId") Long instrutorId);
}
