/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.modelo;

import java.math.BigDecimal;

public class Plano {
   private int planoId;
   private String nome;
   private BigDecimal valor;
   private int duracaoMeses;
   private boolean acessoMultiunidade;

    public int getPlanoId() {
        return planoId;
    }

    public void setPlanoId(int planoId) {
        this.planoId = planoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public int getDuracaoMeses() {
        return duracaoMeses;
    }

    public void setDuracaoMeses(int duracaoMeses) {
        this.duracaoMeses = duracaoMeses;
    }

    public boolean isAcessoMultiunidade() {
        return acessoMultiunidade;
    }

    public void setAcessoMultiunidade(boolean acessoMultiunidade) {
        this.acessoMultiunidade = acessoMultiunidade;
    }

}
