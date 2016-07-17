/**
 * @author Vitor Schweitzer e Caio Cardoso
 * created on 2016/06/18
 */
package br.ufsc.ctc.ine.sin.ine5622.model;

/**
 * Representa um operador com precedência de fator
 */
public enum OperadorMult {

	MUL("*"), FRC("/"), E("e"), DIV("div");

	private String simbolo;

	OperadorMult(String simbolo) {
		this.simbolo = simbolo;
	}

	public String getSimbolo() {
		return simbolo;
	}

}
