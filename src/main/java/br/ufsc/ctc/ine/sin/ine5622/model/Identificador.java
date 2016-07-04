package br.ufsc.ctc.ine.sin.ine5622.model;

public class Identificador {


	private String nome;
	private int nivel;
	private int deslocamento;
	private int tamanho;
	private String tipo;
	private int valor;

	public Identificador(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNivel(int nivelAtual) {
		this.nivel = nivelAtual;
	}

	public void setDeslocamento(int deslocamento) {
		this.deslocamento = deslocamento;

	}

	public int getDeslocamento() {
		return this.deslocamento;
	}

	public int getNivel() {
		return this.nivel;
	}

	public int getTamanho() {
		return this.tamanho;
	}

	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setValor(int valor) {
		this.valor = valor;

	}

}
