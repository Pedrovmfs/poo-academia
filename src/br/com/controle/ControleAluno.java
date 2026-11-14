/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.controle;

import br.com.entidade.ManterAluno;
import br.com.modelo.Aluno;
import java.sql.SQLException;
import java.util.ArrayList;

// validacoes do aluno + tratamento de cpf repetido
public class ControleAluno {

    ManterAluno ma = new ManterAluno();

    public void inserir(Aluno a) throws Exception {
        validar(a);
        try {
            ma.inserir(a);
        } catch (SQLException e) {
            // 1062 = valor repetido em campo UNIQUE (o cpf)
            if (e.getErrorCode() == 1062) {
                throw new Exception("Já existe uma pessoa cadastrada com este CPF!");
            }
            throw e;
        }
    }

    public void alterar(Aluno a) throws Exception {
        if (a.getPessoaId() == 0) {
            throw new Exception("Selecione um aluno na tabela!");
        }
        validar(a);
        try {
            ma.alterar(a);
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new Exception("Já existe uma pessoa cadastrada com este CPF!");
            }
            throw e;
        }
    }

    public void deletar(Aluno a) throws Exception {
        if (a.getPessoaId() == 0) {
            throw new Exception("Selecione um aluno na tabela!");
        }
        try {
            ma.deletar(a);
        } catch (SQLException e) {
            // aluno com matricula, avaliacao ou convite nao pode ser apagado (erro 1451)
            if (e.getErrorCode() == 1451) {
                throw new Exception("Não é possível excluir: este aluno tem matrículas ou outros registros ligados a ele!");
            }
            throw e;
        }
    }

    public void buscar(Aluno a) throws Exception {
        ma.buscar(a);
    }

    public ArrayList<Aluno> listar() throws Exception {
        return ma.listar();
    }

    private void validar(Aluno a) throws Exception {
        if (a.getNome().equals("")) {
            throw new Exception("Informe o nome do aluno!");
        }
        if (a.getNome().length() > 100) {
            throw new Exception("O nome pode ter no máximo 100 caracteres!");
        }
        if (a.getCpf().equals("")) {
            throw new Exception("Informe o CPF!");
        }
        if (a.getCpf().length() != 11) { // Ja chega sem ponto e traço, a tela tira antes
            throw new Exception("O CPF deve ter 11 números!");
        }
        // cpf com todos os numeros iguais (111.111.111-11) nao existe
        if (a.getCpf().matches("(\\d)\\1{10}")) {
            throw new Exception("CPF inválido!");
        }
        if (a.getTelefone() != null && a.getTelefone().length() > 20) {
            throw new Exception("O telefone pode ter no máximo 20 caracteres!");
        }
        if (a.getEmail() != null && a.getEmail().length() > 100) {
            throw new Exception("O e-mail pode ter no máximo 100 caracteres!");
        }
        if (a.getEndereco() != null && a.getEndereco().length() > 200) {
            throw new Exception("O endereço pode ter no máximo 200 caracteres!");
        }
    }
}
