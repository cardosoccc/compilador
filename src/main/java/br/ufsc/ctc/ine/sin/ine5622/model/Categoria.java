/**
 * @author Vitor Schweitzer e Caio Cardoso
 * created on 2016/06/18
 */
package br.ufsc.ctc.ine.sin.ine5622.model;

/**
 * Representa a categoria do identificador.
 */
public enum Categoria {

	PROGRAMA("programa"), VARIAVEL("variavel"), CONSTANTE("constante"), METODO("metodo"), PARAMETRO("parametro");

	private String nome;

	Categoria(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}
}