/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.entidade;

import br.com.modelo.ItemCombo;
import br.com.modelo.Matricula;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;

public class ManterMatricula extends DAO {

    public void inserir(Matricula m) throws Exception {
        abrirBanco();
        String query = "INSERT INTO matricula (matricula_id, aluno_id, plano_id, unidade_id, data_inicio, data_termino, status, forma_renovacao, motivo_cancelamento, canal_matricula) values(null,?,?,?,?,?,?,?,?,?)";
        pst = (PreparedStatement) con.prepareStatement(query);
        pst.setInt(1, m.getAlunoId());
        pst.setInt(2, m.getPlanoId());
        pst.setInt(3, m.getUnidadeId());
        pst.setDate(4, java.sql.Date.valueOf(m.getDataInicio())); // Converte o LocalDate pro tipo de data do JDBC
        pst.setDate(5, java.sql.Date.valueOf(m.getDataTermino()));
        pst.setString(6, m.getStatus());
        pst.setString(7, m.getFormaRenovacao());
        if (m.getMotivoCancelamento() == null) {
            pst.setNull(8, Types.VARCHAR);
        } else {
            pst.setString(8, m.getMotivoCancelamento());
        }
        pst.setString(9, m.getCanalMatricula());
        pst.execute();
        fecharBanco();
    }

    public void alterar(Matricula m) throws Exception {
        abrirBanco();
        String query = "UPDATE matricula set aluno_id = ?, plano_id = ?, unidade_id = ?, data_inicio = ?, data_termino = ?, status = ?, forma_renovacao = ?, motivo_cancelamento = ?, canal_matricula = ? where matricula_id = ?";
        pst = (PreparedStatement) con.prepareStatement(query);
        pst.setInt(1, m.getAlunoId());
        pst.setInt(2, m.getPlanoId());
        pst.setInt(3, m.getUnidadeId());
        pst.setDate(4, java.sql.Date.valueOf(m.getDataInicio()));
        pst.setDate(5, java.sql.Date.valueOf(m.getDataTermino()));
        pst.setString(6, m.getStatus());
        pst.setString(7, m.getFormaRenovacao());
        if (m.getMotivoCancelamento() == null) {
            pst.setNull(8, Types.VARCHAR);
        } else {
            pst.setString(8, m.getMotivoCancelamento());
        }
        pst.setString(9, m.getCanalMatricula());
        pst.setInt(10, m.getMatriculaId());
        pst.executeUpdate();
        fecharBanco();
    }

    public void deletar(Matricula m) throws Exception {
        abrirBanco();
        String query = "delete from matricula where matricula_id = ?";
        pst = (PreparedStatement) con.prepareStatement(query);
        pst.setInt(1, m.getMatriculaId());
        pst.execute();
        fecharBanco();
    }

    public void buscar(Matricula m) throws Exception {
        abrirBanco();
        String query = "select matricula_id, aluno_id, plano_id, unidade_id, data_inicio, data_termino, status, forma_renovacao, motivo_cancelamento, canal_matricula FROM matricula where matricula_id = ?";
        pst = (PreparedStatement) con.prepareStatement(query);
        pst.setInt(1, m.getMatriculaId());
        ResultSet tr = pst.executeQuery();
        if (tr.next()) {
            m.setAlunoId(tr.getInt("aluno_id"));
            m.setPlanoId(tr.getInt("plano_id"));
            m.setUnidadeId(tr.getInt("unidade_id"));
            m.setDataInicio(tr.getDate("data_inicio").toLocalDate());
            if (tr.getDate("data_termino") != null) {
                m.setDataTermino(tr.getDate("data_termino").toLocalDate());
            }
            m.setStatus(tr.getString("status"));
            m.setFormaRenovacao(tr.getString("forma_renovacao"));
            m.setMotivoCancelamento(tr.getString("motivo_cancelamento"));
            m.setCanalMatricula(tr.getString("canal_matricula"));
        }
        fecharBanco();
    }

    // lista com os nomes do aluno, do plano e da unidade (por isso os INNER JOIN)
    public ArrayList<Matricula> listar() throws Exception {
        ArrayList<Matricula> matriculas = new ArrayList<Matricula>();
        abrirBanco();
        String query = "select m.matricula_id, m.data_inicio, m.data_termino, m.status, p.nome as nome_aluno, pl.nome as nome_plano, u.nome as nome_unidade "
                + "FROM matricula m "
                + "INNER JOIN aluno a ON a.pessoa_id = m.aluno_id "
                + "INNER JOIN pessoa p ON p.pessoa_id = a.pessoa_id "
                + "INNER JOIN plano pl ON pl.plano_id = m.plano_id "
                + "INNER JOIN unidade u ON u.unidade_id = m.unidade_id "
                + "order by m.matricula_id desc";
        pst = (PreparedStatement) con.prepareStatement(query);
        ResultSet tr = pst.executeQuery();
        Matricula m;
        while (tr.next()) {
            m = new Matricula();
            m.setMatriculaId(tr.getInt("matricula_id"));
            m.setDataInicio(tr.getDate("data_inicio").toLocalDate());
            if (tr.getDate("data_termino") != null) {
                m.setDataTermino(tr.getDate("data_termino").toLocalDate());
            }
            m.setStatus(tr.getString("status"));
            m.setNomeAluno(tr.getString("nome_aluno"));
            m.setNomePlano(tr.getString("nome_plano"));
            m.setNomeUnidade(tr.getString("nome_unidade"));
            matriculas.add(m);
        }
        fecharBanco();
        return matriculas;
    }

    // usado pra calcular a data de termino
    public int buscarDuracaoPlano(int planoId) throws Exception {
        int duracao = 0;
        abrirBanco();
        String query = "select duracao_meses FROM plano where plano_id = ?";
        pst = (PreparedStatement) con.prepareStatement(query);
        pst.setInt(1, planoId);
        ResultSet tr = pst.executeQuery();
        if (tr.next()) {
            duracao = tr.getInt("duracao_meses");
        }
        fecharBanco();
        return duracao;
    }

    // itens das caixas de seleção da tela
    public ArrayList<ItemCombo> listarAlunos() throws Exception {
        ArrayList<ItemCombo> itens = new ArrayList<ItemCombo>();
        abrirBanco();
        String query = "select p.pessoa_id, p.nome FROM aluno a INNER JOIN pessoa p ON p.pessoa_id = a.pessoa_id order by p.nome";
        pst = (PreparedStatement) con.prepareStatement(query);
        ResultSet tr = pst.executeQuery();
        while (tr.next()) {
            itens.add(new ItemCombo(tr.getInt("pessoa_id"), tr.getString("nome")));
        }
        fecharBanco();
        return itens;
    }

    public ArrayList<ItemCombo> listarPlanos() throws Exception {
        ArrayList<ItemCombo> itens = new ArrayList<ItemCombo>();
        abrirBanco();
        String query = "select plano_id, nome FROM plano order by nome";
        pst = (PreparedStatement) con.prepareStatement(query);
        ResultSet tr = pst.executeQuery();
        while (tr.next()) {
            itens.add(new ItemCombo(tr.getInt("plano_id"), tr.getString("nome")));
        }
        fecharBanco();
        return itens;
    }

    public ArrayList<ItemCombo> listarUnidades() throws Exception {
        ArrayList<ItemCombo> itens = new ArrayList<ItemCombo>();
        abrirBanco();
        String query = "select unidade_id, nome FROM unidade order by nome";
        pst = (PreparedStatement) con.prepareStatement(query);
        ResultSet tr = pst.executeQuery();
        while (tr.next()) {
            itens.add(new ItemCombo(tr.getInt("unidade_id"), tr.getString("nome")));
        }
        fecharBanco();
        return itens;
    }
}
