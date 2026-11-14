/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.entidade;

import br.com.modelo.Modalidade;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;

public class ManterModalidade extends DAO {

    public void inserir(Modalidade m) throws Exception {
        abrirBanco();
        String query = "INSERT INTO modalidade (modalidade_id, nome, categoria, ambiente_necessario, experiencia_minima, duracao_limite) values(null,?,?,?,?,?)";
        pst = (PreparedStatement) con.prepareStatement(query);
        pst.setString(1, m.getNome());
        pst.setString(2, m.getCategoria());
        pst.setString(3, m.getAmbienteNecessario());
        pst.setString(4, m.getExperienciaMinima());
        if (m.getDuracaoLimite() == null) {
            pst.setNull(5, Types.SMALLINT);
        } else {
            pst.setInt(5, m.getDuracaoLimite());
        }
        pst.execute();
        fecharBanco();
    }

    public void alterar(Modalidade m) throws Exception {
        abrirBanco();
        String query = "UPDATE modalidade set nome = ?, categoria = ?, ambiente_necessario = ?, experiencia_minima = ?, duracao_limite = ? where modalidade_id = ?";
        pst = (PreparedStatement) con.prepareStatement(query);
        pst.setString(1, m.getNome());
        pst.setString(2, m.getCategoria());
        pst.setString(3, m.getAmbienteNecessario());
        pst.setString(4, m.getExperienciaMinima());
        if (m.getDuracaoLimite() == null) {
            pst.setNull(5, Types.SMALLINT);
        } else {
            pst.setInt(5, m.getDuracaoLimite());
        }
        pst.setInt(6, m.getModalidadeId());
        pst.executeUpdate();
        fecharBanco();
    }

    public void deletar(Modalidade m) throws Exception {
        abrirBanco();
        String query = "delete from modalidade where modalidade_id = ?";
        pst = (PreparedStatement) con.prepareStatement(query);
        pst.setInt(1, m.getModalidadeId());
        pst.execute();
        fecharBanco();
    }

    public void buscar(Modalidade m) throws Exception {
        abrirBanco();
        String query = "select modalidade_id, nome, categoria, ambiente_necessario, experiencia_minima, duracao_limite FROM modalidade where modalidade_id = ?";
        pst = (PreparedStatement) con.prepareStatement(query);
        pst.setInt(1, m.getModalidadeId());
        ResultSet tr = pst.executeQuery();
        if (tr.next()) {
            m.setNome(tr.getString("nome"));
            m.setCategoria(tr.getString("categoria"));
            m.setAmbienteNecessario(tr.getString("ambiente_necessario"));
            m.setExperienciaMinima(tr.getString("experiencia_minima"));
            if (tr.getObject("duracao_limite") != null) { // GetInt devolveria 0 pra vazio, por isso confere antes com getObject
                m.setDuracaoLimite(tr.getInt("duracao_limite"));
            }
        }
        fecharBanco();
    }

    public ArrayList<Modalidade> listar() throws Exception {
        ArrayList<Modalidade> modalidades = new ArrayList<Modalidade>();
        abrirBanco();
        String query = "select modalidade_id, nome, categoria, ambiente_necessario, experiencia_minima, duracao_limite FROM modalidade order by modalidade_id desc";
        pst = (PreparedStatement) con.prepareStatement(query);
        ResultSet tr = pst.executeQuery();
        Modalidade m;
        while (tr.next()) {
            m = new Modalidade();
            m.setModalidadeId(tr.getInt("modalidade_id"));
            m.setNome(tr.getString("nome"));
            m.setCategoria(tr.getString("categoria"));
            m.setAmbienteNecessario(tr.getString("ambiente_necessario"));
            m.setExperienciaMinima(tr.getString("experiencia_minima"));
            if (tr.getObject("duracao_limite") != null) {
                m.setDuracaoLimite(tr.getInt("duracao_limite"));
            }
            modalidades.add(m);
        }
        fecharBanco();
        return modalidades;
    }
}
