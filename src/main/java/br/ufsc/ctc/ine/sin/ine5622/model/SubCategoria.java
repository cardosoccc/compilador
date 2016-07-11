package br.ufsc.ctc.ine.sin.ine5622.model;

public enum SubCategoria {

	PREDEFINIDO("pre-definido"), CADEIA("cadeia"), VETOR("vetor");

	private String nome;

	SubCategoria(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

}
