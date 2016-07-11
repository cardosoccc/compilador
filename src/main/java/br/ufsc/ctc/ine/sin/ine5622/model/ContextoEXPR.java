package br.ufsc.ctc.ine.sin.ine5622.model;

public enum ContextoEXPR {

	IMPRESSAO("impressao");

	private String nome;

	ContextoEXPR(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

}
