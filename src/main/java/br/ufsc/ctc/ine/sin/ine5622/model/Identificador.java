/**
 * @author Vitor Schweitzer e Caio Cardoso
 * created on 2016/06/18
 */
package br.ufsc.ctc.ine.sin.ine5622.model;

/**
 * Representa um identificador utilizado durante o processo de compilação
 */
public class Identificador {

	private String nome;
	private int nivel;
	private int deslocamento;
	private int tamanho;
	private Tipo tipo;
	private SubCategoria subCategoria;

	public Identificador(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public SubCategoria getSubCategoria() {
		return subCategoria;
	}

	public void setSubCategoria(SubCategoria subCategoria) {
		this.subCategoria = subCategoria;
	}

}
