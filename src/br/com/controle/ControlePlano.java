/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.controle;

import br.com.entidade.ManterPlano;
import br.com.modelo.Plano;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

// regras do plano (nome, valor e duração) ficam aqui
public class ControlePlano {

    ManterPlano mp = new ManterPlano();

    public void inserir(Plano p) throws Exception {
        validar(p);
        mp.inserir(p);
    }

    public void alterar(Plano p) throws Exception {
        if (p.getPlanoId() == 0) {
            throw new Exception("Selecione um plano na tabela!");
        }
        validar(p);
        mp.alterar(p);
    }

    public void deletar(Plano p) throws Exception {
        if (p.getPlanoId() == 0) {
            throw new Exception("Selecione um plano na tabela!");
        }
        try {
            mp.deletar(p);
        } catch (SQLException e) {
            // erro 1451: o plano ainda esta em alguma matricula ou beneficio
            if (e.getErrorCode() == 1451) {
                throw new Exception("Não é possível excluir: existem matrículas ou benefícios ligados a este plano!");
            }
            throw e;
        }
    }

    public void buscar(Plano p) throws Exception {
        mp.buscar(p);
    }

    public ArrayList<Plano> listar() throws Exception {
        return mp.listar();
    }

    private void validar(Plano p) throws Exception {
        if (p.getNome().equals("")) {
            throw new Exception("Informe o nome do plano!");
        }
        if (p.getValor() == null || p.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("O valor do plano deve ser maior que zero!");
        }
        if (p.getDuracaoMeses() <= 0) {
            throw new Exception("A duração deve ser maior que zero!");
        }
        // duracao_meses no banco é TINYINT, vai até 127
        if (p.getDuracaoMeses() > 120) { // Limite pra nao estourar o TINYINT
            throw new Exception("A duração pode ter no máximo 120 meses!");
        }
    }
}
