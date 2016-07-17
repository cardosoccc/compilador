/**
 * @author Vitor Schweitzer e Caio Cardoso
 * created on 2016/06/18
 */
package br.ufsc.ctc.ine.sin.ine5622.model;

/**
 * Responsável por armazenar os parâmetros atuais do método que está sendo acionado
 */
public class ContextoMetodo {

	private int numParametrosAtuais = 0;

	public int getNumParametrosAtuais() {
		return numParametrosAtuais;
	}

	public void setNumParametrosAtuais(int numParametrosAtuais) {
		this.numParametrosAtuais = numParametrosAtuais;
	}

}