/**
 * @author Vitor Schweitzer e Caio Cardoso
 * created on 2016/06/18
 */
package br.ufsc.ctc.ine.sin.ine5622.model;

/**
 * Representa um operador com precedência de termo
 */
public enum OperadorAdd {

	ADD("+"), SUB("-"), OU("ou");

	private String simbolo;

	OperadorAdd(String simbolo) {
		this.simbolo = simbolo;
	}

	public String getSimbolo() {
		return simbolo;
	}

}
