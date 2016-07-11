package br.ufsc.ctc.ine.sin.ine5622.model;

public enum Mpp {

	VALOR("valor"), REFERENCIA("referencia");

	private String nome;

	Mpp(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

}
