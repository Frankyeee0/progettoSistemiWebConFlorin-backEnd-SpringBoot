package com.florin.franco.UniHub_sistemiWeb.controllersTest;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("test")
@RestController
public class TestController {

    @GetMapping("/")
    public String home() {
        return "Server attivo! Benvenuto in UniHub!";
    }

    @GetMapping("/api/test/protetta")
    public String testProtetta() {
        return "Accesso riuscito! Sei autenticato!";
    }
}