package br.ufsc.ctc.ine.sin.ine5622.model;

public class IdVariavel extends Identificador {

	public IdVariavel(String nome) {
		super(nome);
	}

	public IdVariavel(Identificador id) {
		super(id.getNome());
		this.setDeslocamento(id.getDeslocamento());
		this.setNivel(id.getNivel());
	}

}
