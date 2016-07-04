package br.ufsc.ctc.ine.sin.ine5622.model;

public class IdConstante extends Identificador {

	public IdConstante(String nome) {
		super(nome);
	}

	public IdConstante(Identificador id) {
		super(id.getNome());
		this.setDeslocamento(id.getDeslocamento());
		this.setNivel(id.getNivel());
	}

}
