package br.ufsc.ctc.ine.sin.ine5622.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class Semantico implements Constants {

    private static final String ACTION_METHOD_PREFIX = "action";

    private TabelaDeSimbolos tabelaDeSimbolos;

    private ContextoSemantico contextoSemantico;

    public Semantico() {
    	this.tabelaDeSimbolos = new TabelaDeSimbolos();
    	this.contextoSemantico = new ContextoSemantico();
    }

	public void executeAction(int action, Token token)	throws SemanticError
    {
		try {
			Method actionMethod = this.getClass().getMethod(ACTION_METHOD_PREFIX + action, Token.class);
			actionMethod.invoke(this, token);
		} catch (InvocationTargetException e) {
			Throwable t = e.getTargetException();
			t.printStackTrace();
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
		this.contextoSemantico.setContextoLID("decl");
		this.contextoSemantico.setPrimeiraPosicaoListaDeclaracao(this.tabelaDeSimbolos.getDeslocamento());
		this.contextoSemantico.inicializaListaDeclaracao();
	}

	public void action103(Token token) {
		this.contextoSemantico.setUltimaPosicaoListaDeclaracao(this.tabelaDeSimbolos.getDeslocamento());
	}

	public void action104(Token token) throws SemanticError {
		List<Identificador> listaDeclaracao = this.contextoSemantico.getListaDeclaracao();
		for (Identificador id : listaDeclaracao) {
			Identificador identificador;
			String categoriaAtual = this.contextoSemantico.getCategoriaAtual();
			String subCategoria = this.contextoSemantico.getSubCategoria();

			if (categoriaAtual.equals("variavel")) {
				identificador = new IdVariavel(id);
			} else {
				identificador = new IdConstante(id);
				identificador.setValor(this.contextoSemantico.getValConst());
			}

			identificador.setTipo(this.contextoSemantico.getTipoAtual());

			if (subCategoria.equals("vetor")) {
				identificador.setTamanho(this.getContextoSemantico().getValConst());
			} else {
				identificador.setTamanho(1);
			}

			this.tabelaDeSimbolos.incluirIdentificador(identificador);
		}
	}

	public void action105(Token token) throws SemanticError {
		this.contextoSemantico.setTipoAtual("inteiro");
	}

	public void action106(Token token) throws SemanticError {
		this.contextoSemantico.setTipoAtual("real");
	}

	public void action107(Token token) throws SemanticError {
		this.contextoSemantico.setTipoAtual("booleano");
	}

	public void action108(Token token) throws SemanticError {
		this.contextoSemantico.setTipoAtual("caracter");
	}

	public void action109(Token token) throws SemanticError {
		int valConst = this.getContextoSemantico().getValConst();
		if (this.contextoSemantico.getTipoConst().equals("inteiro")) {
			throw new SemanticError("Esperava-se uma constante inteira", token.getPosition());
		} else if (valConst > 256) {
			throw new SemanticError("Cadeia com tamanho maior que o permitido (" + valConst + " > 256)", token.getPosition());
		} else {
			this.contextoSemantico.setTipoAtual("cadeia");
		}
	}

	public void action110(Token token) throws SemanticError {
		if (this.contextoSemantico.getTipoAtual().equals("cadeia")) {
			throw new SemanticError("Vetor do tipo cadeia não é permitido", token.getPosition());
		} else {
			this.contextoSemantico.setSubcategoria("vetor");
		}
	}

	public void action111(Token token) throws SemanticError {
		if (this.contextoSemantico.getTipoConst().equals("inteiro")) {
			throw new SemanticError("A dimensão deve ser uma constante inteira", token.getPosition());
		} else {
			this.contextoSemantico.setNumElementos(this.contextoSemantico.getValConst());
		}
	}

	public void action112(Token token) throws SemanticError {
		if (this.contextoSemantico.getTipoAtual().equals("cadeia")) {
			this.contextoSemantico.setSubcategoria("cadeia");
		} else {
			this.contextoSemantico.setSubcategoria("pre-definido");
		}
	}

	public void action113(Token token) throws SemanticError {
		String contextoLID = this.contextoSemantico.getContextoLID();
		Identificador id = this.tabelaDeSimbolos.getIdentificador(token.getLexeme());
		if (contextoLID.equals("decl")) {
			if (id != null) {
				throw new SemanticError("Id já declarado", token.getPosition());
			} else {
				id = new Identificador(token.getLexeme());
				this.contextoSemantico.getListaDeclaracao().add(id);
				this.tabelaDeSimbolos.incluirIdentificador(id);
			}
		} else if (contextoLID.equals("par-formal")) {
			if (id != null) {
				throw new SemanticError("Id de parâmetro repetido", token.getPosition());
			} else {
				this.contextoSemantico.incrementaNumParametrosFormais();
				id = new IdParametro(token.getLexeme());
				this.contextoSemantico.getListaDeclaracao().add(id);
				this.tabelaDeSimbolos.incluirIdentificador(id);
			}
		} else if (contextoLID.equals("leitura")) {
//			TODO:
		}
	}

	public void action114(Token token) throws SemanticError {
		String subCategoria = this.contextoSemantico.getSubCategoria();
		if (subCategoria.equals("cadeia") || subCategoria.equals("vetor")) {
			throw new SemanticError("Apenas id do tipo pré-definido podem ser declarados como constante", token.getPosition());
		} else {
			this.contextoSemantico.setCategoriaAtual("constante");
		}
	}

	public void action115(Token token) throws SemanticError {
		if (!this.contextoSemantico.getTipoConst().equals(this.contextoSemantico.getTipoAtual())) {
			throw new SemanticError("Tipo da constante incorreto", token.getPosition());
		}
	}

	public void action116(Token token) throws SemanticError {
		this.contextoSemantico.setCategoriaAtual("variavel");
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
}
