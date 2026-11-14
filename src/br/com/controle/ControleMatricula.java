/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.controle;

import br.com.entidade.ManterMatricula;
import br.com.modelo.ItemCombo;
import br.com.modelo.Matricula;
import java.sql.SQLException;
import java.util.ArrayList;

// Controle da matricula (rotina principal): valida e calcula a data de termino
public class ControleMatricula {

    ManterMatricula mm = new ManterMatricula();

    public void inserir(Matricula m) throws Exception {
        validar(m);
        calcularTermino(m);
        mm.inserir(m);
    }

    public void alterar(Matricula m) throws Exception {
        if (m.getMatriculaId() == 0) {
            throw new Exception("Selecione uma matrícula na tabela!");
        }
        validar(m);
        calcularTermino(m);
        mm.alterar(m);
    }

    public void deletar(Matricula m) throws Exception {
        if (m.getMatriculaId() == 0) {
            throw new Exception("Selecione uma matrícula na tabela!");
        }
        try {
            mm.deletar(m);
        } catch (SQLException e) {
            // 1451 = ja tem pagamento dessa matricula
            if (e.getErrorCode() == 1451) {
                throw new Exception("Não é possível excluir: esta matrícula tem pagamentos registrados!");
            }
            throw e;
        }
    }

    public void buscar(Matricula m) throws Exception {
        mm.buscar(m);
    }

    public ArrayList<Matricula> listar() throws Exception {
        return mm.listar();
    }

    public ArrayList<ItemCombo> listarAlunos() throws Exception {
        return mm.listarAlunos();
    }

    public ArrayList<ItemCombo> listarPlanos() throws Exception {
        return mm.listarPlanos();
    }

    public ArrayList<ItemCombo> listarUnidades() throws Exception {
        return mm.listarUnidades();
    }

    private void validar(Matricula m) throws Exception {
        if (m.getAlunoId() == 0) {
            throw new Exception("Selecione o aluno!");
        }
        if (m.getPlanoId() == 0) {
            throw new Exception("Selecione o plano!");
        }
        if (m.getUnidadeId() == 0) {
            throw new Exception("Selecione a unidade!");
        }
        if (m.getDataInicio() == null) {
            throw new Exception("Informe a data de início!");
        }
        // so matricula cancelada tem motivo
        if (m.getStatus().equals("cancelada")) {
            if (m.getMotivoCancelamento() == null) {
                throw new Exception("Informe o motivo do cancelamento!");
            }
            if (m.getMotivoCancelamento().length() > 50) {
                throw new Exception("O motivo pode ter no máximo 50 caracteres!");
            }
        } else {
            m.setMotivoCancelamento(null);
        }
    }

    // termino = inicio + duracao do plano em meses (busca a duracao no banco)
    private void calcularTermino(Matricula m) throws Exception {
        int meses = mm.buscarDuracaoPlano(m.getPlanoId());
        m.setDataTermino(m.getDataInicio().plusMonths(meses)); // PlusMonths ja ajusta mes menor, ex: 31/01 + 1 mes = 28/02
    }
}
