package br.edu.catalogocursos.api;

import br.edu.catalogocursos.domain.Curso;
import br.edu.catalogocursos.domain.Instrutor;
import br.edu.catalogocursos.domain.NivelCurso;
import br.edu.catalogocursos.repository.CursoRepository;
import br.edu.catalogocursos.repository.InstrutorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CatalogoApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InstrutorRepository instrutorRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Test
    void cadastroDeInstrutorValidoRetorna201ELocation() throws Exception {
        mockMvc.perform(post("/api/instrutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Marina Costa","email":"marina.costa@example.com"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/instrutores/")))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.cursos").doesNotExist());
    }

    @Test
    void instrutorInvalidoRetorna400ComErroEstruturado() throws Exception {
        mockMvc.perform(post("/api/instrutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"M","email":"email-invalido"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(containsString("email")))
                .andExpect(jsonPath("$.path").value("/api/instrutores"));
    }

    @Test
    void cadastroDeCursoComInstrutorInexistenteRetorna404() throws Exception {
        mockMvc.perform(post("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo":"Spring Boot","cargaHoraria":40,"nivel":"INTERMEDIARIO","instrutorId":9999}
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(containsString("Instrutor")))
                .andExpect(jsonPath("$.path").value("/api/cursos"));
    }

    @Test
    void emailDuplicadoIgnorandoMaiusculasRetorna409() throws Exception {
        instrutorRepository.saveAndFlush(new Instrutor("Marina Costa", "marina@example.com"));

        mockMvc.perform(post("/api/instrutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Outra Marina","email":"MARINA@EXAMPLE.COM"}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void listagemFiltraInstrutorEOrdenaPorTituloSemRecursaoJson() throws Exception {
        Instrutor instrutor = instrutorRepository.saveAndFlush(
                new Instrutor("Marina Costa", "marina@example.com"));
        Instrutor outro = instrutorRepository.saveAndFlush(
                new Instrutor("João Lima", "joao@example.com"));
        cursoRepository.save(new Curso("Zoologia", 50, NivelCurso.AVANCADO, instrutor));
        cursoRepository.save(new Curso("APIs REST", 40, NivelCurso.INTERMEDIARIO, instrutor));
        cursoRepository.save(new Curso("Algoritmos", 60, NivelCurso.INICIANTE, outro));
        cursoRepository.flush();

        mockMvc.perform(get("/api/instrutores/{id}/cursos", instrutor.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("APIs REST"))
                .andExpect(jsonPath("$[1].titulo").value("Zoologia"))
                .andExpect(jsonPath("$[2]").doesNotExist())
                .andExpect(jsonPath("$[0].instrutor").doesNotExist());
    }
}
