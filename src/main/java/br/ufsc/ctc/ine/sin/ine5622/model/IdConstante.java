package br.ufsc.ctc.ine.sin.ine5622.model;

public class IdConstante extends Identificador {

	private String valor;

	public IdConstante(String nome) {
		super(nome);
	}

	public IdConstante(Identificador id) {
		super(id.getNome());
		this.setDeslocamento(id.getDeslocamento());
		this.setNivel(id.getNivel());
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
