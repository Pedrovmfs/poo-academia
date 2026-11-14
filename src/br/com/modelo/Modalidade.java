/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.modelo;

public class Modalidade {
   private int modalidadeId;
   private String nome;
   private String categoria;
   private String ambienteNecessario;
   private String experienciaMinima;
   private Integer duracaoLimite; // Integer e nao int, pois int nao aceita null

    public int getModalidadeId() {
        return modalidadeId;
    }

    public void setModalidadeId(int modalidadeId) {
        this.modalidadeId = modalidadeId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getAmbienteNecessario() {
        return ambienteNecessario;
    }

    public void setAmbienteNecessario(String ambienteNecessario) {
        this.ambienteNecessario = ambienteNecessario;
    }

    public String getExperienciaMinima() {
        return experienciaMinima;
    }

    public void setExperienciaMinima(String experienciaMinima) {
        this.experienciaMinima = experienciaMinima;
    }

    public Integer getDuracaoLimite() {
        return duracaoLimite;
    }

    public void setDuracaoLimite(Integer duracaoLimite) {
        this.duracaoLimite = duracaoLimite;
    }

}
