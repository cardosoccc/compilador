/**
 * @author Vitor Schweitzer e Caio Cardoso
 * created on 2016/06/18
 */
package br.ufsc.ctc.ine.sin.ine5622.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Responsável por armazenar os valores de variáveis de contexo usadas durante a compilação
 */
public class ContextoSemantico {

	// Variáveis de contexto simples
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
	private MetodoPassagem mpp;
	private Tipo tipoMetodo;
	private Tipo tipoLadoEsq;
	private ContextoEXPR contextoEXPR;
	private boolean opNega;
	private boolean opUnario;

	// Variáveis empilhadas para representar expressões aninhadas
	private Stack<OperadorRel> pilhaOperadoresRel;
	private Stack<OperadorAdd> pilhaOperadoresAdd;
	private Stack<OperadorMult> pilhaOperadoresMult;
	private Stack<Identificador> pilhaId;
	private Stack<IdMetodo> pilhaIdMetodo;
	private Stack<Tipo> pilhaTipoExpr;
	private Stack<Tipo> pilhaTipoExprSimples;
	private Stack<Tipo> pilhaTipoTermo;
	private Stack<Tipo> pilhaTipoFator;
	private Stack<Tipo> pilhaTipoVar;
	private Stack<ContextoMetodo> pilhaContextoMetodo;
	private Stack<SubCategoria> pilhaSubCategoriaVarIndexada;
	private Stack<Boolean> pilhaRetorne;

	public ContextoSemantico() {
		this.pilhaOperadoresRel = new Stack<OperadorRel>();
		this.pilhaOperadoresAdd = new Stack<OperadorAdd>();
		this.pilhaOperadoresMult = new Stack<OperadorMult>();
		this.pilhaContextoMetodo = new Stack<ContextoMetodo>();
		this.pilhaIdMetodo = new Stack<IdMetodo>();
		this.pilhaTipoExpr = new Stack<Tipo>();
		this.pilhaTipoExprSimples = new Stack<Tipo>();
		this.pilhaTipoTermo = new Stack<Tipo>();
		this.pilhaTipoFator = new Stack<Tipo>();
		this.pilhaTipoVar = new Stack<Tipo>();
		this.pilhaId = new Stack<Identificador>();
		this.pilhaSubCategoriaVarIndexada = new Stack<SubCategoria>();
		this.pilhaRetorne = new Stack<Boolean>();
	}

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

	public IdMetodo peekIdMetodo() {
		return pilhaIdMetodo.peek();
	}

	public IdMetodo popIdMetodo() {
		return pilhaIdMetodo.pop();
	}

	public void pushIdMetodo(IdMetodo idMetodo) {
		this.pilhaIdMetodo.push(idMetodo);
	}

	public MetodoPassagem getMpp() {
		return this.mpp;
	}

	public void setMpp(MetodoPassagem mpp) {
		this.mpp = mpp;
	}

	public Tipo getTipoMetodo() {
		return tipoMetodo;
	}

	public void setTipoMetodo(Tipo tipoMetodo) {
		this.tipoMetodo = tipoMetodo;

	}

	public Identificador peekId() {
		return pilhaId.peek();
	}

	public Identificador popId() {
		return pilhaId.pop();
	}

	public void pushId(Identificador id) {
		this.pilhaId.push(id);

	}

	public Tipo peekTipoExpr() {
		return pilhaTipoExpr.peek();
	}

	public Tipo popTipoExpr() {
		return pilhaTipoExpr.pop();
	}

	public void pushTipoExpr(Tipo tipoExpr) {
		this.pilhaTipoExpr.push(tipoExpr);
	}

	public Tipo peekTipoExprSimples() {
		return pilhaTipoExprSimples.peek();
	}

	public Tipo popTipoExprSimples() {
		return pilhaTipoExprSimples.pop();
	}

	public void pushTipoExprSimples(Tipo tipoExprSimples) {
		this.pilhaTipoExprSimples.push(tipoExprSimples);
	}

	public Tipo peekTipoTermo() {
		return pilhaTipoTermo.peek();
	}

	public Tipo popTipoTermo() {
		return pilhaTipoTermo.pop();
	}

	public void pushTipoTermo(Tipo tipoTermo) {
		this.pilhaTipoTermo.push(tipoTermo);
	}

	public Tipo peekTipoFator() {
		return pilhaTipoFator.peek();
	}

	public Tipo popTipoFator() {
		return pilhaTipoFator.pop();
	}

	public void pushTipoFator(Tipo tipoFator) {
		this.pilhaTipoFator.push(tipoFator);
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

	public SubCategoria peekSubCategoriaVarIndexada() {
		return pilhaSubCategoriaVarIndexada.peek();
	}

	public SubCategoria popSubCategoriaVarIndexada() {
		return pilhaSubCategoriaVarIndexada.pop();
	}

	public void pushSubCategoriaVarIndexada(SubCategoria subCategoriaVarIndexada) {
		this.pilhaSubCategoriaVarIndexada.push(subCategoriaVarIndexada);
	}

	public Tipo peekTipoVar() {
		return pilhaTipoVar.peek();
	}

	public Tipo popTipoVar() {
		return pilhaTipoVar.pop();
	}

	public void pushTipoVar(Tipo tipoVar) {
		this.pilhaTipoVar.push(tipoVar);
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

	public OperadorRel popOperadorRel() {
		return this.pilhaOperadoresRel.pop();
	}

	public OperadorRel peekOperadorRel() {
		return this.pilhaOperadoresRel.peek();
	}

	public void pushOperadorRel(OperadorRel op) {
		this.pilhaOperadoresRel.push(op);
	}

	public OperadorMult popOperadorMult() {
		return this.pilhaOperadoresMult.pop();
	}

	public OperadorMult peekOperadorMult() {
		return this.pilhaOperadoresMult.peek();
	}

	public void pushOperadorMult(OperadorMult op) {
		this.pilhaOperadoresMult.push(op);
	}

	public OperadorAdd popOperadorAdd() {
		return this.pilhaOperadoresAdd.pop();
	}

	public OperadorAdd peekOperadorAdd() {
		return this.pilhaOperadoresAdd.peek();
	}

	public void pushOperadorAdd(OperadorAdd op) {
		this.pilhaOperadoresAdd.push(op);
	}

	public Boolean popRetorne() {
		return this.pilhaRetorne.pop();
	}

	public Boolean peekRetorne() {
		return this.pilhaRetorne.peek();
	}

	public void pushRetorne(Boolean retorne) {
		this.pilhaRetorne.push(retorne);
	}

	public boolean isOpNega() {
		return opNega;
	}

	public void setOpNega(boolean opNega) {
		this.opNega = opNega;
	}

	public boolean isOpUnario() {
		return opUnario;
	}

	public void setOpUnario(boolean opUnario) {
		this.opUnario = opUnario;
	}

}
