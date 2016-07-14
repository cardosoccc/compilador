package br.ufsc.ctc.ine.sin.ine5622.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class Semantico implements Constants {

    private static final String ACTION_METHOD_PREFIX = "action";

    private TabelaDeSimbolos tabelaDeSimbolos;

    private ContextoSemantico contextoSemantico;

    private boolean geradorCodigoAcionado = false;

    public Semantico() {
    	this.tabelaDeSimbolos = new TabelaDeSimbolos();
    	this.contextoSemantico = new ContextoSemantico();
    }

	public void executeAction(int action, Token token)	throws SemanticError
    {
		try {
			Method actionMethod = this.getClass().getMethod(ACTION_METHOD_PREFIX + action, Token.class);
			this.geradorCodigoAcionado = true;
			actionMethod.invoke(this, token);
		} catch (InvocationTargetException e) {
			Throwable t = e.getTargetException();
//			e.printStackTrace();
			if (t instanceof SemanticError) {
				throw new SemanticError("#" + action + ": " + t.getMessage(), token.getPosition());
			}
			throw new SemanticError(t.getMessage(), token.getPosition());
		} catch (Exception e) {
			throw new SemanticError("Acao semântica inexistente (#" + action + ")");
		}
    }

	public void action101(Token token) throws SemanticError {
		this.tabelaDeSimbolos = new TabelaDeSimbolos();
		IdPrograma idPrograma = new IdPrograma(token.getLexeme());
		this.tabelaDeSimbolos.incluirIdentificador(idPrograma);
	}

	public void action102(Token token) throws SemanticError {
		this.contextoSemantico.setContextoLID(ContextoLID.DECL);
		this.contextoSemantico.setPrimeiraPosicaoListaDeclaracao(this.tabelaDeSimbolos.getDeslocamento());
		this.contextoSemantico.inicializaListaDeclaracao();
	}

	public void action103(Token token) throws SemanticError {
		this.contextoSemantico.setUltimaPosicaoListaDeclaracao(this.tabelaDeSimbolos.getDeslocamento());
	}

	public void action104(Token token) throws SemanticError {
		List<Identificador> listaDeclaracao = this.contextoSemantico.getListaDeclaracao();
		for (Identificador id : listaDeclaracao) {
			Identificador identificador;
			Categoria categoriaAtual = this.contextoSemantico.getCategoriaAtual();
			SubCategoria subCategoria = this.contextoSemantico.getSubCategoria();

			if (categoriaAtual.equals(Categoria.VARIAVEL)) {
				identificador = new IdVariavel(id);
			} else {
				IdConstante idConstante = new IdConstante(id);
				idConstante.setValor(this.contextoSemantico.getValConst());
				identificador = idConstante;
			}

			identificador.setTipo(this.contextoSemantico.getTipoAtual());

			if (subCategoria.equals(SubCategoria.VETOR)) {
				Integer intValConst = Integer.parseInt(this.getContextoSemantico().getValConst());
				identificador.setTamanho(intValConst);
			} else {
				identificador.setTamanho(1);
			}

			this.tabelaDeSimbolos.incluirIdentificador(identificador);
		}
	}

	public void action105(Token token) throws SemanticError {
		this.contextoSemantico.setTipoAtual(Tipo.INTEIRO);
	}

	public void action106(Token token) throws SemanticError {
		this.contextoSemantico.setTipoAtual(Tipo.REAL);
	}

	public void action107(Token token) throws SemanticError {
		this.contextoSemantico.setTipoAtual(Tipo.BOOLEANO);
	}

	public void action108(Token token) throws SemanticError {
		this.contextoSemantico.setTipoAtual(Tipo.CARACTER);
	}

	public void action109(Token token) throws SemanticError {

		if (!this.contextoSemantico.getTipoConst().equals(Tipo.INTEIRO)) {
			throw new SemanticError("Esperava-se uma constante inteira", token.getPosition());
		}

		int valConst = Integer.parseInt(this.getContextoSemantico().getValConst());
		if (valConst > 256) {
			throw new SemanticError("Cadeia com tamanho maior que o permitido (" + valConst + " > 256)", token.getPosition());
		} else {
			this.contextoSemantico.setTipoAtual(Tipo.CADEIA);
		}
	}

	public void action110(Token token) throws SemanticError {
		if (this.contextoSemantico.getTipoAtual() == Tipo.CADEIA) {
			throw new SemanticError("Vetor do tipo cadeia não é permitido", token.getPosition());
		} else {
			this.contextoSemantico.setSubCategoria(SubCategoria.VETOR);
		}
	}

	public void action111(Token token) throws SemanticError {
		if (!this.contextoSemantico.getTipoConst().equals(Tipo.INTEIRO)) {
			throw new SemanticError("A dimensão deve ser uma constante inteira", token.getPosition());
		} else {
			int valConst = Integer.parseInt(this.getContextoSemantico().getValConst());
			this.contextoSemantico.setNumElementos(valConst);
		}
	}

	public void action112(Token token) throws SemanticError {
		if (this.contextoSemantico.getTipoAtual().equals(Tipo.CADEIA)) {
			this.contextoSemantico.setSubCategoria(SubCategoria.CADEIA);
		} else {
			this.contextoSemantico.setSubCategoria(SubCategoria.PREDEFINIDO);
		}
	}

	public void action113(Token token) throws SemanticError {
		ContextoLID contextoLID = this.contextoSemantico.getContextoLID();
		Identificador id = this.tabelaDeSimbolos.getIdentificador(token.getLexeme());
		if (contextoLID.equals(ContextoLID.DECL)) {
			if (id != null) {
				throw new SemanticError("Id já declarado", token.getPosition());
			} else {
				id = new Identificador(token.getLexeme());
				this.contextoSemantico.getListaDeclaracao().add(id);
				this.tabelaDeSimbolos.incluirIdentificador(id);
			}
		} else if (contextoLID.equals(ContextoLID.PAR_FORMAL)) {
			if (id != null) {
				throw new SemanticError("Id de parâmetro repetido", token.getPosition());
			} else {
				this.contextoSemantico.incrementaNumParametrosFormais();
				id = new IdParametro(token.getLexeme());
				this.contextoSemantico.getListaDeclaracao().add(id);
				this.tabelaDeSimbolos.incluirIdentificador(id);
			}
		} else if (contextoLID.equals(ContextoLID.LEITURA)) {
			if (id == null) {
				throw new SemanticError("Id não declarado", token.getPosition());
			} else if (!categoriaValidaParaLeitura(id)) {
				throw new SemanticError("Categoria inválida para leitura", token.getPosition());
			} else if (!tipoValidoParaLeitura((IdComSubCategoria) id)) {
				throw new SemanticError("Tipo inválido para leitura", token.getPosition());
			} else {
				this.geradorCodigoAcionado = true;
			}
		}
	}

	public void action114(Token token) throws SemanticError {
		SubCategoria subCategoria = this.contextoSemantico.getSubCategoria();
		if (subCategoria.equals(SubCategoria.CADEIA) || subCategoria.equals(SubCategoria.VETOR)) {
			throw new SemanticError("Apenas id do tipo pré-definido podem ser declarados como constante", token.getPosition());
		} else {
			this.contextoSemantico.setCategoriaAtual(Categoria.CONSTANTE);
		}
	}

	public void action115(Token token) throws SemanticError {
		if (!this.contextoSemantico.getTipoConst().equals(this.contextoSemantico.getTipoAtual())) {
			throw new SemanticError("Tipo da constante incorreto", token.getPosition());
		}
	}

	public void action116(Token token) throws SemanticError {
		this.contextoSemantico.setCategoriaAtual(Categoria.VARIAVEL);
	}

	public void action117(Token token) throws SemanticError {
		Identificador id = this.tabelaDeSimbolos.getIdentificador(token.getLexeme());
		if (id != null) {
			throw new SemanticError("Id já declarado", token.getPosition());
		} else {
			IdMetodo idMetodo = new IdMetodo(token.getLexeme());
			this.tabelaDeSimbolos.incluirIdentificador(idMetodo);
			this.tabelaDeSimbolos.setNivelAtual(this.tabelaDeSimbolos.getNivelAtual() + 1);
			this.contextoSemantico.setNumParametrosFormais(0);
			this.contextoSemantico.setIdMetodoAtual(idMetodo);
		}
	}

	public void action118(Token token) throws SemanticError {
		this.contextoSemantico.setNumParametrosFormais(this.contextoSemantico.getNumParametrosFormais() + 1);
	}

	public void action119(Token token) throws SemanticError {
		this.contextoSemantico.getIdMetodoAtual().setTipo(this.contextoSemantico.getTipoMetodo());
	}

	public void action120(Token token) throws SemanticError {
		this.tabelaDeSimbolos.limparNivelAtual();
		this.tabelaDeSimbolos.setNivelAtual(this.tabelaDeSimbolos.getNivelAtual() - 1);
	}

	public void action121(Token token) throws SemanticError {
		this.contextoSemantico.setContextoLID(ContextoLID.PAR_FORMAL);
		this.contextoSemantico.setPrimeiraPosicaoListaDeclaracao(this.tabelaDeSimbolos.getDeslocamento());
		this.contextoSemantico.inicializaListaDeclaracao();
	}

	public void action122(Token token) throws SemanticError {
		this.contextoSemantico.setUltimaPosicaoListaDeclaracao(this.tabelaDeSimbolos.getDeslocamento());
	}

	public void action123(Token token) throws SemanticError {
		if (!tipoPreDefinido(this.contextoSemantico.getTipoAtual())) {
			throw new SemanticError("Parametros devem ser de tipo pre-definido", token.getPosition());
		} else {
			List<Identificador> listaDeclaracao = this.contextoSemantico.getListaDeclaracao();
			for (Identificador id : listaDeclaracao) {
				IdParametro idParametro = new IdParametro(id);
				idParametro.setTipo(this.contextoSemantico.getTipoAtual());
				idParametro.setMpp(this.contextoSemantico.getMpp());
				IdMetodo idMetodo = this.contextoSemantico.getIdMetodoAtual();
				idMetodo.incluirParametro(idParametro);
				this.tabelaDeSimbolos.incluirIdentificador(idParametro);
			}
		}
	}

	public void action124(Token token) throws SemanticError {
		if (this.contextoSemantico.getTipoAtual().equals(Tipo.CADEIA)) {
			throw new SemanticError("Métodos devem ser de tipo pre-definido", token.getPosition());
		} else {
			this.contextoSemantico.setTipoMetodo(this.contextoSemantico.getTipoAtual());
		}
	}

	public void action125(Token token) throws SemanticError {
		this.contextoSemantico.setTipoMetodo(Tipo.NULO);
	}

	public void action126(Token token) throws SemanticError {
		this.contextoSemantico.setMpp(Mpp.REFERENCIA);
	}

	public void action127(Token token) throws SemanticError {
		this.contextoSemantico.setMpp(Mpp.VALOR);
	}

	public void action128(Token token) throws SemanticError {
		Identificador id = this.tabelaDeSimbolos.getIdentificador(token.getLexeme());
		if (id == null) {
			throw new SemanticError("Identificador não declarado", token.getPosition());
		} else {
			this.contextoSemantico.pushId(id);
		}
	}

	public void action129(Token token) throws SemanticError {
		Tipo tipoExpr = this.contextoSemantico.popTipoExpr();
		if (tipoExpr != Tipo.BOOLEANO && tipoExpr != Tipo.INTEIRO) {
			throw new SemanticError("Tipo inválido de expressão", token.getPosition());
		} else {
			this.geradorCodigoAcionado = true;
		}
	}

	public void action130(Token token) throws SemanticError {
		this.contextoSemantico.setContextoLID(ContextoLID.LEITURA);
	}

	public void action131(Token token) throws SemanticError {
		this.contextoSemantico.pushContextoEXPR(ContextoEXPR.IMPRESSAO);
	}

	public void action132(Token token) throws SemanticError {
		IdMetodo idMetodoAtual = this.contextoSemantico.getIdMetodoAtual();
		if (idMetodoAtual.getTipo() == Tipo.NULO) {
			throw new SemanticError("'Retorne' só pode ser usado em Método com tipo", token.getPosition());
		} else if (!tiposCompativeis(idMetodoAtual.getTipo(), this.contextoSemantico.popTipoExpr())) {
			throw new SemanticError("Tipo de retorno inválido", token.getPosition());
		} else {
			this.geradorCodigoAcionado = true;
		}
	}

	public void action133(Token token) throws SemanticError {
		Identificador id = this.contextoSemantico.peekId();
		if (id instanceof IdVariavel || id instanceof IdParametro) {
			IdComSubCategoria idSub = (IdComSubCategoria) id;
			if (idSub.getSubCategoria() == SubCategoria.VETOR) {
				throw new SemanticError("Id deveria ser indexado", token.getPosition());
			} else {
				this.contextoSemantico.setTipoLadoEsq(id.getTipo());
			}
		} else {
			throw new SemanticError("Id deveria ser variável ou parâmetro", token.getPosition());
		}
	}

	public void action134(Token token) throws SemanticError {
		if (!tiposCompativeis(this.contextoSemantico.getTipoLadoEsq(), this.contextoSemantico.popTipoExpr())) {
			throw new SemanticError("Tipos incompatíveis", token.getPosition());
		} else {
			this.contextoSemantico.limparContextoId();
			this.geradorCodigoAcionado = true;
		}
	}


	public void action135(Token token) throws SemanticError {
		Identificador id = this.contextoSemantico.peekId();
		if (!(id instanceof IdVariavel)) {
			throw new SemanticError("Esperava-se uma variável", token.getPosition());
		} else {
			IdComSubCategoria idSub = (IdComSubCategoria) id;
			SubCategoria subCategoria = idSub.getSubCategoria();
			if (subCategoria != SubCategoria.VETOR && subCategoria != SubCategoria.CADEIA) {
				throw new SemanticError("Apenas vetores e cadeias podem ser indexados", token.getPosition());
			} else {
				this.contextoSemantico.pushSubCategoriaVarIndexada(subCategoria);
			}
		}
	}

	public void action136(Token token) throws SemanticError {
		if (this.contextoSemantico.popTipoExpr() != Tipo.INTEIRO) {
			throw new SemanticError("Índice deveria ser inteiro", token.getPosition());
		} else if (this.contextoSemantico.popSubCategoriaVarIndexada() == SubCategoria.CADEIA) {
			this.contextoSemantico.setTipoLadoEsq(Tipo.CARACTER);
		} else {
			Identificador id = this.contextoSemantico.peekId();
			this.contextoSemantico.setTipoLadoEsq(id.getTipo());
		}
	}

	public void action137(Token token) throws SemanticError {
		Identificador id = this.contextoSemantico.peekId();
		if (!(id instanceof IdMetodo)) {
			throw new SemanticError("Id deveria ser um método", token.getPosition());
		} else if (id.getTipo() != Tipo.NULO) {
			throw new SemanticError("Esperava-se método sem tipo", token.getPosition());
		}
	}

	public void action138(Token token) throws SemanticError {
		ContextoMetodo contextoMetodo = new ContextoMetodo();
		this.contextoSemantico.pushContextoMetodo(contextoMetodo);
		this.contextoSemantico.pushContextoEXPR(ContextoEXPR.PAR_ATUAL);
	}

	public void action139(Token token) throws SemanticError {
		ContextoMetodo contextoMetodo = this.contextoSemantico.popContextoMetodo();
		IdMetodo id = (IdMetodo) this.contextoSemantico.popId();
		if (contextoMetodo.getNumParametrosAtuais() != id.getNumParametrosFormais()) {
			throw new SemanticError("Erro na quantidade de parâmetros", token.getPosition());
		} else {
			this.geradorCodigoAcionado = true;
		}
	}

	public void action140(Token token) throws SemanticError {
		Identificador id = this.contextoSemantico.popId();
		if (!(id instanceof IdMetodo)) {
			throw new SemanticError("Id Deveria ser um método", token.getPosition());
		} else if (id.getTipo() != Tipo.NULO) {
			throw new SemanticError("Esperava-se método sem tipo", token.getPosition());
		} else if (((IdMetodo)id).getParametros().size() != 0) {
			throw new SemanticError("Erro na quantidade de parâmetros", token.getPosition());
		} else {
			this.geradorCodigoAcionado = true;
		}
	}

	public void action141(Token token) throws SemanticError {
		// TODO
	}

	public void action142(Token token) throws SemanticError {
		// TODO
	}

	public void action143(Token token) throws SemanticError {
		// TODO
	}

	public void action144(Token token) throws SemanticError {
		// TODO
	}

	public void action145(Token token) throws SemanticError {
		// TODO
	}

	public void action146(Token token) throws SemanticError {
		// TODO
	}

	public void action147(Token token) throws SemanticError {
		// TODO
	}

	public void action148(Token token) throws SemanticError {
		// TODO
	}

	public void action149(Token token) throws SemanticError {
		// TODO
	}

	public void action150(Token token) throws SemanticError {
		// TODO
	}

	public void action151(Token token) throws SemanticError {
		// TODO
	}

	public void action152(Token token) throws SemanticError {
		// TODO
	}

	public void action153(Token token) throws SemanticError {
		// TODO
	}

	public void action154(Token token) throws SemanticError {
		// TODO
	}

	public void action155(Token token) throws SemanticError {
		// TODO
	}

	public void action156(Token token) throws SemanticError {
		// TODO
	}

	public void action157(Token token) throws SemanticError {
		// TODO
	}

	public void action158(Token token) throws SemanticError {
		// TODO
	}

	public void action159(Token token) throws SemanticError {
		// TODO
	}

	public void action160(Token token) throws SemanticError {
		// TODO
	}

	public void action161(Token token) throws SemanticError {
		// TODO
	}

	public void action162(Token token) throws SemanticError {
		// TODO
	}

	public void action163(Token token) throws SemanticError {
		// TODO
	}

	public void action164(Token token) throws SemanticError {
		// TODO
	}

	public void action165(Token token) throws SemanticError {
		// TODO
	}

	public void action166(Token token) throws SemanticError {
		// TODO
	}

	public void action167(Token token) throws SemanticError {
		// TODO
	}

	public void action168(Token token) throws SemanticError {
		// TODO
	}

	public void action169(Token token) throws SemanticError {
		// TODO
	}

	public void action170(Token token) throws SemanticError {
		// TODO
	}

	public void action171(Token token) throws SemanticError {
		Identificador id = this.contextoSemantico.peekId();
		if (!(id instanceof IdMetodo)) {
			throw new SemanticError("Id deveria ser um método", token.getPosition());
		} else if (id.getTipo() == Tipo.NULO) {
			throw new SemanticError("Esperava-se método com tipo", token.getPosition());
		} else {
			ContextoMetodo contextoMetodo = new ContextoMetodo();
			this.contextoSemantico.pushContextoMetodo(contextoMetodo);
			this.contextoSemantico.pushContextoEXPR(ContextoEXPR.PAR_ATUAL);
		}
	}

	public void action172(Token token) throws SemanticError {
		ContextoMetodo contextoMetodo = this.contextoSemantico.popContextoMetodo();
		IdMetodo id = (IdMetodo) this.contextoSemantico.peekId();

		if (id.getNumParametrosFormais() == contextoMetodo.getNumParametrosAtuais()) {
			this.contextoSemantico.setTipoVar(id.getTipo());
			this.geradorCodigoAcionado = true;
		} else {
			throw new SemanticError("Erro na quantidade de parâmetros", token.getPosition());
		}
	}

	public void action173(Token token) throws SemanticError {
		if (this.contextoSemantico.peekTipoExpr() != Tipo.INTEIRO) {
			throw new SemanticError("Índice deveria ser inteiro", token.getPosition());
		} else if (this.contextoSemantico.peekSubCategoriaVarIndexada() == SubCategoria.CADEIA) {
			this.contextoSemantico.setTipoVar(Tipo.CARACTER);
		} else {
			this.contextoSemantico.setTipoVar(this.contextoSemantico.peekId().getTipo());
		}
	}

	public void action174(Token token) throws SemanticError {
		Identificador id = this.contextoSemantico.peekId();
		if (id instanceof IdVariavel || id instanceof IdParametro) {
			IdComSubCategoria idSub = (IdComSubCategoria) id;
			if (idSub.getSubCategoria() == SubCategoria.VETOR) {
				throw new SemanticError("Vetor deve ser indexado", token.getPosition());
			} else {
				this.contextoSemantico.setTipoVar(id.getTipo());
			}
		} else if (id instanceof IdMetodo) {
			if (id.getTipo() == Tipo.NULO) {
				throw new SemanticError("Esperava-se método com tipo", token.getPosition());
			} else {
				IdMetodo idMetodo = (IdMetodo) id;
				if (idMetodo.getNumParametrosFormais() != 0) {
					throw new SemanticError("Erro na quantidade de parâmetros", token.getPosition());
				} else {
					this.contextoSemantico.setTipoVar(idMetodo.getTipo());
					this.geradorCodigoAcionado = true;
				}
			}
		} else if (id instanceof IdConstante) {
			this.contextoSemantico.setTipoVar(this.contextoSemantico.getTipoConst());
		} else {
			throw new SemanticError("Esperava-se id de categoria variável, método ou constante", token.getPosition());
		}
	}

	public void action175(Token token) throws SemanticError {
		Identificador id = this.tabelaDeSimbolos.getIdentificador(token.getLexeme());
		if (id == null) {
			throw new SemanticError("Id não declarado", token.getPosition());
		} else if (!(id instanceof IdConstante)) {
			throw new SemanticError("Id de constante esperado", token.getPosition());
		} else {
			IdConstante idConstante = (IdConstante) id;
			this.contextoSemantico.setTipoConst(id.getTipo());
			this.contextoSemantico.setValConst(idConstante.getValor());
		}
	}

	public void action176(Token token) throws SemanticError {
		this.contextoSemantico.setTipoConst(Tipo.INTEIRO);
		this.contextoSemantico.setValConst(token.getLexeme());
	}

	public void action177(Token token) throws SemanticError {
		this.contextoSemantico.setTipoConst(Tipo.REAL);
		this.contextoSemantico.setValConst(token.getLexeme());
	}

	public void action178(Token token) throws SemanticError {
		this.contextoSemantico.setTipoConst(Tipo.BOOLEANO);
		this.contextoSemantico.setValConst(token.getLexeme());
	}

	public void action179(Token token) throws SemanticError {
		this.contextoSemantico.setTipoConst(Tipo.BOOLEANO);
		this.contextoSemantico.setValConst(token.getLexeme());
	}

	public void action180(Token token) throws SemanticError {
		if (token.getLexeme().length() > 3) {
			this.contextoSemantico.setTipoConst(Tipo.CADEIA);
		} else {
			this.contextoSemantico.setTipoConst(Tipo.CARACTER);
		}
		this.contextoSemantico.setValConst(token.getLexeme());
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

	private boolean tipoValidoParaLeitura(IdComSubCategoria id) {
		return id.getSubCategoria().equals(SubCategoria.CADEIA)
				|| (id.getSubCategoria().equals(SubCategoria.PREDEFINIDO) && !id.getTipo().equals(Tipo.BOOLEANO));
	}

	private boolean tipoPreDefinido(Tipo tipo) {
		return tipo == Tipo.BOOLEANO || tipo == Tipo.CARACTER || tipo == Tipo.INTEIRO || tipo == Tipo.REAL;
	}

	private boolean tiposCompativeis(Tipo tipoLadoEsq, Tipo tipoExpr) {
		return (tipoLadoEsq == tipoExpr)
				|| (tipoLadoEsq == Tipo.REAL && tipoExpr == Tipo.INTEIRO)
				|| (tipoLadoEsq == Tipo.CADEIA && tipoExpr == Tipo.CARACTER);
	}

}
