package br.unipar.frameworks.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @GetMapping("/config")
    public Map<String, String> config() {
        return Map.of(
                "status", "debug protegido",
                "profile", "lab"
        );
    }

    @GetMapping("/error-example")
    public String errorExample() {
        throw new RuntimeException("Erro interno simulado: falha ao consultar tabela");
    }
}
