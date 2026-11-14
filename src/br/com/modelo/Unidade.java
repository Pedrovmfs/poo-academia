/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Unidade {
   private int unidadeId;
   private String nome;
   private String endereco;
   private String telefone;
   private String horarioFuncionamento;
   private LocalDate dataInauguracao;
   private BigDecimal custoMensalOcupacao;

    public int getUnidadeId() {
        return unidadeId;
    }

    public void setUnidadeId(int unidadeId) {
        this.unidadeId = unidadeId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getHorarioFuncionamento() {
        return horarioFuncionamento;
    }

    public void setHorarioFuncionamento(String horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento;
    }

    public LocalDate getDataInauguracao() {
        return dataInauguracao;
    }

    public void setDataInauguracao(LocalDate dataInauguracao) {
        this.dataInauguracao = dataInauguracao;
    }

    public BigDecimal getCustoMensalOcupacao() {
        return custoMensalOcupacao;
    }

    public void setCustoMensalOcupacao(BigDecimal custoMensalOcupacao) {
        this.custoMensalOcupacao = custoMensalOcupacao;
    }

}
