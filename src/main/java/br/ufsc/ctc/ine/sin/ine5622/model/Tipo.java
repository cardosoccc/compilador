package br.ufsc.ctc.ine.sin.ine5622.model;

public enum Tipo {

	INTEIRO("inteiro"), REAL("real"), BOOLEANO("booleano"), CARACTER("caracter"), CADEIA("cadeia"), NULO("nulo");

	private String nome;

	Tipo(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

}
