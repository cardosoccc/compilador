/**
 * @author Vitor Schweitzer e Caio Cardoso
 * created on 2016/06/18
 */
package br.ufsc.ctc.ine.sin.ine5622.model;

/**
 * Representa um operador com precedência de expressão simples (relacional)
 */
public enum OperadorRel {

	EQ("="), LT("<"), GT(">"), GE(">="), LE("<="), NE("<>");

	private String simbolo;

	OperadorRel(String simbolo) {
		this.simbolo = simbolo;
	}

	public String getSimbolo() {
		return simbolo;
	}

}
