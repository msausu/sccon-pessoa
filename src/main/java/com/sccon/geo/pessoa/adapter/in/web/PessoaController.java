package com.sccon.geo.pessoa.adapter.in.web;

import com.sccon.geo.pessoa.adapter.in.web.dto.SalaryResponse;
import com.sccon.geo.pessoa.application.command.AdmitirPessoaCommand;
import com.sccon.geo.pessoa.application.command.AtualizarPessoaCommand;
import com.sccon.geo.pessoa.application.command.PatchPessoaCommand;
import com.sccon.geo.pessoa.application.result.AgeOutput;
import com.sccon.geo.pessoa.application.result.AgeResult;
import com.sccon.geo.pessoa.application.result.SalaryOutput;
import com.sccon.geo.pessoa.application.result.SalaryResult;
import com.sccon.geo.pessoa.domain.model.Pessoa;

import com.sccon.geo.pessoa.domain.port.in.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import java.util.List;

@RestController
@RequestMapping("/person")
public class PessoaController {

    private final AdmitirPessoaUseCase admitir;
    private final AtualizarPessoaUseCase atualizar;
    private final BuscarPessoaUseCase buscar;
    private final ConsultarSalarioUseCase consultarSalarioUseCase;
    private final ConsultarIdadeUseCase consultarIdadeUseCase;
    private final RemoverPessoaUseCase removerPessoaUseCase;

    public PessoaController(
            AdmitirPessoaUseCase admitir,
            AtualizarPessoaUseCase atualizar,
            BuscarPessoaUseCase buscar,
            RemoverPessoaUseCase removerPessoaUseCase,
            ConsultarSalarioUseCase consultarSalarioUseCase,
            ConsultarIdadeUseCase consultarIdadeUseCase) {
        this.admitir = admitir;
        this.atualizar = atualizar;
        this.buscar = buscar;
        this.removerPessoaUseCase = removerPessoaUseCase;
        this.consultarIdadeUseCase = consultarIdadeUseCase;
        this.consultarSalarioUseCase = consultarSalarioUseCase;
    }

    @PostMapping
    public Pessoa admitir(@Valid @RequestBody AdmitirPessoaCommand cmd) {
        return admitir.admitir(cmd);
    }

    @PutMapping("/{id}")
    public Pessoa atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarPessoaCommand cmd) {

        return atualizar.atualizarDados(id, cmd);
    }

    @PatchMapping("/{id}")
    public Pessoa patch(
            @PathVariable Long id,
            @RequestBody PatchPessoaCommand cmd) {

        return atualizar.patch(id, cmd);
    }

    @GetMapping
    public List<Pessoa> listar() {
        return buscar.listarOrdenadoPorNome();
    }

    @GetMapping("/{id}")
    public Pessoa buscar(@PathVariable Long id) {
        return buscar.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        removerPessoaUseCase.remover(id);
    }

    @GetMapping(value = "/{id}/age", produces = APPLICATION_JSON_VALUE)
    public AgeResult idadeJson(
            @PathVariable Long id,
            @RequestParam(defaultValue = "DAYS") AgeOutput output) {

        return consultarIdadeUseCase.consultar(id, output);
    }

    @GetMapping(value = "/{id}/age", produces = TEXT_PLAIN_VALUE)
    public String idadePlain(
            @PathVariable Long id,
            @RequestParam(defaultValue = "DAYS") AgeOutput output) {

        AgeResult result = consultarIdadeUseCase.consultar(id, output);
        return String.valueOf(result.value());
    }

    @GetMapping(value = "/{id}/salary", produces = APPLICATION_JSON_VALUE)
    public SalaryResponse salarioJson(
            @PathVariable Long id,
            @RequestParam(defaultValue = "FULL") SalaryOutput output) {

        SalaryResult result = consultarSalarioUseCase.consultar(id, output);

        return SalaryResponse.from(result);
    }

    @GetMapping(value = "/{id}/salary", produces = TEXT_PLAIN_VALUE)
    public String salarioPlain(
            @PathVariable Long id,
            @RequestParam(defaultValue = "FULL") SalaryOutput output) {

        SalaryResult result = consultarSalarioUseCase.consultar(id, output);

        return SalaryResponse.getValorFormatted(result);
    }
}