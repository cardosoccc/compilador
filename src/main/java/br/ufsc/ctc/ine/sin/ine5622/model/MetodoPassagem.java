/**
 * @author Vitor Schweitzer e Caio Cardoso
 * created on 2016/06/18
 */
package br.ufsc.ctc.ine.sin.ine5622.model;

/**
 * Representa o método de passagem de parâmetro
 */
public enum MetodoPassagem {

	VALOR("valor"), REFERENCIA("referencia");

	private String nome;

	MetodoPassagem(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

}
