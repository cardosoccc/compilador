/**
 * @author Vitor Schweitzer e Caio Cardoso
 * created on 2016/06/18
 */
package br.ufsc.ctc.ine.sin.ine5622.model;

/**
 * Representa os possíveis valores para o ContextoLID
 */
public enum ContextoLID {

	DECL("decl"), PAR_FORMAL("par-formal"), LEITURA("leitura");

	private String nome;

	ContextoLID(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

}
