/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.entidade;

import br.com.modelo.ItemCombo;
import br.com.modelo.MatriculaDetalhada;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

// Consulta com JOIN e relatorio por periodo (so leitura, nao altera nada no banco)
public class ManterRelatorio extends DAO {

    // parte comum das duas consultas: junta matricula com aluno, pessoa, plano e unidade
    String select = "select m.matricula_id, p.nome as nome_aluno, pl.nome as nome_plano, u.nome as nome_unidade, "
            + "m.data_inicio, m.data_termino, m.status, pl.valor "
            + "FROM matricula m "
            + "INNER JOIN aluno a ON a.pessoa_id = m.aluno_id "
            + "INNER JOIN pessoa p ON p.pessoa_id = a.pessoa_id "
            + "INNER JOIN plano pl ON pl.plano_id = m.plano_id "
            + "INNER JOIN unidade u ON u.unidade_id = m.unidade_id ";

    // busca pelo nome do aluno (vazio traz todas)
    public ArrayList<MatriculaDetalhada> consultar(String nome) throws Exception {
        abrirBanco();
        String query = select + "where p.nome like ? order by m.matricula_id desc";
        pst = (PreparedStatement) con.prepareStatement(query);
        // o % antes e depois faz achar o nome em qualquer parte
        pst.setString(1, "%" + nome + "%");
        ResultSet tr = pst.executeQuery();
        ArrayList<MatriculaDetalhada> lista = montarLista(tr);
        fecharBanco();
        return lista;
    }

    // matriculas que comecaram dentro do periodo, com filtro opcional de unidade e status
    public ArrayList<MatriculaDetalhada> listarPorPeriodo(LocalDate inicio, LocalDate fim, int unidadeId, String status) throws Exception {
        abrirBanco();
        String query = select + "where m.data_inicio between ? and ?";
        if (unidadeId != 0) {
            query = query + " and m.unidade_id = ?";
        }
        if (!status.equals("Todas")) { // Caso seja Todas nao filtra o status
            query = query + " and m.status = ?";
        }
        query = query + " order by m.data_inicio";
        pst = (PreparedStatement) con.prepareStatement(query);
        pst.setDate(1, java.sql.Date.valueOf(inicio));
        pst.setDate(2, java.sql.Date.valueOf(fim));
        // a posicao do proximo ? depende de quais filtros foram usados
        int posicao = 3;
        if (unidadeId != 0) {
            pst.setInt(posicao, unidadeId);
            posicao++; // Proximo filtro vai no ? seguinte
        }
        if (!status.equals("Todas")) {
            pst.setString(posicao, status);
        }
        ResultSet tr = pst.executeQuery();
        ArrayList<MatriculaDetalhada> lista = montarLista(tr);
        fecharBanco();
        return lista;
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

    // transforma o resultado do select na lista (usado pelas duas consultas)
    private ArrayList<MatriculaDetalhada> montarLista(ResultSet tr) throws Exception {
        ArrayList<MatriculaDetalhada> lista = new ArrayList<MatriculaDetalhada>();
        MatriculaDetalhada md;
        while (tr.next()) {
            md = new MatriculaDetalhada();
            md.setMatriculaId(tr.getInt("matricula_id"));
            md.setNomeAluno(tr.getString("nome_aluno"));
            md.setNomePlano(tr.getString("nome_plano"));
            md.setNomeUnidade(tr.getString("nome_unidade"));
            md.setDataInicio(tr.getDate("data_inicio").toLocalDate());
            if (tr.getDate("data_termino") != null) {
                md.setDataTermino(tr.getDate("data_termino").toLocalDate());
            }
            md.setStatus(tr.getString("status"));
            md.setValor(tr.getBigDecimal("valor"));
            lista.add(md);
        }
        return lista;
    }
}
