/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.controle;

import br.com.entidade.ManterRelatorio;
import br.com.modelo.ItemCombo;
import br.com.modelo.MatriculaDetalhada;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

// Controle da consulta e do relatorio: valida o periodo e calcula a soma
public class ControleRelatorio {

    ManterRelatorio mr = new ManterRelatorio();

    public ArrayList<MatriculaDetalhada> consultar(String nome) throws Exception {
        return mr.consultar(nome);
    }

    public ArrayList<MatriculaDetalhada> gerarRelatorio(LocalDate inicio, LocalDate fim, int unidadeId, String status) throws Exception {
        if (inicio == null || fim == null) {
            throw new Exception("Informe a data inicial e a data final!");
        }
        if (inicio.isAfter(fim)) { // Verifica se a data inicial vem depois da final
            throw new Exception("A data inicial não pode ser depois da data final!");
        }
        return mr.listarPorPeriodo(inicio, fim, unidadeId, status);
    }

    // soma o valor do plano de cada matricula da lista
    public BigDecimal somarValores(ArrayList<MatriculaDetalhada> lista) {
        BigDecimal soma = BigDecimal.ZERO;
        for (int i = 0; i < lista.size(); i++) {
            soma = soma.add(lista.get(i).getValor()); // BigDecimal nao usa o +, precisa do add
        }
        return soma;
    }

    public ArrayList<ItemCombo> listarUnidades() throws Exception {
        return mr.listarUnidades();
    }
}
