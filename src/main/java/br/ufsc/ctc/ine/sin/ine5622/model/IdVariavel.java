/**
 * @author Vitor Schweitzer e Caio Cardoso
 * created on 2016/06/18
 */
package br.ufsc.ctc.ine.sin.ine5622.model;

/**
 * Representa um identificador do tipo variável
 */
public class IdVariavel extends Identificador {

	public IdVariavel(String nome) {
		super(nome);
	}

	/**
	 * Permite inicializar o Identificador com um identificador genérico pré-existente
	 *
	 * @param id o identificador pré-existente
	 */
	public IdVariavel(Identificador id) {
		super(id.getNome());
		this.setDeslocamento(id.getDeslocamento());
		this.setNivel(id.getNivel());
		this.setTamanho(id.getTamanho());
	}

}
