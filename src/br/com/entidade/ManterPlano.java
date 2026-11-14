/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.entidade;

import br.com.modelo.Plano;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ManterPlano extends DAO {

    public void inserir(Plano p) throws Exception {
        abrirBanco();
        String query = "INSERT INTO plano (plano_id, nome, valor, duracao_meses, acesso_multiunidade) values(null,?,?,?,?)";
        pst = (PreparedStatement) con.prepareStatement(query);
        pst.setString(1, p.getNome());
        pst.setBigDecimal(2, p.getValor());
        pst.setInt(3, p.getDuracaoMeses());
        pst.setBoolean(4, p.isAcessoMultiunidade()); // No banco é TINYINT(1), o setBoolean grava 1 ou 0
        pst.execute();
        fecharBanco();
    }

    public void alterar(Plano p) throws Exception {
        abrirBanco();
        String query = "UPDATE plano set nome = ?, valor = ?, duracao_meses = ?, acesso_multiunidade = ? where plano_id = ?";
        pst = (PreparedStatement) con.prepareStatement(query);
        pst.setString(1, p.getNome());
        pst.setBigDecimal(2, p.getValor());
        pst.setInt(3, p.getDuracaoMeses());
        pst.setBoolean(4, p.isAcessoMultiunidade());
        pst.setInt(5, p.getPlanoId());
        pst.executeUpdate();
        fecharBanco();
    }

    public void deletar(Plano p) throws Exception {
        abrirBanco();
        String query = "delete from plano where plano_id = ?";
        pst = (PreparedStatement) con.prepareStatement(query);
        pst.setInt(1, p.getPlanoId());
        pst.execute();
        fecharBanco();
    }

    public void buscar(Plano p) throws Exception {
        abrirBanco();
        String query = "select plano_id, nome, valor, duracao_meses, acesso_multiunidade FROM plano where plano_id = ?";
        pst = (PreparedStatement) con.prepareStatement(query);
        pst.setInt(1, p.getPlanoId());
        ResultSet tr = pst.executeQuery();
        if (tr.next()) {
            p.setNome(tr.getString("nome"));
            p.setValor(tr.getBigDecimal("valor"));
            p.setDuracaoMeses(tr.getInt("duracao_meses"));
            p.setAcessoMultiunidade(tr.getBoolean("acesso_multiunidade"));
        }
        fecharBanco();
    }

    public ArrayList<Plano> listar() throws Exception {
        ArrayList<Plano> planos = new ArrayList<Plano>();
        abrirBanco();
        String query = "select plano_id, nome, valor, duracao_meses, acesso_multiunidade FROM plano order by plano_id desc";
        pst = (PreparedStatement) con.prepareStatement(query);
        ResultSet tr = pst.executeQuery();
        Plano p;
        while (tr.next()) {
            p = new Plano();
            p.setPlanoId(tr.getInt("plano_id"));
            p.setNome(tr.getString("nome"));
            p.setValor(tr.getBigDecimal("valor"));
            p.setDuracaoMeses(tr.getInt("duracao_meses"));
            p.setAcessoMultiunidade(tr.getBoolean("acesso_multiunidade"));
            planos.add(p);
        }
        fecharBanco();
        return planos;
    }
}
