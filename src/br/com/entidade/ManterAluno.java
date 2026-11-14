/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.entidade;

import br.com.modelo.Aluno;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

// O aluno fica em duas tabelas (pessoa e aluno), por isso usa transacao:
// ou grava nas duas, ou nao grava em nenhuma
public class ManterAluno extends DAO {

    public void inserir(Aluno a) throws Exception {
        abrirBanco();
        try {
            con.setAutoCommit(false); // Desliga o commit automatico, assim nada é gravado ate chamar o commit()
            String query = "INSERT INTO pessoa (pessoa_id, nome, cpf, data_nascimento, telefone, email) values(null,?,?,?,?,?)";
            pst = (PreparedStatement) con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, a.getNome());
            pst.setString(2, a.getCpf());
            if (a.getDataNascimento() == null) {
                pst.setNull(3, Types.DATE);
            } else {
                pst.setDate(3, java.sql.Date.valueOf(a.getDataNascimento()));
            }
            pst.setString(4, a.getTelefone());
            pst.setString(5, a.getEmail());
            pst.execute();

            // pega o id que o banco gerou pra pessoa e usa no aluno
            ResultSet tr = pst.getGeneratedKeys();
            tr.next(); // Vai pra primeira (e unica) linha, que tem o id gerado
            int pessoaId = tr.getInt(1);
            pst.close();

            query = "INSERT INTO aluno (pessoa_id, endereco, status) values(?,?,?)";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.setInt(1, pessoaId);
            pst.setString(2, a.getEndereco());
            pst.setString(3, a.getStatus());
            pst.execute();

            con.commit(); // Grava nas duas tabelas de uma vez
        } catch (Exception e) {
            con.rollback(); // Deu erro, desfaz tudo que foi feito na transacao
            fecharBanco();
            throw e;
        }
        fecharBanco();
    }

    public void alterar(Aluno a) throws Exception {
        abrirBanco();
        try {
            con.setAutoCommit(false);
            String query = "UPDATE pessoa set nome = ?, cpf = ?, data_nascimento = ?, telefone = ?, email = ? where pessoa_id = ?";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.setString(1, a.getNome());
            pst.setString(2, a.getCpf());
            if (a.getDataNascimento() == null) {
                pst.setNull(3, Types.DATE);
            } else {
                pst.setDate(3, java.sql.Date.valueOf(a.getDataNascimento()));
            }
            pst.setString(4, a.getTelefone());
            pst.setString(5, a.getEmail());
            pst.setInt(6, a.getPessoaId());
            pst.executeUpdate();
            pst.close();

            query = "UPDATE aluno set endereco = ?, status = ? where pessoa_id = ?";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.setString(1, a.getEndereco());
            pst.setString(2, a.getStatus());
            pst.setInt(3, a.getPessoaId());
            pst.executeUpdate();

            con.commit();
        } catch (Exception e) {
            con.rollback();
            fecharBanco();
            throw e;
        }
        fecharBanco();
    }

    public void deletar(Aluno a) throws Exception {
        abrirBanco();
        try {
            con.setAutoCommit(false);
            // primeiro apaga de aluno, depois de pessoa (aluno depende de pessoa)
            String query = "delete from aluno where pessoa_id = ?";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.setInt(1, a.getPessoaId());
            pst.execute();
            pst.close();

            query = "delete from pessoa where pessoa_id = ?";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.setInt(1, a.getPessoaId());
            pst.execute();

            con.commit();
        } catch (Exception e) {
            con.rollback();
            fecharBanco();
            throw e;
        }
        fecharBanco();
    }

    public void buscar(Aluno a) throws Exception {
        abrirBanco();
        String query = "select p.pessoa_id, p.nome, p.cpf, p.data_nascimento, p.telefone, p.email, a.endereco, a.status "
                + "FROM pessoa p INNER JOIN aluno a ON a.pessoa_id = p.pessoa_id where p.pessoa_id = ?";
        pst = (PreparedStatement) con.prepareStatement(query);
        pst.setInt(1, a.getPessoaId());
        ResultSet tr = pst.executeQuery();
        if (tr.next()) {
            a.setNome(tr.getString("nome"));
            a.setCpf(tr.getString("cpf"));
            if (tr.getDate("data_nascimento") != null) {
                a.setDataNascimento(tr.getDate("data_nascimento").toLocalDate());
            }
            a.setTelefone(tr.getString("telefone"));
            a.setEmail(tr.getString("email"));
            a.setEndereco(tr.getString("endereco"));
            a.setStatus(tr.getString("status"));
        }
        fecharBanco();
    }

    public ArrayList<Aluno> listar() throws Exception {
        ArrayList<Aluno> alunos = new ArrayList<Aluno>();
        abrirBanco();
        String query = "select p.pessoa_id, p.nome, p.cpf, p.data_nascimento, p.telefone, p.email, a.endereco, a.status "
                + "FROM pessoa p INNER JOIN aluno a ON a.pessoa_id = p.pessoa_id order by p.pessoa_id desc";
        pst = (PreparedStatement) con.prepareStatement(query);
        ResultSet tr = pst.executeQuery();
        Aluno a;
        while (tr.next()) {
            a = new Aluno();
            a.setPessoaId(tr.getInt("pessoa_id"));
            a.setNome(tr.getString("nome"));
            a.setCpf(tr.getString("cpf"));
            if (tr.getDate("data_nascimento") != null) {
                a.setDataNascimento(tr.getDate("data_nascimento").toLocalDate());
            }
            a.setTelefone(tr.getString("telefone"));
            a.setEmail(tr.getString("email"));
            a.setEndereco(tr.getString("endereco"));
            a.setStatus(tr.getString("status"));
            alunos.add(a);
        }
        fecharBanco();
        return alunos;
    }
}
