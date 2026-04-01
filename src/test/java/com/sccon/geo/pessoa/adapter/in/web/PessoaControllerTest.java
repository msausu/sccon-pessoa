package com.sccon.geo.pessoa.adapter.in.web;

import com.sccon.geo.pessoa.adapter.in.web.config.SecurityConfig;
import com.sccon.geo.pessoa.application.command.AdmitirPessoaCommand;
import com.sccon.geo.pessoa.application.command.AtualizarPessoaCommand;
import com.sccon.geo.pessoa.application.command.PatchPessoaCommand;
import com.sccon.geo.pessoa.application.result.AgeOutput;
import com.sccon.geo.pessoa.application.result.AgeResult;
import com.sccon.geo.pessoa.application.result.SalaryOutput;
import com.sccon.geo.pessoa.application.result.SalaryResult;
import com.sccon.geo.pessoa.domain.model.Pessoa;
import com.sccon.geo.pessoa.domain.port.in.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PessoaController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class PessoaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdmitirPessoaUseCase admitir;

    @MockitoBean
    private AtualizarPessoaUseCase atualizar;

    @MockitoBean
    private BuscarPessoaUseCase buscar;

    @MockitoBean
    private RemoverPessoaUseCase removerPessoaUseCase;

    @MockitoBean
    private ConsultarSalarioUseCase consultarSalarioUseCase;

    @MockitoBean
    private ConsultarIdadeUseCase consultarIdadeUseCase;

    @Test
    @DisplayName("POST /person should admit a person")
    void shouldAdmitPerson() throws Exception {
        Pessoa pessoa = new Pessoa(1L, "João", LocalDate.of(1990, 1, 1), LocalDate.of(2023, 1, 1));
        when(admitir.admitir(any(AdmitirPessoaCommand.class))).thenReturn(pessoa);

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"João\",\"dataNascimento\":\"1990-01-01\",\"dataAdmissao\":\"2023-01-01\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João"));
    }

    @Test
    @DisplayName("PUT /person/{id} should update a person")
    void shouldUpdatePerson() throws Exception {
        Pessoa pessoa = new Pessoa(1L, "João Silva", LocalDate.of(1990, 1, 1), LocalDate.of(2023, 1, 1));
        when(atualizar.atualizarDados(eq(1L), any(AtualizarPessoaCommand.class))).thenReturn(pessoa);

        mockMvc.perform(put("/person/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"João Silva\",\"dataNascimento\":\"1990-01-01\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    @DisplayName("PATCH /person/{id} should patch a person")
    void shouldPatchPerson() throws Exception {
        Pessoa pessoa = new Pessoa(1L, "João Patched", LocalDate.of(1990, 1, 1), LocalDate.of(2023, 1, 1));
        when(atualizar.patch(eq(1L), any(PatchPessoaCommand.class))).thenReturn(pessoa);

        mockMvc.perform(patch("/person/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"João Patched\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Patched"));
    }

    @Test
    @DisplayName("GET /person should list all persons")
    void shouldListPersons() throws Exception {
        Pessoa p1 = new Pessoa(1L, "Ana", LocalDate.of(1990, 1, 1), LocalDate.of(2023, 1, 1));
        Pessoa p2 = new Pessoa(2L, "Beto", LocalDate.of(1990, 1, 1), LocalDate.of(2023, 1, 1));
        when(buscar.listarOrdenadoPorNome()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/person"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Ana"))
                .andExpect(jsonPath("$[1].nome").value("Beto"));
    }

    @Test
    @DisplayName("GET /person/{id} should return person by id")
    void shouldGetPersonById() throws Exception {
        Pessoa pessoa = new Pessoa(1L, "João", LocalDate.of(1990, 1, 1), LocalDate.of(2023, 1, 1));
        when(buscar.buscarPorId(1L)).thenReturn(pessoa);

        mockMvc.perform(get("/person/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João"));
    }

    @Test
    @DisplayName("DELETE /person/{id} should remove a person")
    void shouldRemovePerson() throws Exception {
        mockMvc.perform(delete("/person/1"))
                .andExpect(status().isNoContent());

        verify(removerPessoaUseCase).remover(1L);
    }

    @Test
    @DisplayName("GET /person/{id}/salary should return salary as JSON")
    void shouldGetSalaryJson() throws Exception {
        SalaryResult result = new SalaryResult(1L, new BigDecimal("2000.00"), SalaryOutput.FULL);
        when(consultarSalarioUseCase.consultar(eq(1L), eq(SalaryOutput.FULL))).thenReturn(result);

        mockMvc.perform(get("/person/1/salary")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pessoaId").value(1))
                .andExpect(jsonPath("$.valor").value(2000.00));
    }

    @Test
    @DisplayName("GET /person/{id}/salary should return formatted salary as text")
    void shouldGetSalaryPlain() throws Exception {
        SalaryResult result = new SalaryResult(1L, new BigDecimal("2500.50"), SalaryOutput.FULL);
        when(consultarSalarioUseCase.consultar(eq(1L), eq(SalaryOutput.FULL))).thenReturn(result);

        mockMvc.perform(get("/person/1/salary")
                        .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("2.500,50"));
    }

    @Test
    @DisplayName("GET /person/{id}/age should return age as JSON")
    void shouldGetAgeJson() throws Exception {
        AgeResult result = new AgeResult(1L, 3650L, AgeOutput.DAYS);
        when(consultarIdadeUseCase.consultar(eq(1L), eq(AgeOutput.DAYS))).thenReturn(result);

        mockMvc.perform(get("/person/1/age")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.value").value(3650));
    }
}