/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.modelo;

// Item das caixas de seleção: guarda o codigo e mostra o nome
public class ItemCombo {
   private int id;
   private String nome;

    public ItemCombo(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome; // Por isso a caixa mostra o nome e nao o endereco do objeto
    }

    // o JComboBox mostra o que o toString devolve, por isso devolve o nome
    @Override
    public String toString() {
        return nome;
    }

}
