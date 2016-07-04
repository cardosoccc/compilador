package br.ufsc.ctc.ine.sin.ine5622.model;

import java.util.ArrayList;
import java.util.List;

public class ContextoSemantico {

	private String contextoLID;
	private String tipoAtual;
	private String categoriaAtual;
	private String subCategoria;
	private ArrayList<Identificador> listaDeclaracao;
	private int primeiraPosicaoListaDeclaracao;
	private int ultimaPosicaoListaDeclaracao;
	private String tipoConst;
	private int valConst;
	private int numElementos;
	private int numParametrosFormais;

	public String getContextoLID() {
		return contextoLID;
	}

	public void setContextoLID(String contextoLID) {
		this.contextoLID = contextoLID;
	}

	public String getTipoAtual() {
		return tipoAtual;
	}

	public void setTipoAtual(String tipoAtual) {
		this.tipoAtual = tipoAtual;
	}

	public void setCategoriaAtual(String categoriaAtual) {
		this.categoriaAtual = categoriaAtual;

	}

	public void setSubcategoria(String subCategoria) {
		this.subCategoria = subCategoria;
	}

	public void inicializaListaDeclaracao() {
		this.listaDeclaracao = new ArrayList<Identificador>();
	}

	public void setPrimeiraPosicaoListaDeclaracao(int posicao) {
		 this.primeiraPosicaoListaDeclaracao = posicao;

	}

	public void setUltimaPosicaoListaDeclaracao(int posicao) {
		this.ultimaPosicaoListaDeclaracao = posicao;
	}

	public List<Identificador> getListaDeclaracao() {
		return this.listaDeclaracao;
	}

	public String getCategoriaAtual() {
		return this.categoriaAtual;
	}

	public String getTipoConst() {
		return this.tipoConst;
	}

	public int getValConst() {
		return this.valConst;
	}

	public void setNumElementos(int numElements) {
		this.numElementos = numElements;
	}

	public String getSubCategoria() {
		return this.subCategoria;
	}

	public void incrementaNumParametrosFormais() {
		this.numParametrosFormais += 1;

	}

}
