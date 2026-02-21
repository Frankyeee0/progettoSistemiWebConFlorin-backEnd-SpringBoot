package com.florin.franco.UniHub_sistemiWeb.dto;

public class MateriaDto {
    private Long id;
    private String nome;
    private String codice;
    private String corsoDiStudi;
    private Long dipartimentoId;

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

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getCorsoDiStudi() {
        return corsoDiStudi;
    }

    public void setCorsoDiStudi(String corsoDiStudi) {
        this.corsoDiStudi = corsoDiStudi;
    }

    public Long getDipartimentoId() {
        return dipartimentoId;
    }

    public void setDipartimentoId(Long dipartimentoId) {
        this.dipartimentoId = dipartimentoId;
    }
}
