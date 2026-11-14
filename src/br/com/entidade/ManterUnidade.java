/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.entidade;

import br.com.modelo.Unidade;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;

public class ManterUnidade extends DAO {

    public void inserir(Unidade u) throws Exception {
        abrirBanco();
        String query = "INSERT INTO unidade (unidade_id, nome, endereco, telefone, horario_funcionamento, data_inauguracao, custo_mensal_ocupacao) values(null,?,?,?,?,?,?)"; // Null no id porque o banco gera sozinho (AUTO_INCREMENT)
        pst = (PreparedStatement) con.prepareStatement(query);
        pst.setString(1, u.getNome());
        pst.setString(2, u.getEndereco());
        pst.setString(3, u.getTelefone());
        pst.setString(4, u.getHorarioFuncionamento());
        if (u.getDataInauguracao() == null) {
            pst.setNull(5, Types.DATE); // Campo vazio vai como NULL pro banco
        } else {
            pst.setDate(5, java.sql.Date.valueOf(u.getDataInauguracao()));
        }
        if (u.getCustoMensalOcupacao() == null) {
            pst.setNull(6, Types.DECIMAL);
        } else {
            pst.setBigDecimal(6, u.getCustoMensalOcupacao());
        }
        pst.execute();
        fecharBanco();
    }

    public void alterar(Unidade u) throws Exception {
        abrirBanco();
        String query = "UPDATE unidade set nome = ?, endereco = ?, telefone = ?, horario_funcionamento = ?, data_inauguracao = ?, custo_mensal_ocupacao = ? where unidade_id = ?";
        pst = (PreparedStatement) con.prepareStatement(query);
        pst.setString(1, u.getNome());
        pst.setString(2, u.getEndereco());
        pst.setString(3, u.getTelefone());
        pst.setString(4, u.getHorarioFuncionamento());
        if (u.getDataInauguracao() == null) {
            pst.setNull(5, Types.DATE);
        } else {
            pst.setDate(5, java.sql.Date.valueOf(u.getDataInauguracao()));
        }
        if (u.getCustoMensalOcupacao() == null) {
            pst.setNull(6, Types.DECIMAL);
        } else {
            pst.setBigDecimal(6, u.getCustoMensalOcupacao());
        }
        pst.setInt(7, u.getUnidadeId());
        pst.executeUpdate();
        fecharBanco();
    }

    public void deletar(Unidade u) throws Exception {
        abrirBanco();
        String query = "delete from unidade where unidade_id = ?";
        pst = (PreparedStatement) con.prepareStatement(query);
        pst.setInt(1, u.getUnidadeId());
        pst.execute();
        fecharBanco();
    }

    public void buscar(Unidade u) throws Exception {
        abrirBanco();
        String query = "select unidade_id, nome, endereco, telefone, horario_funcionamento, data_inauguracao, custo_mensal_ocupacao FROM unidade where unidade_id = ?";
        pst = (PreparedStatement) con.prepareStatement(query);
        pst.setInt(1, u.getUnidadeId());
        ResultSet tr = pst.executeQuery();
        if (tr.next()) {
            u.setNome(tr.getString("nome"));
            u.setEndereco(tr.getString("endereco"));
            u.setTelefone(tr.getString("telefone"));
            u.setHorarioFuncionamento(tr.getString("horario_funcionamento"));
            if (tr.getDate("data_inauguracao") != null) {
                u.setDataInauguracao(tr.getDate("data_inauguracao").toLocalDate());
            }
            u.setCustoMensalOcupacao(tr.getBigDecimal("custo_mensal_ocupacao"));
        }
        fecharBanco();
    }

    public ArrayList<Unidade> listar() throws Exception {
        ArrayList<Unidade> unidades = new ArrayList<Unidade>();
        abrirBanco();
        String query = "select unidade_id, nome, endereco, telefone, horario_funcionamento, data_inauguracao, custo_mensal_ocupacao FROM unidade order by unidade_id desc";
        pst = (PreparedStatement) con.prepareStatement(query);
        ResultSet tr = pst.executeQuery();
        Unidade u;
        while (tr.next()) {
            u = new Unidade();
            u.setUnidadeId(tr.getInt("unidade_id"));
            u.setNome(tr.getString("nome"));
            u.setEndereco(tr.getString("endereco"));
            u.setTelefone(tr.getString("telefone"));
            u.setHorarioFuncionamento(tr.getString("horario_funcionamento"));
            if (tr.getDate("data_inauguracao") != null) {
                u.setDataInauguracao(tr.getDate("data_inauguracao").toLocalDate());
            }
            u.setCustoMensalOcupacao(tr.getBigDecimal("custo_mensal_ocupacao"));
            unidades.add(u);
        }
        fecharBanco();
        return unidades;
    }
}
