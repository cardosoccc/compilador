/**
 * @author Vitor Schweitzer e Caio Cardoso
 * created on 2016/06/18
 */
package br.ufsc.ctc.ine.sin.ine5622.model;

/**
 * Representa os tipos possíveis para um identificador
 */
public enum Tipo {

	INTEIRO("inteiro"), REAL("real"), BOOLEANO("booleano"), CARACTER("caracter"), CADEIA("cadeia"), NULO("nulo");

	private String nome;

	Tipo(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

}
