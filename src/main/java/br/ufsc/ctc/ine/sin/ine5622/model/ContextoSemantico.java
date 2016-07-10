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
	private String valConst;
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

	public int getPrimeiraPosicaoListaDeclaracao() {
		return this.primeiraPosicaoListaDeclaracao;
	}

	public int getUltimaPosicaoListaDeclaracao() {
		return this.ultimaPosicaoListaDeclaracao;
	}

	public String getCategoriaAtual() {
		return this.categoriaAtual;
	}

	public String getTipoConst() {
		return this.tipoConst;
	}

	public void setTipoConst(String tipoConst) {
		this.tipoConst = tipoConst;
	}

	public String getValConst() {
		return this.valConst;
	}

	public void setValConst(String valConst) {
		this.valConst = valConst;
	}

	public void setNumElementos(int numElements) {
		this.numElementos = numElements;
	}

	public String getSubCategoria() {
		return this.subCategoria;
	}

	public int getNumElementos() {
		return numElementos;
	}

	public int getNumParametrosFormais() {
		return numParametrosFormais;
	}

	public void setNumParametrosFormais(int numParametrosFormais) {
		this.numParametrosFormais = numParametrosFormais;
	}

	public void incrementaNumParametrosFormais() {
		this.numParametrosFormais += 1;

	}

}
