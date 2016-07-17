/**
 * @author Vitor Schweitzer e Caio Cardoso
 * created on 2016/06/18
 */
package br.ufsc.ctc.ine.sin.ine5622.model;

/**
 * Representa os possíveis valores para a sub-categoria de um identificador
 */
public enum SubCategoria {

	PREDEFINIDO("pre-definido"), CADEIA("cadeia"), VETOR("vetor");

	private String nome;

	SubCategoria(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

}
