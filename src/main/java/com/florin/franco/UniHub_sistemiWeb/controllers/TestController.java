package com.florin.franco.UniHub_sistemiWeb.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String home() {
        return "🚀 Server attivo! Benvenuto in UniHub!";
    }

    @GetMapping("/api/test/protetta")
    public String testProtetta() {
        return "🔒 Accesso riuscito! Sei autenticato!";
    }
}