/**
 * @author Vitor Schweitzer e Caio Cardoso
 * created on 2016/06/18
 */
package br.ufsc.ctc.ine.sin.ine5622.model;

/**
 * Representa um identificador da categoria constante
 */
public class IdConstante extends Identificador {

	private String valor;

	public IdConstante(String nome) {
		super(nome);
		this.setTamanho(1);
	}

	/**
	 * Permite inicializar o Identificador com um identificador genérico pré-existente
	 *
	 * @param id o identificador pré-existente
	 */
	public IdConstante(Identificador id) {
		super(id.getNome());
		this.setDeslocamento(id.getDeslocamento());
		this.setNivel(id.getNivel());
		this.setTamanho(1);
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
