package br.ufsc.ctc.ine.sin.ine5622.model;

public enum ContextoLID {

	DECL("decl"), PAR_FORMAL("par-formal"), LEITURA("leitura");

	private String nome;

	ContextoLID(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

}
