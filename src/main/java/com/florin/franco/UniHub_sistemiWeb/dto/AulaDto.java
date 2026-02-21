package com.florin.franco.UniHub_sistemiWeb.dto;

public class AulaDto {
    private Long id;
    private String nome;
    private String edificio;
    private Integer capienza;
    private Long universitaId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public Integer getCapienza() {
        return capienza;
    }

    public void setCapienza(Integer capienza) {
        this.capienza = capienza;
    }

    public Long getUniversitaId() {
        return universitaId;
    }

    public void setUniversitaId(Long universitaId) {
        this.universitaId = universitaId;
    }
}
