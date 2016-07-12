package br.ufsc.ctc.ine.sin.ine5622.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ContextoSemantico {

	private ContextoLID contextoLID;
	private Tipo tipoAtual;
	private Categoria categoriaAtual;
	private SubCategoria subCategoria;
	private ArrayList<Identificador> listaDeclaracao;
	private int primeiraPosicaoListaDeclaracao;
	private int ultimaPosicaoListaDeclaracao;
	private Tipo tipoConst;
	private String valConst;
	private int numElementos;
	private int numParametrosFormais;
	private IdMetodo idMetodoAtual;
	private Mpp mpp;
	private Tipo tipoMetodo;
	private Identificador idAtual;
	private Tipo tipoExpr;
	private Tipo tipoLadoEsq;
	private ContextoEXPR contextoEXPR;
	private SubCategoria subCategoriaVarIndexada;
	private Stack<ContextoMetodo> pilhaContextoMetodo;
	private Tipo tipoVar;

	public ContextoLID getContextoLID() {
		return contextoLID;
	}

	public void setContextoLID(ContextoLID contextoLID) {
		this.contextoLID = contextoLID;
	}

	public Tipo getTipoAtual() {
		return tipoAtual;
	}

	public void setTipoAtual(Tipo tipoAtual) {
		this.tipoAtual = tipoAtual;
	}

	public Categoria getCategoriaAtual() {
		return this.categoriaAtual;
	}

	public void setCategoriaAtual(Categoria categoriaAtual) {
		this.categoriaAtual = categoriaAtual;

	}

	public void setSubCategoria(SubCategoria subCategoria) {
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

	public Tipo getTipoConst() {
		return this.tipoConst;
	}

	public void setTipoConst(Tipo tipoConst) {
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

	public SubCategoria getSubCategoria() {
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

	public IdMetodo getIdMetodoAtual() {
		return idMetodoAtual;
	}

	public void setIdMetodoAtual(IdMetodo idMetodo) {
		this.idMetodoAtual = idMetodo;

	}

	public Mpp getMpp() {
		return this.mpp;
	}

	public void setMpp(Mpp mpp) {
		this.mpp = mpp;
	}

	public Tipo getTipoMetodo() {
		return tipoMetodo;
	}

	public void setTipoMetodo(Tipo tipoMetodo) {
		this.tipoMetodo = tipoMetodo;

	}

	public Identificador getIdAtual() {
		return idAtual;
	}

	public void setIdAtual(Identificador id) {
		this.idAtual = id;

	}

	public Tipo getTipoExpr() {
		return tipoExpr;
	}

	public void setTipoExpr(Tipo tipoExpr) {
		this.tipoExpr = tipoExpr;
	}

	public ContextoEXPR getContextoEXPR() {
		return contextoEXPR;
	}

	public void setContextoEXPR(ContextoEXPR contextoEXPR) {
		this.contextoEXPR = contextoEXPR;
	}

	public Tipo getTipoLadoEsq() {
		return tipoLadoEsq;
	}

	public void setTipoLadoEsq(Tipo tipoLadoEsq) {
		this.tipoLadoEsq = tipoLadoEsq;
	}

	public SubCategoria getSubCategoriaVarIndexada() {
		return subCategoriaVarIndexada;
	}

	public void setSubCategoriaVarIndexada(SubCategoria subCategoriaVarIndexada) {
		this.subCategoriaVarIndexada = subCategoriaVarIndexada;
	}

	public Tipo getTipoVar() {
		return tipoVar;
	}

	public void setTipoVar(Tipo tipoVar) {
		this.tipoVar = tipoVar;
	}

	public ContextoMetodo peekContextoMetodo() {
		return this.pilhaContextoMetodo.peek();
	}

	public ContextoMetodo popContextoMetodo() {
		return this.pilhaContextoMetodo.pop();
	}

	public void pushContextoMetodo(ContextoMetodo contextoMetodo) {
		this.pilhaContextoMetodo.push(contextoMetodo);
	}

}
