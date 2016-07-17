/**
 * @author Vitor Schweitzer e Caio Cardoso
 * created on 2016/06/18
 */
package br.ufsc.ctc.ine.sin.ine5622.model;

/**
 * Representa os possíveis valores para o ContextoEXPR
 */
public enum ContextoEXPR {

	IMPRESSAO("impressao"), PAR_ATUAL("par-atual");

	private String nome;

	ContextoEXPR(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

}
