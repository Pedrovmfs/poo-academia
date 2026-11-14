/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.controle;

import br.com.entidade.ManterUnidade;
import br.com.modelo.Unidade;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

// Validações da unidade antes de salvar
public class ControleUnidade {

    ManterUnidade mu = new ManterUnidade();

    public void inserir(Unidade u) throws Exception {
        validar(u);
        mu.inserir(u);
    }

    public void alterar(Unidade u) throws Exception {
        if (u.getUnidadeId() == 0) throw new Exception("Selecione uma unidade na tabela!");
        validar(u);
        mu.alterar(u);
    }

    public void deletar(Unidade u) throws Exception {
        if (u.getUnidadeId() == 0) throw new Exception("Selecione uma unidade na tabela!");
        try {
            mu.deletar(u);
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) throw new Exception("Não é possível excluir: existem registros vinculados a esta unidade!"); // 1451 = erro de chave estrangeira (unidade usada em outra tabela)
            throw e;
        }
    }

    public void buscar(Unidade u) throws Exception {
        mu.buscar(u);
    }

    public ArrayList<Unidade> listar() throws Exception {
        return mu.listar();
    }

    private void validar(Unidade u) throws Exception {
        if (u.getNome().equals("")) throw new Exception("Informe o nome da unidade!");
        if (u.getEndereco().equals("")) throw new Exception("Informe o endereço da unidade!");
        if (u.getCustoMensalOcupacao() != null && u.getCustoMensalOcupacao().compareTo(BigDecimal.ZERO) < 0) throw new Exception("O custo mensal não pode ser negativo!");
    }
}
