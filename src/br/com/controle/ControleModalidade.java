/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.controle;

import br.com.entidade.ManterModalidade;
import br.com.modelo.Modalidade;
import java.sql.SQLException;
import java.util.ArrayList;

// confere os dados da modalidade antes de chamar o ManterModalidade
public class ControleModalidade {

    ManterModalidade mm = new ManterModalidade();

    public void inserir(Modalidade m) throws Exception {
        validar(m);
        mm.inserir(m);
    }

    public void alterar(Modalidade m) throws Exception {
        if (m.getModalidadeId() == 0) {
            throw new Exception("Selecione uma modalidade na tabela!");
        }
        validar(m);
        mm.alterar(m);
    }

    public void deletar(Modalidade m) throws Exception {
        if (m.getModalidadeId() == 0) {
            throw new Exception("Selecione uma modalidade na tabela!");
        }
        try {
            mm.deletar(m);
        } catch (SQLException e) {
            // se tiver professor ligado o mysql nao deixa apagar (1451)
            if (e.getErrorCode() == 1451) {
                throw new Exception("Não é possível excluir: existem professores ligados a esta modalidade!");
            }
            throw e;
        }
    }

    public void buscar(Modalidade m) throws Exception {
        mm.buscar(m);
    }

    public ArrayList<Modalidade> listar() throws Exception {
        return mm.listar();
    }

    private void validar(Modalidade m) throws Exception {
        if (m.getNome().equals("")) {
            throw new Exception("Informe o nome da modalidade!");
        }
        // tamanhos iguais aos do banco
        if (m.getNome().length() > 50) {
            throw new Exception("O nome pode ter no máximo 50 caracteres!");
        }
        if (m.getCategoria() != null && m.getCategoria().length() > 20) {
            throw new Exception("A categoria pode ter no máximo 20 caracteres!");
        }
        if (m.getAmbienteNecessario() != null && m.getAmbienteNecessario().length() > 30) {
            throw new Exception("O ambiente pode ter no máximo 30 caracteres!");
        }
        if (m.getExperienciaMinima() != null && m.getExperienciaMinima().length() > 20) {
            throw new Exception("A experiência mínima pode ter no máximo 20 caracteres!");
        }
        if (m.getDuracaoLimite() != null && m.getDuracaoLimite() <= 0) {
            throw new Exception("A duração limite deve ser maior que zero!");
        }
    }
}
