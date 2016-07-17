/**
 * @author Vitor Schweitzer e Caio Cardoso
 * created on 2016/06/18
 */
package br.ufsc.ctc.ine.sin.ine5622.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Responsável por realizar a análise semântica
 */
public class Semantico implements Constants {

	private static final String ACTION_METHOD_PREFIX = "action";

	private TabelaDeSimbolos tabelaDeSimbolos;
	private ContextoSemantico contextoSemantico;
	private boolean geradorCodigoAcionado = false;

	public Semantico() {
		tabelaDeSimbolos = new TabelaDeSimbolos();
		contextoSemantico = new ContextoSemantico();
	}

	/**
	 * Redireciona para a ação correta utilizando reflections
	 *
	 * @param action número de ação
	 * @param token o token atual
	 * @throws SemanticError erro semântico
	 */
	public void executeAction(int action, Token token) throws SemanticError {
		try {
			Method actionMethod = this.getClass().getMethod(ACTION_METHOD_PREFIX + action, Token.class);
			geradorCodigoAcionado = true;
			actionMethod.invoke(this, token);
		} catch (InvocationTargetException e) {
			Throwable t = e.getTargetException();
			e.printStackTrace();
			if (t instanceof SemanticError) {
				throw new SemanticError("#" + action + ": " + t.getMessage(), token.getPosition());
			}
			throw new SemanticError(t.getMessage(), token.getPosition());
		} catch (Exception e) {
			throw new SemanticError("Acao semântica inexistente (#" + action + ")");
		}
	}

	public void action101(Token token) throws SemanticError {
		tabelaDeSimbolos = new TabelaDeSimbolos();
		IdPrograma idPrograma = new IdPrograma(token.getLexeme());
		tabelaDeSimbolos.incluirIdentificador(idPrograma);
	}

	public void action102(Token token) throws SemanticError {
		contextoSemantico.setContextoLID(ContextoLID.DECL);
		contextoSemantico.setPrimeiraPosicaoListaDeclaracao(tabelaDeSimbolos.getDeslocamento());
		contextoSemantico.inicializaListaDeclaracao();
	}

	public void action103(Token token) throws SemanticError {
		contextoSemantico.setUltimaPosicaoListaDeclaracao(tabelaDeSimbolos.getDeslocamento());
	}

	public void action104(Token token) throws SemanticError {
		List<Identificador> listaDeclaracao = contextoSemantico.getListaDeclaracao();
		for (Identificador id : listaDeclaracao) {
			Identificador identificador;
			Categoria categoriaAtual = contextoSemantico.getCategoriaAtual();
			SubCategoria subCategoria = contextoSemantico.getSubCategoria();

			if (categoriaAtual.equals(Categoria.VARIAVEL)) {
				identificador = new IdVariavel(id);
			} else {
				IdConstante idConstante = new IdConstante(id);
				idConstante.setValor(contextoSemantico.getValConst());
				identificador = idConstante;
			}

			identificador.setTipo(contextoSemantico.getTipoAtual());

			if (subCategoria.equals(SubCategoria.VETOR)) {
				Integer intValConst = Integer.parseInt(contextoSemantico.getValConst());
				identificador.setTamanho(intValConst);
			} else {
				identificador.setTamanho(1);
			}

			identificador.setSubCategoria(subCategoria);

			tabelaDeSimbolos.incluirIdentificador(identificador);
		}
	}

	public void action105(Token token) throws SemanticError {
		contextoSemantico.setTipoAtual(Tipo.INTEIRO);
	}

	public void action106(Token token) throws SemanticError {
		contextoSemantico.setTipoAtual(Tipo.REAL);
	}

	public void action107(Token token) throws SemanticError {
		contextoSemantico.setTipoAtual(Tipo.BOOLEANO);
	}

	public void action108(Token token) throws SemanticError {
		contextoSemantico.setTipoAtual(Tipo.CARACTER);
	}

	public void action109(Token token) throws SemanticError {

		if (!contextoSemantico.getTipoConst().equals(Tipo.INTEIRO)) {
			throw new SemanticError("Esperava-se uma constante inteira", token.getPosition());
		}

		int valConst = Integer.parseInt(contextoSemantico.getValConst());
		if (valConst > 256) {
			throw new SemanticError("Cadeia com tamanho maior que o permitido (" + valConst + " > 256)",
					token.getPosition());
		} else {
			contextoSemantico.setTipoAtual(Tipo.CADEIA);
		}
	}

	public void action110(Token token) throws SemanticError {
		if (contextoSemantico.getTipoAtual() == Tipo.CADEIA) {
			throw new SemanticError("Vetor do tipo cadeia não é permitido", token.getPosition());
		} else {
			contextoSemantico.setSubCategoria(SubCategoria.VETOR);
		}
	}

	public void action111(Token token) throws SemanticError {
		if (contextoSemantico.getTipoConst() == Tipo.INTEIRO) {
			int valConst = Integer.parseInt(contextoSemantico.getValConst());
			contextoSemantico.setNumElementos(valConst);
		} else {
			throw new SemanticError("A dimensão deve ser uma constante inteira", token.getPosition());
		}
	}

	public void action112(Token token) throws SemanticError {
		if (contextoSemantico.getTipoAtual() == Tipo.CADEIA) {
			contextoSemantico.setSubCategoria(SubCategoria.CADEIA);
		} else {
			contextoSemantico.setSubCategoria(SubCategoria.PREDEFINIDO);
		}
	}

	public void action113(Token token) throws SemanticError {
		ContextoLID contextoLID = contextoSemantico.getContextoLID();
		Identificador id = tabelaDeSimbolos.getIdentificador(token.getLexeme());
		if (contextoLID.equals(ContextoLID.DECL)) {
			if (id != null) {
				throw new SemanticError("Id já declarado", token.getPosition());
			} else {
				id = new Identificador(token.getLexeme());
				contextoSemantico.getListaDeclaracao().add(id);
				tabelaDeSimbolos.incluirIdentificador(id);
			}
		} else if (contextoLID.equals(ContextoLID.PAR_FORMAL)) {
			if (id != null) {
				throw new SemanticError("Id de parâmetro repetido", token.getPosition());
			} else {
				contextoSemantico.incrementaNumParametrosFormais();
				id = new IdParametro(token.getLexeme());
				contextoSemantico.getListaDeclaracao().add(id);
				tabelaDeSimbolos.incluirIdentificador(id);
			}
		} else if (contextoLID.equals(ContextoLID.LEITURA)) {
			if (id == null) {
				throw new SemanticError("Id não declarado", token.getPosition());
			} else if (!categoriaValidaParaLeitura(id)) {
				throw new SemanticError("Categoria inválida para leitura", token.getPosition());
			} else if (!tipoValidoParaLeitura(id)) {
				throw new SemanticError("Tipo inválido para leitura", token.getPosition());
			} else {
				geradorCodigoAcionado = true;
			}
		}
	}

	public void action114(Token token) throws SemanticError {
		SubCategoria subCategoria = contextoSemantico.getSubCategoria();
		if (subCategoria.equals(SubCategoria.CADEIA) || subCategoria.equals(SubCategoria.VETOR)) {
			throw new SemanticError("Apenas id do tipo pré-definido podem ser declarados como constante",
					token.getPosition());
		} else {
			contextoSemantico.setCategoriaAtual(Categoria.CONSTANTE);
		}
	}

	public void action115(Token token) throws SemanticError {
		if (!contextoSemantico.getTipoConst().equals(contextoSemantico.getTipoAtual())) {
			throw new SemanticError("Tipo da constante incorreto", token.getPosition());
		}
	}

	public void action116(Token token) throws SemanticError {
		contextoSemantico.setCategoriaAtual(Categoria.VARIAVEL);
	}

	public void action117(Token token) throws SemanticError {
		Identificador id = tabelaDeSimbolos.getIdentificador(token.getLexeme());
		if (id != null) {
			throw new SemanticError("Id já declarado", token.getPosition());
		} else {
			IdMetodo idMetodo = new IdMetodo(token.getLexeme());
			tabelaDeSimbolos.incluirIdentificador(idMetodo);
			tabelaDeSimbolos.setNivelAtual(tabelaDeSimbolos.getNivelAtual() + 1);
			contextoSemantico.setNumParametrosFormais(0);
			contextoSemantico.pushIdMetodo(idMetodo);
			contextoSemantico.pushRetorne(false);
		}
	}

	public void action118(Token token) throws SemanticError {
		contextoSemantico.setNumParametrosFormais(contextoSemantico.getNumParametrosFormais() + 1);
	}

	public void action119(Token token) throws SemanticError {
		contextoSemantico.peekIdMetodo().setTipo(contextoSemantico.getTipoMetodo());
	}

	public void action120(Token token) throws SemanticError {
		IdMetodo idMetodo = contextoSemantico.popIdMetodo();
		tabelaDeSimbolos.limparNivelAtual();
		tabelaDeSimbolos.setNivelAtual(tabelaDeSimbolos.getNivelAtual() - 1);

		if (idMetodo.getTipo() != Tipo.NULO && !contextoSemantico.popRetorne()) {
			throw new SemanticError("Método com tipo de retorno deve ter comando de retorno", token.getPosition());
		}
	}

	public void action121(Token token) throws SemanticError {
		contextoSemantico.setContextoLID(ContextoLID.PAR_FORMAL);
		contextoSemantico.setPrimeiraPosicaoListaDeclaracao(tabelaDeSimbolos.getDeslocamento());
		contextoSemantico.inicializaListaDeclaracao();
	}

	public void action122(Token token) throws SemanticError {
		contextoSemantico.setUltimaPosicaoListaDeclaracao(tabelaDeSimbolos.getDeslocamento());
	}

	public void action123(Token token) throws SemanticError {
		if (!tipoPreDefinido(contextoSemantico.getTipoAtual())) {
			throw new SemanticError("Parametros devem ser de tipo pre-definido", token.getPosition());
		} else {
			List<Identificador> listaDeclaracao = contextoSemantico.getListaDeclaracao();
			for (Identificador id : listaDeclaracao) {
				IdParametro idParametro = new IdParametro(id);
				idParametro.setTipo(contextoSemantico.getTipoAtual());
				idParametro.setMpp(contextoSemantico.getMpp());
				IdMetodo idMetodo = contextoSemantico.peekIdMetodo();
				idMetodo.incluirParametro(idParametro);
				tabelaDeSimbolos.incluirIdentificador(idParametro);
			}
		}
	}

	public void action124(Token token) throws SemanticError {
		if (contextoSemantico.getTipoAtual() == Tipo.CADEIA) {
			throw new SemanticError("Métodos devem ser de tipo pre-definido", token.getPosition());
		} else {
			contextoSemantico.setTipoMetodo(contextoSemantico.getTipoAtual());
		}
	}

	public void action125(Token token) throws SemanticError {
		contextoSemantico.setTipoMetodo(Tipo.NULO);
	}

	public void action126(Token token) throws SemanticError {
		contextoSemantico.setMpp(MetodoPassagem.REFERENCIA);
	}

	public void action127(Token token) throws SemanticError {
		contextoSemantico.setMpp(MetodoPassagem.VALOR);
	}

	public void action128(Token token) throws SemanticError {
		Identificador id = tabelaDeSimbolos.getIdentificador(token.getLexeme());
		if (id == null) {
			throw new SemanticError("Identificador não declarado", token.getPosition());
		} else {
			contextoSemantico.pushId(id);
		}
	}

	public void action129(Token token) throws SemanticError {
		Tipo tipoExpr = contextoSemantico.popTipoExpr();
		if (tipoExpr != Tipo.BOOLEANO && tipoExpr != Tipo.INTEIRO) {
			throw new SemanticError("Tipo inválido de expressão", token.getPosition());
		} else {
			geradorCodigoAcionado = true;
		}
	}

	public void action130(Token token) throws SemanticError {
		contextoSemantico.setContextoLID(ContextoLID.LEITURA);
	}

	public void action131(Token token) throws SemanticError {
		contextoSemantico.setContextoEXPR(ContextoEXPR.IMPRESSAO);
	}

	public void action132(Token token) throws SemanticError {
		IdMetodo idMetodoAtual = contextoSemantico.peekIdMetodo();
		Tipo tipoExpr = contextoSemantico.popTipoExpr();
		if (idMetodoAtual.getTipo() == Tipo.NULO) {
			throw new SemanticError("'Retorne' só pode ser usado em Método com tipo", token.getPosition());
		} else {
			if (!tiposCompativeis(idMetodoAtual.getTipo(), tipoExpr)) {
				throw new SemanticError("Tipo de retorno inválido", token.getPosition());
			} else {
				geradorCodigoAcionado = true;
				contextoSemantico.popRetorne();
				contextoSemantico.pushRetorne(true);
			}
		}
	}

	public void action133(Token token) throws SemanticError {
		Identificador id = contextoSemantico.peekId();
		if (id instanceof IdVariavel || id instanceof IdParametro) {
			if (id.getSubCategoria() == SubCategoria.VETOR) {
				throw new SemanticError("Id deveria ser indexado", token.getPosition());
			} else {
				contextoSemantico.setTipoLadoEsq(id.getTipo());
			}
		} else {
			throw new SemanticError("Id deveria ser variável ou parâmetro", token.getPosition());
		}
	}

	public void action134(Token token) throws SemanticError {
		Tipo tipoExpr = contextoSemantico.popTipoExpr();
		if (!tiposCompativeis(contextoSemantico.getTipoLadoEsq(), tipoExpr)) {
			throw new SemanticError("Tipos incompatíveis", token.getPosition());
		} else {
			contextoSemantico.popId();
			geradorCodigoAcionado = true;
		}
	}

	public void action135(Token token) throws SemanticError {
		Identificador id = contextoSemantico.peekId();
		if (!(id instanceof IdVariavel)) {
			throw new SemanticError("Esperava-se uma variável", token.getPosition());
		} else {
			SubCategoria subCategoria = id.getSubCategoria();
			if (subCategoria != SubCategoria.VETOR && subCategoria != SubCategoria.CADEIA) {
				throw new SemanticError("Apenas vetores e cadeias podem ser indexados", token.getPosition());
			} else {
				contextoSemantico.pushSubCategoriaVarIndexada(subCategoria);
			}
		}
	}

	public void action136(Token token) throws SemanticError {
		SubCategoria subCategoriaVarIndexada = contextoSemantico.popSubCategoriaVarIndexada();
		Tipo tipoExpr = contextoSemantico.popTipoExpr();
		if (tipoExpr != Tipo.INTEIRO) {
			throw new SemanticError("Índice deveria ser inteiro", token.getPosition());
		} else {
			if (subCategoriaVarIndexada == SubCategoria.CADEIA) {
				contextoSemantico.setTipoLadoEsq(Tipo.CARACTER);
			} else {
				Identificador id = contextoSemantico.peekId();
				contextoSemantico.setTipoLadoEsq(id.getTipo());
			}
		}
	}

	public void action137(Token token) throws SemanticError {
		Identificador id = contextoSemantico.peekId();
		if (!(id instanceof IdMetodo)) {
			throw new SemanticError("Id deveria ser um método", token.getPosition());
		} else if (id.getTipo() != Tipo.NULO) {
			throw new SemanticError("Esperava-se método sem tipo", token.getPosition());
		}
	}

	public void action138(Token token) throws SemanticError {
		ContextoMetodo contextoMetodo = new ContextoMetodo();
		contextoSemantico.pushContextoMetodo(contextoMetodo);
		contextoSemantico.setContextoEXPR(ContextoEXPR.PAR_ATUAL);
	}

	public void action139(Token token) throws SemanticError {
		ContextoMetodo contextoMetodo = contextoSemantico.popContextoMetodo();
		IdMetodo id = (IdMetodo) contextoSemantico.popId();
		if (contextoMetodo.getNumParametrosAtuais() != id.getNumParametrosFormais()) {
			throw new SemanticError("Erro na quantidade de parâmetros", token.getPosition());
		} else {
			geradorCodigoAcionado = true;
		}
	}

	public void action140(Token token) throws SemanticError {
		Identificador id = contextoSemantico.popId();
		System.out.println(id.getNome());
		if (!(id instanceof IdMetodo)) {
			throw new SemanticError("Id Deveria ser um método", token.getPosition());
		} else if (id.getTipo() != Tipo.NULO) {
			throw new SemanticError("Esperava-se método sem tipo", token.getPosition());
		} else if (((IdMetodo) id).getParametros().size() != 0) {
			throw new SemanticError("Erro na quantidade de parâmetros", token.getPosition());
		} else {
			geradorCodigoAcionado = true;
		}
	}

	public void action141(Token token) throws SemanticError {
		ContextoEXPR contextoEXPR = contextoSemantico.getContextoEXPR();
		Tipo tipoExpr = contextoSemantico.popTipoExpr();

		if (contextoEXPR == ContextoEXPR.PAR_ATUAL) {
			ContextoMetodo contextoMetodo = contextoSemantico.peekContextoMetodo();
			IdMetodo idMetodo = (IdMetodo) contextoSemantico.peekId();
			int indiceParametro = contextoMetodo.getNumParametrosAtuais();
			contextoMetodo.setNumParametrosAtuais(indiceParametro + 1);
			if (idMetodo.getNumParametrosFormais() < indiceParametro + 1) {
				throw new SemanticError("Erro na quantidade de parâmetros", token.getPosition());
			}

			IdParametro idParametro = idMetodo.getParametros().get(indiceParametro);
			Tipo tipoParametro = idParametro.getTipo();
			if (!tiposCompativeis(tipoParametro, tipoExpr)) {
				throw new SemanticError("Esperava-se parâmetro do tipo '" + tipoParametro.getNome() + "', ao invés de '"
						+ tipoExpr.getNome() + "'", token.getPosition());
			}

			if (idParametro.getMpp() == MetodoPassagem.REFERENCIA) {
				Identificador id = tabelaDeSimbolos.getIdentificador(token.getLexeme());
				System.out.println(token.getLexeme());
				if (id == null) {
					throw new SemanticError("Identificador não declarado", token.getPosition());
				}
				if (!categoriaValidaParaLeitura(id)) {
					throw new SemanticError(
							"Parâmetro com método de passagem referência devem ser de categoria variável ou parâmetro",
							token.getPosition());
				}
			}
		} else if (contextoEXPR == ContextoEXPR.IMPRESSAO) {
			if (tipoExpr == Tipo.BOOLEANO) {
				throw new SemanticError("Tipo inválido para impressão", token.getPosition());
			} else {
				geradorCodigoAcionado = true;
			}
		}
	}

	public void action142(Token token) throws SemanticError {
		contextoSemantico.pushTipoExpr(contextoSemantico.popTipoExprSimples());
	}

	public void action143(Token token) throws SemanticError {
		Tipo tipoExpr = contextoSemantico.popTipoExpr();
		Tipo tipoExprSimples = contextoSemantico.popTipoExprSimples();
		if (tiposCompativeis(tipoExpr, tipoExprSimples) || tiposCompativeis(tipoExprSimples, tipoExpr)) {
			contextoSemantico.pushTipoExpr(Tipo.BOOLEANO);
		} else {
			throw new SemanticError("Operandos incompatíveis", token.getPosition());
		}
	}

	public void action144(Token token) throws SemanticError {
		contextoSemantico.pushOperadorRel(OperadorRel.EQ);
	}

	public void action145(Token token) throws SemanticError {
		contextoSemantico.pushOperadorRel(OperadorRel.LT);
	}

	public void action146(Token token) throws SemanticError {
		contextoSemantico.pushOperadorRel(OperadorRel.GT);
	}

	public void action147(Token token) throws SemanticError {
		contextoSemantico.pushOperadorRel(OperadorRel.GE);
	}

	public void action148(Token token) throws SemanticError {
		contextoSemantico.pushOperadorRel(OperadorRel.LE);
	}

	public void action149(Token token) throws SemanticError {
		contextoSemantico.pushOperadorRel(OperadorRel.NE);
	}

	public void action150(Token token) throws SemanticError {
		contextoSemantico.pushTipoExprSimples(contextoSemantico.popTipoTermo());
	}

	public void action151(Token token) throws SemanticError {
		OperadorAdd op = contextoSemantico.peekOperadorAdd();
		Tipo tipoExprSimples = contextoSemantico.peekTipoExprSimples();
		if (!operadorCompativel(op, tipoExprSimples)) {
			throw new SemanticError("Operador e Operando incompatíveis", token.getPosition());
		}
	}

	public void action152(Token token) throws SemanticError {
		Tipo tipoExprSimples = contextoSemantico.popTipoExprSimples();
		Tipo tipoTermo = contextoSemantico.popTipoTermo();
		if (tiposCompativeis(tipoExprSimples, tipoTermo) || tiposCompativeis(tipoTermo, tipoExprSimples)) {
			geradorCodigoAcionado = true;
			if (tipoExprSimples != Tipo.BOOLEANO && tipoExprSimples != tipoTermo) {
				contextoSemantico.pushTipoExprSimples(Tipo.REAL);
			} else {
				contextoSemantico.pushTipoExprSimples(tipoExprSimples);
			}
		} else {
			throw new SemanticError("Operandos incompatíveis", token.getPosition());
		}
	}

	public void action153(Token token) throws SemanticError {
		contextoSemantico.pushOperadorAdd(OperadorAdd.ADD);
	}

	public void action154(Token token) throws SemanticError {
		contextoSemantico.pushOperadorAdd(OperadorAdd.SUB);
	}

	public void action155(Token token) throws SemanticError {
		contextoSemantico.pushOperadorAdd(OperadorAdd.OU);
	}

	public void action156(Token token) throws SemanticError {
		contextoSemantico.pushTipoTermo(contextoSemantico.popTipoFator());
	}

	public void action157(Token token) throws SemanticError {
		OperadorMult op = contextoSemantico.peekOperadorMult();
		Tipo tipoTermo = contextoSemantico.peekTipoTermo();
		if (!operadorCompativel(op, tipoTermo)) {
			throw new SemanticError("Operador e Operando incompatíveis", token.getPosition());
		}
	}

	public void action158(Token token) throws SemanticError {
		Tipo tipoTermo = contextoSemantico.popTipoTermo();
		Tipo tipoFator = contextoSemantico.popTipoFator();
		OperadorMult operadorMult = contextoSemantico.peekOperadorMult();
		if (tiposCompativeis(tipoTermo, tipoFator) || tiposCompativeis(tipoFator, tipoTermo)) {
			geradorCodigoAcionado = true;
			if (operadorMult == OperadorMult.FRC) {
				contextoSemantico.pushTipoTermo(Tipo.REAL);
			} else if (operadorMult == OperadorMult.DIV) {
				if (tipoFator != Tipo.INTEIRO || tipoTermo != Tipo.INTEIRO) {
					throw new SemanticError("Operador e Operando incompatíveis", token.getPosition());
				} else {
					contextoSemantico.pushTipoTermo(Tipo.INTEIRO);
				}
			} else if (tipoTermo != tipoFator) {
				contextoSemantico.pushTipoTermo(Tipo.REAL);
			} else {
				contextoSemantico.pushTipoTermo(tipoTermo);
			}
		} else {
			throw new SemanticError("Operador e Operando incompatíveis", token.getPosition());
		}
	}

	public void action159(Token token) throws SemanticError {
		contextoSemantico.pushOperadorMult(OperadorMult.MUL);
	}

	public void action160(Token token) throws SemanticError {
		contextoSemantico.pushOperadorMult(OperadorMult.FRC);
	}

	public void action161(Token token) throws SemanticError {
		contextoSemantico.pushOperadorMult(OperadorMult.E);
	}

	public void action162(Token token) throws SemanticError {
		contextoSemantico.pushOperadorMult(OperadorMult.DIV);
	}

	public void action163(Token token) throws SemanticError {
		if (contextoSemantico.isOpNega()) {
			throw new SemanticError("Operador 'não' repetido - não pode!", token.getPosition());
		} else {
			contextoSemantico.setOpNega(true);
		}
	}

	public void action164(Token token) throws SemanticError {
		if (contextoSemantico.peekTipoFator() == Tipo.BOOLEANO) {
			contextoSemantico.setOpNega(false);
		} else {
			throw new SemanticError("Operador 'não' exige operando booleano!", token.getPosition());
		}
	}

	public void action165(Token token) throws SemanticError {
		if (contextoSemantico.isOpUnario()) {
			throw new SemanticError("Operador unário repetido", token.getPosition());
		} else {
			contextoSemantico.setOpUnario(true);
		}
	}

	public void action166(Token token) throws SemanticError {
		Tipo tipoFator = contextoSemantico.peekTipoFator();
		if (tipoFator != Tipo.INTEIRO && tipoFator != Tipo.REAL) {
			throw new SemanticError("Operador unário exige operando numérico", token.getPosition());
		} else {
			contextoSemantico.setOpUnario(false);
		}
	}

	public void action167(Token token) throws SemanticError {
		contextoSemantico.setOpNega(false);
		contextoSemantico.setOpUnario(false);
	}

	public void action168(Token token) throws SemanticError {
		contextoSemantico.pushTipoFator(contextoSemantico.popTipoExpr());
	}

	public void action169(Token token) throws SemanticError {
		contextoSemantico.pushTipoFator(contextoSemantico.popTipoVar());
		contextoSemantico.popId();

	}

	public void action170(Token token) throws SemanticError {
		contextoSemantico.pushTipoFator(contextoSemantico.getTipoConst());
	}

	public void action171(Token token) throws SemanticError {
		Identificador id = contextoSemantico.peekId();
		if (!(id instanceof IdMetodo)) {
			throw new SemanticError("Id deveria ser um método", token.getPosition());
		} else if (id.getTipo() == Tipo.NULO) {
			throw new SemanticError("Esperava-se método com tipo", token.getPosition());
		} else {
			ContextoMetodo contextoMetodo = new ContextoMetodo();
			contextoSemantico.pushContextoMetodo(contextoMetodo);
			contextoSemantico.setContextoEXPR(ContextoEXPR.PAR_ATUAL);
		}
	}

	public void action172(Token token) throws SemanticError {
		ContextoMetodo contextoMetodo = contextoSemantico.popContextoMetodo();
		IdMetodo id = (IdMetodo) contextoSemantico.peekId();

		if (id.getNumParametrosFormais() == contextoMetodo.getNumParametrosAtuais()) {
			contextoSemantico.pushTipoVar(id.getTipo());
			geradorCodigoAcionado = true;
		} else {
			throw new SemanticError("Erro na quantidade de parâmetros", token.getPosition());
		}
	}

	public void action173(Token token) throws SemanticError {
		if (contextoSemantico.popTipoExpr() != Tipo.INTEIRO) {
			throw new SemanticError("Índice deveria ser inteiro", token.getPosition());
		} else if (contextoSemantico.popSubCategoriaVarIndexada() == SubCategoria.CADEIA) {
			contextoSemantico.pushTipoVar(Tipo.CARACTER);
		} else {
			contextoSemantico.pushTipoVar(contextoSemantico.peekId().getTipo());
		}
	}

	public void action174(Token token) throws SemanticError {
		Identificador id = contextoSemantico.peekId();
		if (id instanceof IdVariavel || id instanceof IdParametro) {
			if (id.getSubCategoria() == SubCategoria.VETOR) {
				throw new SemanticError("Vetor deve ser indexado", token.getPosition());
			} else {
				contextoSemantico.pushTipoVar(id.getTipo());
			}
		} else if (id instanceof IdMetodo) {
			if (id.getTipo() == Tipo.NULO) {
				throw new SemanticError("Esperava-se método com tipo", token.getPosition());
			} else {
				IdMetodo idMetodo = (IdMetodo) id;
				if (idMetodo.getNumParametrosFormais() != 0) {
					throw new SemanticError("Erro na quantidade de parâmetros", token.getPosition());
				} else {
					contextoSemantico.pushTipoVar(idMetodo.getTipo());
					geradorCodigoAcionado = true;
				}
			}
		} else if (id instanceof IdConstante) {
			contextoSemantico.pushTipoVar(id.getTipo());
		} else {
			throw new SemanticError("Esperava-se id de categoria variável, método ou constante", token.getPosition());
		}
	}

	public void action175(Token token) throws SemanticError {
		Identificador id = tabelaDeSimbolos.getIdentificador(token.getLexeme());
		if (id == null) {
			throw new SemanticError("Id não declarado", token.getPosition());
		} else if (!(id instanceof IdConstante)) {
			throw new SemanticError("Id de constante esperado", token.getPosition());
		} else {
			IdConstante idConstante = (IdConstante) id;
			contextoSemantico.setTipoConst(id.getTipo());
			contextoSemantico.setValConst(idConstante.getValor());
		}
	}

	public void action176(Token token) throws SemanticError {
		contextoSemantico.setTipoConst(Tipo.INTEIRO);
		contextoSemantico.setValConst(token.getLexeme());
	}

	public void action177(Token token) throws SemanticError {
		contextoSemantico.setTipoConst(Tipo.REAL);
		contextoSemantico.setValConst(token.getLexeme());
	}

	public void action178(Token token) throws SemanticError {
		contextoSemantico.setTipoConst(Tipo.BOOLEANO);
		contextoSemantico.setValConst(token.getLexeme());
	}

	public void action179(Token token) throws SemanticError {
		contextoSemantico.setTipoConst(Tipo.BOOLEANO);
		contextoSemantico.setValConst(token.getLexeme());
	}

	public void action180(Token token) throws SemanticError {
		if (token.getLexeme().length() > 3) {
			contextoSemantico.setTipoConst(Tipo.CADEIA);
		} else {
			contextoSemantico.setTipoConst(Tipo.CARACTER);
		}
		contextoSemantico.setValConst(token.getLexeme());
	}

	public TabelaDeSimbolos getTabelaDeSimbolos() {
		return tabelaDeSimbolos;
	}

	public void setTabelaDeSimbolos(TabelaDeSimbolos tabelaDeSimbolos) {
		this.tabelaDeSimbolos = tabelaDeSimbolos;
	}

	public ContextoSemantico getContextoSemantico() {
		return contextoSemantico;
	}

	public void setContextoSemantico(ContextoSemantico contextoSemantico) {
		this.contextoSemantico = contextoSemantico;
	}

	public boolean isGeradorCodigoAcionado() {
		return geradorCodigoAcionado;
	}

	public void setGeradorCodigoAcionado(boolean geradorCodigoAcionado) {
		this.geradorCodigoAcionado = geradorCodigoAcionado;
	}

	private boolean categoriaValidaParaLeitura(Identificador id) {
		return id instanceof IdVariavel || id instanceof IdParametro;
	}

	private boolean tipoValidoParaLeitura(Identificador id) {
		return id.getSubCategoria() == SubCategoria.CADEIA
				|| (id.getSubCategoria() == SubCategoria.PREDEFINIDO && id.getTipo() != Tipo.BOOLEANO);
	}

	private boolean tipoPreDefinido(Tipo tipo) {
		return tipo == Tipo.BOOLEANO || tipo == Tipo.CARACTER || tipo == Tipo.INTEIRO || tipo == Tipo.REAL;
	}

	private boolean tiposCompativeis(Tipo tipoLadoEsq, Tipo tipoExpr) {
		return (tipoLadoEsq == tipoExpr) || (tipoLadoEsq == Tipo.REAL && tipoExpr == Tipo.INTEIRO)
				|| (tipoLadoEsq == Tipo.CADEIA && tipoExpr == Tipo.CARACTER);
	}

	private boolean operadorCompativel(OperadorAdd op, Tipo tipo) {
		if (op == OperadorAdd.ADD || op == OperadorAdd.SUB) {
			return tipo == Tipo.INTEIRO || tipo == Tipo.REAL;
		} else {
			return tipo == Tipo.BOOLEANO;
		}
	}

	private boolean operadorCompativel(OperadorMult op, Tipo tipo) {
		if (op == OperadorMult.MUL || op == OperadorMult.FRC) {
			return tipo == Tipo.INTEIRO || tipo == Tipo.REAL;
		} else if (op == OperadorMult.DIV) {
			return tipo == Tipo.INTEIRO;
		} else {
			return tipo == Tipo.BOOLEANO;
		}
	}

}
