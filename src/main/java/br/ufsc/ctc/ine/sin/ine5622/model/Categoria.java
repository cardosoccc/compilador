package br.ufsc.ctc.ine.sin.ine5622.model;

public enum Categoria {

	PROGRAMA("programa"), VARIAVEL("variavel"), CONSTANTE("constante"), METODO("metodo"), PARAMETRO("parametro");

	private String nome;

	Categoria(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}
}