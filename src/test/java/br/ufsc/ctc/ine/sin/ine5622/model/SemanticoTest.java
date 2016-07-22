package br.ufsc.ctc.ine.sin.ine5622.model;

import static org.junit.Assert.*;

import java.util.EmptyStackException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class SemanticoTest {

	private Semantico semantico;
	private TabelaDeSimbolos tabela;
	private ContextoSemantico contexto;

	@Before
	public void setUp() throws Exception {
		semantico = new Semantico();
		tabela = semantico.getTabelaDeSimbolos();
		contexto = semantico.getContextoSemantico();
	}

	@Test
	public void action101DeveReinicializarTabela() throws Exception {
		semantico.executeAction(101, token());
		assertNotEquals(tabela, semantico.getTabelaDeSimbolos());
	}

	@Test
	public void action101DeveInicializarNivelEDeslocamento() throws Exception {
		semantico.executeAction(101, token());
		tabela = semantico.getTabelaDeSimbolos();

		assertEquals(1, tabela.getDeslocamento());
		assertEquals(0, tabela.getNivelAtual());
	}

	@Test
	public void action101DeveInserirIdCategoriaPrograma() throws Exception {
		Token token = token();
		semantico.executeAction(101, token);
		tabela = semantico.getTabelaDeSimbolos();

		Identificador id = tabela.getIdentificador(token.getLexeme());

		assertNotNull(id);
		assertTrue(id instanceof IdPrograma);
	}

	@Test
	public void action101DeveIdCategoriaProgramaDeveTerNivelZero() throws Exception {
		Token token = token();
		semantico.executeAction(101, token);
		tabela = semantico.getTabelaDeSimbolos();

		Identificador id = tabela.getIdentificador(token.getLexeme());

		assertEquals(0, id.getNivel());
	}

	@Test
	public void action102DeveSetarContextoParaDecl() throws Exception {
		semantico.executeAction(102, token());
		assertEquals(ContextoLID.DECL, contexto.getContextoLID());
	}

	@Test
	public void action102DeveInicializarListaDeDeclaracao() throws Exception {
		semantico.executeAction(102, token());

		assertNotNull(contexto.getListaDeclaracao());
		assertEquals(0, contexto.getListaDeclaracao().size());
	}

	@Test
	public void action102DeveGuardarPosicaoDoPrimeiroIdDaLista() throws Exception {
		tabela.setDeslocamento(1);
		semantico.executeAction(102, token());

		assertEquals(tabela.getDeslocamento(), contexto.getPrimeiraPosicaoListaDeclaracao());
	}

	@Test
	public void action103DeveGuardarPosicaoDoUltimoIdDaLista() throws Exception {
		tabela.setDeslocamento(2);
		semantico.executeAction(103, token());

		assertEquals(tabela.getDeslocamento(), contexto.getUltimaPosicaoListaDeclaracao());
	}

	@Test
	public void action104DeveIncluirIdNaTabela() throws Exception {
		Token token = token();
		Identificador idAntes = declararIdentificador(Categoria.VARIAVEL, token);

		semantico.executeAction(104, token);

		Identificador idDepois = tabela.getIdentificador(idAntes.getNome());

		assertNotNull(idDepois);
		assertEquals(idAntes.getNome(), idDepois.getNome());
	}

	@Test
	public void action104DeveAlterarCategoriaDoIdParaVariavelQuandoForOCaso() throws Exception {
		Token token = token();
		Identificador idAntes = declararIdentificador(Categoria.VARIAVEL, token);

		semantico.executeAction(104, token());

		Identificador idDepois = tabela.getIdentificador(idAntes.getNome());

		assertTrue(idDepois instanceof IdVariavel);
	}

	@Test
	public void action104DeveAlterarCategoriaDoIdParaContanteQuandoForOCaso() throws Exception {
		Token token = token();
		Identificador idAntes = declararIdentificador(Categoria.CONSTANTE, token);

		semantico.executeAction(104, token());

		Identificador idDepois = tabela.getIdentificador(idAntes.getNome());

		assertNotNull(idDepois);
		assertTrue(idDepois instanceof IdConstante);
		assertEquals(idAntes.getNome(), idDepois.getNome());
	}

	@Test
	public void action104DeveAtribuirValorParaIdCategoriaConstante() throws Exception {
		Token token = token();
		Identificador idAntes = declararIdentificador(Categoria.CONSTANTE, token);

		semantico.executeAction(104, token());

		IdConstante idDepois = (IdConstante) tabela.getIdentificador(idAntes.getNome());

		assertEquals(contexto.getValConst(), idDepois.getValor());
	}

	@Test
	public void action104DeveAtribuirTipoCorretoParaIdDeclarados() throws Exception {
		Token token = token();
		Identificador idAntes = declararIdentificador(Categoria.VARIAVEL, token);
		Tipo tipoAtual = Tipo.INTEIRO;
		contexto.setTipoAtual(tipoAtual);

		semantico.executeAction(104, token());

		Identificador id = tabela.getIdentificador(idAntes.getNome());

		assertEquals(tipoAtual, id.getTipo());
	}

	@Test
	public void action104DeveIncrementarUmaUnidadeDeDeslocamentoQuandoNaoForVetor() throws Exception {
		Token token = token();
		Identificador idAntes = declararIdentificador(Categoria.VARIAVEL, token);
		SubCategoria subCategoria = SubCategoria.PREDEFINIDO;
		Tipo tipoAtual = Tipo.INTEIRO;
		contexto.setTipoAtual(tipoAtual);
		contexto.setSubCategoria(subCategoria);

		semantico.executeAction(104, token());

		Identificador id = tabela.getIdentificador(idAntes.getNome());

		assertEquals(1, tabela.getDeslocamento());
		assertEquals(1, id.getTamanho());
	}

	@Test
	public void action104DeveIncrementarDeslocamentoDeAcordoQuandoForVetor() throws Exception {
		Token token = token();
		Identificador idAntes = declararIdentificador(Categoria.VARIAVEL, token);
		SubCategoria subCategoria = SubCategoria.VETOR;
		Tipo tipoAtual = Tipo.INTEIRO;
		contexto.setTipoAtual(tipoAtual);
		contexto.setSubCategoria(subCategoria);
		contexto.setValConst("5");

		semantico.executeAction(104, token());

		Identificador id = tabela.getIdentificador(idAntes.getNome());
		int tamanhoVetor = Integer.parseInt(contexto.getValConst());

		assertEquals(tamanhoVetor, tabela.getDeslocamento());
		assertEquals(tamanhoVetor, id.getTamanho());
	}

	@Test
	public void action105DeveAlterarTipoAtualParaInteiro() throws Exception {
		semantico.executeAction(105, token());
		assertEquals(Tipo.INTEIRO, contexto.getTipoAtual());
	}

	@Test
	public void action106DeveAlterarTipoAtualParaReal() throws Exception {
		semantico.executeAction(106, token());
		assertEquals(Tipo.REAL, contexto.getTipoAtual());
	}

	@Test
	public void action107DeveAlterarTipoAtualParaBooleano() throws Exception {
		semantico.executeAction(107, token());
		assertEquals(Tipo.BOOLEANO, contexto.getTipoAtual());
	}

	@Test
	public void action108DeveAlterarTipoAtualParaCaracter() throws Exception {
		semantico.executeAction(108, token());
		assertEquals(Tipo.CARACTER, contexto.getTipoAtual());
	}

	@Test(expected=SemanticError.class)
	public void action109DeveLancarExcecaoCasoTipoConstNaoSejaInteiro() throws Exception {
		contexto.setTipoConst(Tipo.BOOLEANO);
		semantico.executeAction(109, token());
	}

	@Test(expected=SemanticError.class)
	public void action109DeveLancarExcecaoCasoTipoConstSejaInteiroEValConstMaiorQue256() throws Exception {
		contexto.setTipoConst(Tipo.INTEIRO);
		contexto.setValConst("257");
		semantico.executeAction(109, token());
	}

	public void action109DeveAlterarTipoAtualParaCadeiaCasoTipoConstSejaInteiroEValConstMenorOuIgual256() throws Exception {
		contexto.setTipoConst(Tipo.INTEIRO);
		contexto.setValConst("256");

		semantico.executeAction(109, token());

		assertEquals(Tipo.CADEIA, contexto.getTipoAtual());
	}

	@Test(expected=SemanticError.class)
	public void action110DeveLancarExcecaoCasoTipoAtualSejaCadeia() throws Exception {
		contexto.setTipoAtual(Tipo.CADEIA);
		semantico.executeAction(110, token());
	}

	@Test
	public void action110DeveAtribuirSubCategoriaComoVetorCasoTipoAtualSejaDiferenteDeCadeia() throws Exception {
		contexto.setTipoAtual(Tipo.INTEIRO);

		semantico.executeAction(110, token());

		assertEquals(SubCategoria.VETOR, contexto.getSubCategoria());
	}

	@Test(expected=SemanticError.class)
	public void action111DeveLancarExcecaoCasoTipoConstNaoSejaInteiro() throws Exception {
		contexto.setTipoConst(Tipo.CADEIA);
		semantico.executeAction(111, token());
	}

	@Test
	public void action111AlterarNumElementosParaValConst() throws Exception {
		contexto.setTipoConst(Tipo.INTEIRO);
		String dimensao = "14";
		contexto.setValConst(dimensao);

		semantico.executeAction(111, token());

		assertEquals(Integer.parseInt(dimensao), contexto.getNumElementos());
	}

	@Test
	public void action112DeveAlterarSubCategoriaParaCadeiaCasoTipoAtualSejaCadeia() throws Exception {
		contexto.setTipoAtual(Tipo.CADEIA);
		semantico.executeAction(112, token());

		assertEquals(SubCategoria.CADEIA, contexto.getSubCategoria());
	}

	@Test
	public void action112DeveAlterarSubCategoriaParaPreDefinidoCasoTipoAtualNaoSejaCadeia() throws Exception {
		contexto.setTipoAtual(Tipo.INTEIRO);
		semantico.executeAction(112, token());

		assertEquals(SubCategoria.PREDEFINIDO, contexto.getSubCategoria());
	}

	@Test(expected=SemanticError.class)
	public void action113DeveLancarExcecaoCasoContextoLIDSejaDeclEIdJaDeclaradoNoNivelAtual() throws Exception {
		contexto.setContextoLID(ContextoLID.DECL);
		Token token = token();
		Identificador idExistente = new Identificador(token.getLexeme());
		tabela.incluirIdentificador(idExistente);

		semantico.executeAction(113, token);
	}


	@Test
	public void action113DeveInserirIdNaTabelaCasoContextoLIDSejaDeclEIdNaoDeclaradoNoNivelAtual() throws Exception {
		contexto.setContextoLID(ContextoLID.DECL);
		Token token = token();
		contexto.inicializaListaDeclaracao();

		semantico.executeAction(113, token);

		assertNotNull(tabela.getIdentificador(token.getLexeme()));
	}

	@Test
	public void action113DeveInserirIdNaListaDeDeclaracaoCasoContextoLIDSejaDeclEIdNaoDeclaradoNoNivelAtual() throws Exception {
		contexto.setContextoLID(ContextoLID.DECL);
		Token token = token();
		contexto.inicializaListaDeclaracao();

		semantico.executeAction(113, token);

		assertEquals(1, contexto.getListaDeclaracao().size());
	}

	@Test(expected=SemanticError.class)
	public void action113DeveLancarExcecaoCasoContextoLIDSejaParFormalEIdJaDeclaradoNoNivelAtual() throws Exception {
		contexto.setContextoLID(ContextoLID.PAR_FORMAL);
		Token token = token();
		Identificador idExistente = new Identificador(token.getLexeme());
		contexto.inicializaListaDeclaracao();
		tabela.incluirIdentificador(idExistente);

		semantico.executeAction(113, token);
	}

	@Test
	public void action113DeveInserirIdNaTabelaCasoContextoLIDSejaParFormalEIdNaoDeclaradoNoNivelAtual() throws Exception {
		contexto.setContextoLID(ContextoLID.PAR_FORMAL);
		contexto.inicializaListaDeclaracao();
		Token token = token();

		semantico.executeAction(113, token);

		assertNotNull(tabela.getIdentificador(token.getLexeme()));
	}

	@Test
	public void action113DeveInserirIdNaListaDeDeclaracaoContextoLIDSejaParFormalEIdNaoDeclaradoNoNivelAtual() throws Exception {
		contexto.setContextoLID(ContextoLID.PAR_FORMAL);
		contexto.inicializaListaDeclaracao();
		Token token = token();

		semantico.executeAction(113, token);

		assertEquals(1, contexto.getListaDeclaracao().size());
	}

	@Test
	public void action113DeveIncrementarNumParametrosFormaisCasoContextoLIDSejaParFormalEIdNaoDeclaradoNoNivelAtual() throws Exception {
		contexto.setContextoLID(ContextoLID.PAR_FORMAL);
		contexto.inicializaListaDeclaracao();
		Token token = token();

		semantico.executeAction(113, token);

		assertEquals(1, contexto.getNumParametrosFormais());
	}

	@Test(expected=SemanticError.class)
	public void action113DeveLancarExcecaoCasoContextoLIDSejaLeituraEIdNaoDeclarado() throws Exception {
		contexto.setContextoLID(ContextoLID.LEITURA);
		contexto.inicializaListaDeclaracao();
		semantico.executeAction(113, token());
	}

	@Test(expected=SemanticError.class)
	public void action113DeveLancarExcecaoCasoContextoLIDSejaLeituraComIdJaDeclaradoECategoriaConstanteInvalidaParaLeitura() throws Exception {
		contexto.setContextoLID(ContextoLID.LEITURA);
		Token token = token();
		IdConstante id = new IdConstante(token.getLexeme());
		contexto.inicializaListaDeclaracao();
		tabela.incluirIdentificador(id);

		semantico.executeAction(113, token);
	}

	@Test(expected=SemanticError.class)
	public void action113DeveLancarExcecaoCasoContextoLIDSejaLeituraComIdJaDeclaradoECategoriaProgramaInvalidaParaLeitura() throws Exception {
		contexto.setContextoLID(ContextoLID.LEITURA);
		Token token = token();
		IdPrograma id = new IdPrograma(token.getLexeme());
		contexto.inicializaListaDeclaracao();
		tabela.incluirIdentificador(id);

		semantico.executeAction(113, token);
	}

	@Test(expected=SemanticError.class)
	public void action113DeveLancarExcecaoCasoContextoLIDSejaLeituraComIdJaDeclaradoECategoriaMetodoInvalidaParaLeitura() throws Exception {
		contexto.setContextoLID(ContextoLID.LEITURA);
		Token token = token();
		IdMetodo id = new IdMetodo(token.getLexeme());
		contexto.inicializaListaDeclaracao();
		tabela.incluirIdentificador(id);

		semantico.executeAction(113, token);
	}

	@Test(expected=SemanticError.class)
	public void action113DeveLancarExcecaoCasoContextoLIDSejaLeituraComIdJaDeclaradoETipoBooleanoInvalidoParaLeitura() throws Exception {
		contexto.setContextoLID(ContextoLID.LEITURA);
		Token token = token();
		IdVariavel id = new IdVariavel(token.getLexeme());
		id.setSubCategoria(SubCategoria.PREDEFINIDO);
		id.setTipo(Tipo.BOOLEANO);
		contexto.inicializaListaDeclaracao();
		tabela.incluirIdentificador(id);

		semantico.executeAction(113, token);
	}

	@Test(expected=SemanticError.class)
	public void action113DeveLancarExcecaoCasoContextoLIDSejaLeituraComIdJaDeclaradoESubcategoriaVetorInvalidaParaLeitura() throws Exception {
		contexto.setContextoLID(ContextoLID.LEITURA);
		Token token = token();
		IdVariavel id = new IdVariavel(token.getLexeme());
		id.setSubCategoria(SubCategoria.VETOR);
		contexto.inicializaListaDeclaracao();
		tabela.incluirIdentificador(id);

		semantico.executeAction(113, token);
	}

	@Test
	public void action113DeveGerarCodigoCasoContextoLIDSejaLeituraIdJaDeclaradoECategoriaETipoValidosParaLeitura() throws Exception {
		contexto.setContextoLID(ContextoLID.LEITURA);
		Token token = token();
		IdVariavel id = new IdVariavel(token.getLexeme());
		id.setSubCategoria(SubCategoria.PREDEFINIDO);
		id.setTipo(Tipo.INTEIRO);
		contexto.inicializaListaDeclaracao();
		tabela.incluirIdentificador(id);

		semantico.executeAction(113, token);

		assertTrue(semantico.isGeradorCodigoAcionado());
	}

	@Test
	public void action113DeveGerarCodigoCasoContextoLIDSejaLeituraIdJaDeclaradoESubCategoriaValidaParaLeitura() throws Exception {
		contexto.setContextoLID(ContextoLID.LEITURA);
		Token token = token();
		IdVariavel id = new IdVariavel(token.getLexeme());
		id.setSubCategoria(SubCategoria.CADEIA);
		contexto.inicializaListaDeclaracao();
		tabela.incluirIdentificador(id);

		semantico.executeAction(113, token);

		assertTrue(semantico.isGeradorCodigoAcionado());
	}

	@Test(expected=SemanticError.class)
	public void action114DeveLancarExcecaoCasoSubCategoriaSejaCadeia() throws Exception {
		contexto.setSubCategoria(SubCategoria.CADEIA);
		semantico.executeAction(114, token());
	}

	@Test(expected=SemanticError.class)
	public void action114DeveLancarExcecaoCasoSubCategoriaSejaVetor() throws Exception {
		contexto.setSubCategoria(SubCategoria.VETOR);
		semantico.executeAction(114, token());
	}

	@Test
	public void action114DeveAlterarCategoriaAtualParaConstanteCasoSubCategoriaSejaPreDefinido() throws Exception {
		contexto.setSubCategoria(SubCategoria.PREDEFINIDO);

		semantico.executeAction(114, token());

		assertEquals(Categoria.CONSTANTE, contexto.getCategoriaAtual());
	}

	@Test(expected=SemanticError.class)
	public void action115DeveLancarExcecaoCasoTipoConstSejaDiferenteDeTipoAtual() throws Exception {
		contexto.setTipoConst(Tipo.BOOLEANO);
		contexto.setTipoAtual(Tipo.REAL);
		semantico.executeAction(115, token());
	}

	@Test
	public void action115NaoDeveLancarExcecaoCasoTipoConstSejaIgualTipoAtual() throws Exception {
		contexto.setTipoConst(Tipo.INTEIRO);
		contexto.setTipoAtual(Tipo.INTEIRO);
		semantico.executeAction(115, token());
	}

	@Test
	public void action116DeveAtribuirVariavelParaCategoriaAtual() throws Exception {
		semantico.executeAction(116, token());
		assertEquals(Categoria.VARIAVEL, contexto.getCategoriaAtual());
	}

	@Test(expected=SemanticError.class)
	public void action117DeveLancarExcecaoCasoIdJaDeclarado() throws Exception {
		Token token = token();
		IdVariavel id = new IdVariavel(token.getLexeme());
		tabela.incluirIdentificador(id);

		semantico.executeAction(117, token);
	}

	@Test
	public void action117DeveInserirIdNaTabelaCasoIdNaoDeclarado() throws Exception {
		semantico.executeAction(117, token());
		assertNotNull(tabela.getIdentificador(token().getLexeme(), 0));
	}

	@Test
	public void action117DeveZerarNumParametrosFormaisCasoIdNaoDeclarado() throws Exception {
		contexto.setNumParametrosFormais(3);
		semantico.executeAction(117, token());
		assertEquals(0,  contexto.getNumParametrosFormais());
	}

	@Test
	public void action117DeveIncrementarNivelAtualCasoIdNaoDeclarado() throws Exception {
		semantico.executeAction(117, token());
		assertEquals(1, tabela.getNivelAtual());
	}

	@Test
	public void action117DeveEmpilharRetorneCasoIdNaoDeclarado() throws Exception {
		semantico.executeAction(117, token());
		assertFalse(contexto.popRetorne());
	}

	@Test
	public void action118DeveAtualizarNumParametrosFormais() throws Exception {
		semantico.executeAction(118, token());
		assertEquals(1, contexto.getNumParametrosFormais());
	}

	@Test
	public void action119DeveAtualizarTipoDoMetodoAtual() throws Exception {
		Token token = token();
		IdMetodo idMetodo = new IdMetodo(token.getLexeme());
		contexto.pushIdMetodo(idMetodo);
		contexto.setTipoMetodo(Tipo.INTEIRO);

		semantico.executeAction(119, token);

		assertEquals(Tipo.INTEIRO, idMetodo.getTipo());
	}

	@Test
	public void action120DeveRetirarVariaveisDeclaradasLocalmente() throws Exception {
		tabela.setNivelAtual(1);
		Token token = token();
		Identificador id = new Identificador(token.getLexeme());
		tabela.incluirIdentificador(id);
		contexto.pushIdMetodo(new IdMetodo(""));
		Map<String, Identificador> ids = tabela.getIdentificadoresPorNivel().get(1);
		assertNotNull(ids.get(token.getLexeme()));
		contexto.pushRetorne(true);

		semantico.executeAction(120, token);

		assertNull(tabela.getIdentificadoresPorNivel().get(1));
	}

	@Test
	public void action120DeveDecrementarNivelAtual() throws Exception {
		tabela.setNivelAtual(1);
		contexto.pushIdMetodo(new IdMetodo(""));
		contexto.pushRetorne(true);

		semantico.executeAction(120, token());

		assertEquals(0, tabela.getNivelAtual());
	}

	@Test(expected=SemanticError.class)
	public void action120DeveLancarExcecaoCasoRetorneEmpilhadoSejaFalsoETipoDoMetodoSejaDiferenteDeNulo() throws Exception {
		tabela.setNivelAtual(1);
		IdMetodo idMetodo = new IdMetodo("");
		idMetodo.setTipo(Tipo.INTEIRO);
		contexto.pushIdMetodo(idMetodo);
		contexto.pushRetorne(false);

		semantico.executeAction(120, token());
	}

	@Test
	public void action120DeveLancarExcecaoCasoRetorneEmpilhadoSejaFalsoETipoDoMetodoNulo() throws Exception {
		tabela.setNivelAtual(1);
		IdMetodo idMetodo = new IdMetodo("");
		idMetodo.setTipo(Tipo.NULO);
		contexto.pushIdMetodo(idMetodo);
		contexto.pushRetorne(false);

		semantico.executeAction(120, token());
	}

	@Test
	public void action121DeveAlterarContextLIDParaParFormal() throws Exception {
		semantico.executeAction(121, token());
		assertEquals(ContextoLID.PAR_FORMAL, contexto.getContextoLID());
	}

	@Test
	public void action121DeveGuardarPosicaoDoPrimeiroIdDaLista() throws Exception {
		tabela.setDeslocamento(1);
		semantico.executeAction(121, token());

		assertEquals(tabela.getDeslocamento(), contexto.getPrimeiraPosicaoListaDeclaracao());
	}

	@Test
	public void action122DeveGuardarPosicaoDoUltimoIdDaLista() throws Exception {
		tabela.setDeslocamento(1);
		semantico.executeAction(122, token());

		assertEquals(tabela.getDeslocamento(), contexto.getUltimaPosicaoListaDeclaracao());
	}

	@Test(expected=SemanticError.class)
	public void action123DeveLancarExcecaoCasoTipoAtualSejaDiferenteDePreDefinido() throws Exception {
		contexto.setTipoAtual(Tipo.CADEIA);
		semantico.executeAction(123, token());
	}

	@Test
	public void action123DeveAtualizarIdParaIdParametroCasoTipoAtualSejaPreDefinido() throws Exception {
		contexto.setTipoAtual(Tipo.INTEIRO);
		contexto.inicializaListaDeclaracao();
		Token token = token();
		Identificador idAntes = new Identificador(token.getLexeme());
		contexto.getListaDeclaracao().add(idAntes);
		tabela.incluirIdentificador(idAntes);
		IdMetodo idMetodo = new IdMetodo("metodo");
		contexto.pushIdMetodo(idMetodo);

		semantico.executeAction(123, token);

		Identificador idDepois = tabela.getIdentificador(token.getLexeme());
		assertTrue(idDepois instanceof IdParametro);
	}

	@Test
	public void action123DeveAtualizarMppDoIdParametroCasoTipoAtualSejaPreDefinido() throws Exception {
		contexto.setTipoAtual(Tipo.INTEIRO);
		contexto.inicializaListaDeclaracao();
		Token token = token();
		Identificador idAntes = new Identificador(token.getLexeme());
		contexto.getListaDeclaracao().add(idAntes);
		tabela.incluirIdentificador(idAntes);
		IdMetodo idMetodo = new IdMetodo("metodo");
		contexto.pushIdMetodo(idMetodo);
		contexto.setMetodoPassagem(MetodoPassagem.VALOR);

		semantico.executeAction(123, token);

		IdParametro idDepois = (IdParametro)tabela.getIdentificador(token.getLexeme());
		assertEquals(MetodoPassagem.VALOR, idDepois.getMpp());
	}

	@Test
	public void action123DeveAtualizarTipoAtualDoIdParametroCasoTipoAtualSejaPreDefinido() throws Exception {
		contexto.setTipoAtual(Tipo.INTEIRO);
		contexto.inicializaListaDeclaracao();
		Token token = token();
		Identificador idAntes = new Identificador(token.getLexeme());
		contexto.getListaDeclaracao().add(idAntes);
		tabela.incluirIdentificador(idAntes);
		IdMetodo idMetodo = new IdMetodo("metodo");
		contexto.pushIdMetodo(idMetodo);
		contexto.setTipoAtual(Tipo.INTEIRO);

		semantico.executeAction(123, token);

		Identificador idDepois = tabela.getIdentificador(token.getLexeme());
		assertEquals(Tipo.INTEIRO, idDepois.getTipo());
	}

	@Test
	public void action123DeveIncluirIdParametroNaListaDeParametrosDoIdMetodoAtualCasoTipoAtualSejaPreDefinido() throws Exception {
		contexto.setTipoAtual(Tipo.INTEIRO);
		contexto.inicializaListaDeclaracao();
		Token token = token();
		Identificador idAntes = new Identificador(token.getLexeme());
		contexto.getListaDeclaracao().add(idAntes);
		tabela.incluirIdentificador(idAntes);
		IdMetodo idMetodo = new IdMetodo("metodo");
		contexto.pushIdMetodo(idMetodo);

		semantico.executeAction(123, token);

		assertEquals(1, idMetodo.getParametros().size());
		assertEquals(idAntes.getNome(), idMetodo.getParametros().get(0).getNome());
	}

	@Test(expected=SemanticError.class)
	public void action124DeveLancarExcecaoCasoTipoAtualSejaCadeia() throws Exception {
		contexto.setTipoAtual(Tipo.CADEIA);
		semantico.executeAction(124, token());
	}

	@Test
	public void action124DeveAlterarTipoDoMetodoParaTipoAtualCasoTipoAtualSejaDiferenteCadeia() throws Exception {
		contexto.setTipoAtual(Tipo.INTEIRO);

		semantico.executeAction(124, token());

		assertEquals(Tipo.INTEIRO, contexto.getTipoMetodo());
	}

	@Test
	public void action125DeveAlterarTipoDoMetodoParaNulo() throws Exception {
		semantico.executeAction(125, token());
		assertEquals(Tipo.NULO, contexto.getTipoMetodo());
	}

	@Test
	public void action126DeveAlterarMppParaReferencia() throws Exception {
		semantico.executeAction(126, token());
		assertEquals(MetodoPassagem.REFERENCIA, contexto.getMetodoPassagem());
	}

	@Test
	public void action127DeveAlterarMppParaValor() throws Exception {
		semantico.executeAction(127, token());
		assertEquals(MetodoPassagem.VALOR, contexto.getMetodoPassagem());
	}

	@Test(expected=SemanticError.class)
	public void action128DeveLancarExcecaoCasoIdNaoDeclarado() throws Exception {
		semantico.executeAction(128, token());
	}

	@Test
	public void action128DeveArmazenarIdAtual() throws Exception {
		tabela.incluirIdentificador(new Identificador(token().getLexeme()));
		semantico.executeAction(128, token());

		Identificador id = tabela.getIdentificador(token().getLexeme());
		assertNotNull(id);
	}

	@Test
	public void action128DeveArmazenarIdAtualMesmoQuandoIdEstiverEmNivelAnterior() throws Exception {
		tabela.incluirIdentificador(new Identificador(token().getLexeme()));
		tabela.setNivelAtual(1);
		semantico.executeAction(128, token());

		Identificador id = tabela.getIdentificador(token().getLexeme());
		assertNotNull(id);
	}

	@Test(expected=SemanticError.class)
	public void action129DeveLancarExcecaoCasoTipoExprSejaDiferenteDeBooleanoOuInteiro() throws Exception {
		contexto.pushTipoExpr(Tipo.REAL);
		semantico.executeAction(129, token());
	}

	@Test
	public void action129DeveGerarCodigoCasoTipoExprSejaBooleano() throws Exception {
		contexto.pushTipoExpr(Tipo.BOOLEANO);

		semantico.executeAction(129, token());

		assertTrue(semantico.isGeradorCodigoAcionado());
	}

	@Test
	public void action129DeveGerarCodigoCasoTipoExprSejaInteiro() throws Exception {
		contexto.pushTipoExpr(Tipo.INTEIRO);

		semantico.executeAction(129, token());

		assertTrue(semantico.isGeradorCodigoAcionado());
	}

	@Test
	public void action130DeveAlterarContextoLIDParaLeitura() throws Exception {
		semantico.executeAction(130, token());
		assertEquals(ContextoLID.LEITURA, contexto.getContextoLID());
	}

	@Test
	public void action131DeveAlterarContextoEXPRParaImpressao() throws Exception {
		semantico.executeAction(131, token());
		assertEquals(ContextoEXPR.IMPRESSAO, contexto.getContextoEXPR());
	}

	@Test(expected=SemanticError.class)
	public void action132DeveLancarExcecaoCasoMetodoAtualNaoTenhaTipo() throws Exception {
		IdMetodo idMetodo = new IdMetodo(token().getLexeme());
		idMetodo.setTipo(Tipo.NULO);
		contexto.pushIdMetodo(idMetodo);
		contexto.pushTipoExpr(Tipo.BOOLEANO);

		semantico.executeAction(132, token());
	}

	@Test(expected=SemanticError.class)
	public void action132DeveLancarExcecaoCasoTipoExprSejaDiferenteDoTipoDoMetodoAtual() throws Exception {
		IdMetodo idMetodo = new IdMetodo(token().getLexeme());
		idMetodo.setTipo(Tipo.INTEIRO);
		contexto.pushIdMetodo(idMetodo);
		contexto.pushTipoExpr(Tipo.BOOLEANO);

		semantico.executeAction(132, token());
	}

	@Test
	public void action132DeveGerarCodigoCasoTipoExprSejaIgualAoTipoDoMetodoAtual() throws Exception {
		IdMetodo idMetodo = new IdMetodo(token().getLexeme());
		idMetodo.setTipo(Tipo.INTEIRO);
		contexto.pushIdMetodo(idMetodo);
		contexto.pushTipoExpr(Tipo.INTEIRO);
		contexto.pushRetorne(true);

		semantico.executeAction(132, token());

		assertTrue(semantico.isGeradorCodigoAcionado());
	}

	@Test
	public void action132DeveGerarCodigoCasoTipoExprSejaCompativelComTipoRealDoMetodoAtual() throws Exception {
		IdMetodo idMetodo = new IdMetodo(token().getLexeme());
		idMetodo.setTipo(Tipo.REAL);
		contexto.pushIdMetodo(idMetodo);
		contexto.pushTipoExpr(Tipo.INTEIRO);
		contexto.pushRetorne(false);

		semantico.executeAction(132, token());

		assertTrue(semantico.isGeradorCodigoAcionado());
	}

	@Test
	public void action132DeveGerarCodigoCasoTipoExprSejaCompativelComTipoCadeiaDoMetodoAtual() throws Exception {
		IdMetodo idMetodo = new IdMetodo(token().getLexeme());
		idMetodo.setTipo(Tipo.CADEIA);
		contexto.pushIdMetodo(idMetodo);
		contexto.pushTipoExpr(Tipo.CARACTER);
		contexto.pushRetorne(false);

		semantico.executeAction(132, token());

		assertTrue(semantico.isGeradorCodigoAcionado());
	}

	@Test
	public void action132DesempilharValorAtualDeRetorneEEmpilharValorVerdadeiroCasoTipoExprSejaCompativelComTipoCadeiaDoMetodoAtual() throws Exception {
		IdMetodo idMetodo = new IdMetodo(token().getLexeme());
		idMetodo.setTipo(Tipo.CADEIA);
		contexto.pushIdMetodo(idMetodo);
		contexto.pushTipoExpr(Tipo.CARACTER);
		contexto.pushRetorne(false);

		semantico.executeAction(132, token());

		assertTrue(contexto.popRetorne());
	}

	@Test(expected=SemanticError.class)
	public void action133DeveLancarExcecaoCasoIdNaoSejaVariavelNemParametro() throws Exception {
		IdConstante id = new IdConstante(token().getLexeme());
		contexto.pushId(id);

		semantico.executeAction(133, token());
	}

	@Test(expected=SemanticError.class)
	public void action133DeveLancarExcecaoCasoIdSejaVariavelESubCategoriaSejaVetor() throws Exception {
		IdVariavel id = new IdVariavel(token().getLexeme());
		id.setSubCategoria(SubCategoria.VETOR);
		contexto.pushId(id);

		semantico.executeAction(133, token());
	}

	@Test(expected=SemanticError.class)
	public void action133DeveLancarExcecaoCasoIdSejaParametroESubCategoriaSejaVetor() throws Exception {
		IdParametro id = new IdParametro(token().getLexeme());
		id.setSubCategoria(SubCategoria.VETOR);
		contexto.pushId(id);

		semantico.executeAction(133, token());
	}

	@Test
	public void action133DeveAlterarTipoLadEsqParaTipoDoIdCasoIdSejaVariavelESubCategoriaNaoSejaVetor() throws Exception {
		IdVariavel id = new IdVariavel(token().getLexeme());
		id.setTipo(Tipo.INTEIRO);
		contexto.pushId(id);

		semantico.executeAction(133, token());

		assertEquals(Tipo.INTEIRO, contexto.getTipoLadoEsq());
	}

	@Test(expected=SemanticError.class)
	public void action134DeveLancarExcecaoCasoTipoExprSejaIncompativelComTipoLadoEsq() throws Exception {
		contexto.pushTipoExpr(Tipo.BOOLEANO);
		contexto.setTipoLadoEsq(Tipo.INTEIRO);

		semantico.executeAction(134, token());
	}

	@Test
	public void action134DeveGerarCodigoCasoTipoExprSejaCompativelComTipoLadoEsq() throws Exception {
		contexto.pushTipoExpr(Tipo.INTEIRO);
		contexto.setTipoLadoEsq(Tipo.REAL);
		contexto.pushId(new Identificador(token().getLexeme()));

		semantico.executeAction(134, token());
	}

	@Test(expected=EmptyStackException.class)
	public void action134DeveDesempilharIdCasoTipoExprSejaCompativelComTipoLadoEsq() throws Exception {
		contexto.pushTipoExpr(Tipo.INTEIRO);
		contexto.setTipoLadoEsq(Tipo.INTEIRO);
		contexto.pushId(new Identificador(token().getLexeme()));
		assertNotNull(contexto.peekId());

		semantico.executeAction(134, token());

		contexto.peekId();
	}

	@Test(expected=EmptyStackException.class)
	public void action134DeveDesempilharTipoExprCasoTipoExprSejaCompativelComTipoLadoEsq() throws Exception {
		contexto.pushTipoExpr(Tipo.INTEIRO);
		contexto.setTipoLadoEsq(Tipo.INTEIRO);
		contexto.pushId(new Identificador(token().getLexeme()));
		assertNotNull(contexto.peekTipoExpr());

		semantico.executeAction(134, token());

		contexto.peekTipoExpr();
	}

	@Test(expected=SemanticError.class)
	public void action135DeveLancarExcecaoCasoIdAtualNaoSejaVariavel() throws Exception {
		IdConstante id = new IdConstante(token().getLexeme());
		contexto.pushId(id);

		semantico.executeAction(135, token());
	}

	@Test(expected=SemanticError.class)
	public void action135DeveLancarExcecaoCasoIdAtualSejaVariavelESubCategoriaNaoSejaCadeiaNemVetor() throws Exception {
		IdVariavel id = new IdVariavel(token().getLexeme());
		id.setSubCategoria(SubCategoria.PREDEFINIDO);
		contexto.pushId(id);

		semantico.executeAction(135, token());
	}

	@Test
	public void action135DeveAlterarSubCategoriaVarIndexadaParaSubCategoriaDoIdCasoIdAtualSejaVariavelESubCategoriaSejaCadeia() throws Exception {
		IdVariavel id = new IdVariavel(token().getLexeme());
		id.setSubCategoria(SubCategoria.CADEIA);
		contexto.pushId(id);

		semantico.executeAction(135, token());

		assertEquals(SubCategoria.CADEIA, contexto.peekSubCategoriaVarIndexada());
	}

	@Test
	public void action135DeveAlterarSubCategoriaVarIndexadaParaSubCategoriaDoIdCasoIdAtualSejaVariavelESubCategoriaSejaVetor() throws Exception {
		IdVariavel id = new IdVariavel(token().getLexeme());
		id.setSubCategoria(SubCategoria.VETOR);
		contexto.pushId(id);

		semantico.executeAction(135, token());

		assertEquals(SubCategoria.VETOR, contexto.peekSubCategoriaVarIndexada());
	}

	@Test(expected=SemanticError.class)
	public void action136DeveLancarExcecaoCasoTipoExprSejaDiferenteDeInteiro() throws Exception {
		contexto.pushTipoExpr(Tipo.BOOLEANO);
		semantico.executeAction(136, token());
	}

	@Test
	public void action136DeveAlterarTipoLadoEsqParaCaracterCasoTipoExprSejaInteiroESubCategoriaVarIndexadaSejaCadeia() throws Exception {
		contexto.pushTipoExpr(Tipo.INTEIRO);
		contexto.pushSubCategoriaVarIndexada(SubCategoria.CADEIA);

		semantico.executeAction(136, token());

		assertEquals(Tipo.CARACTER, contexto.getTipoLadoEsq());
	}

	@Test(expected=EmptyStackException.class)
	public void action136DeveDesempilharTipoExprCasoTipoExprSejaInteiroESubCategoriaVarIndexadaSejaCadeia() throws Exception {
		contexto.pushTipoExpr(Tipo.INTEIRO);
		contexto.pushSubCategoriaVarIndexada(SubCategoria.CADEIA);
		assertNotNull(contexto.peekTipoExpr());

		semantico.executeAction(136, token());

		contexto.peekTipoExpr();
	}

	@Test(expected=EmptyStackException.class)
	public void action136DeveDesempilharSubCategoriaVarIndexadaCasoTipoExprSejaInteiroESubCategoriaVarIndexadaSejaCadeia() throws Exception {
		contexto.pushTipoExpr(Tipo.INTEIRO);
		contexto.pushSubCategoriaVarIndexada(SubCategoria.CADEIA);
		assertNotNull(contexto.peekSubCategoriaVarIndexada());

		semantico.executeAction(136, token());

		contexto.peekSubCategoriaVarIndexada();
	}

	@Test
	public void action136DeveAlterarTipoLadoEsqParaTipoDoVetorCasoTipoExprSejaInteiroESubCategoriaVarIndexadaSejaVetor() throws Exception {
		Identificador id = new Identificador(token().getLexeme());
		id.setTipo(Tipo.BOOLEANO);
		contexto.pushId(id);
		contexto.pushTipoExpr(Tipo.INTEIRO);
		contexto.pushSubCategoriaVarIndexada(SubCategoria.VETOR);

		semantico.executeAction(136, token());

		assertEquals(Tipo.BOOLEANO, contexto.getTipoLadoEsq());
	}

	@Test(expected=SemanticError.class)
	public void action137DeveLancarExcecaoCasoCategoriaDoIdNaoSejaMetodo() throws Exception {
		contexto.pushId(new IdVariavel(token().getLexeme()));
		semantico.executeAction(137, token());
	}

	@Test(expected=SemanticError.class)
	public void action137DeveLancarExcecaoCasoCategoriaDoIdSejaMetodoETipoDoMetodoSejaDiferenteDeNulo() throws Exception {
		IdMetodo id = new IdMetodo(token().getLexeme());
		id.setTipo(Tipo.BOOLEANO);
		contexto.pushId(id);

		semantico.executeAction(137, token());
	}

	@Test
	public void action137DeveExecutarImpunementeCasoCategoriaDoIdSejaMetodoETipoDoMetodoSejaNulo() throws Exception {
		IdMetodo id = new IdMetodo(token().getLexeme());
		id.setTipo(Tipo.NULO);
		contexto.pushId(id);

		semantico.executeAction(137, token());
	}


	@Test
	public void action138DeveInicializarNovoContextoMetodoEIncluirNaPilha() throws Exception {
		semantico.executeAction(138, token());

		ContextoMetodo contextoMetodo = contexto.popContextoMetodo();
		assertNotNull(contextoMetodo);
	}

	@Test
	public void action138DeveAlterarNumParametrosAtuaisParaZero() throws Exception {
		semantico.executeAction(138, token());

		ContextoMetodo contextoMetodo = contexto.popContextoMetodo();
		assertEquals(0, contextoMetodo.getNumParametrosAtuais());
	}

	@Test
	public void action138DeveAlterarContextoEXPRParaParAtual() throws Exception {
		semantico.executeAction(138, token());
		assertEquals(ContextoEXPR.PAR_ATUAL, contexto.getContextoEXPR());
	}

	@Test(expected=SemanticError.class)
	public void action139DeveLancarExcecaoCasoIdSejaMetodoENumParametrosFormaisDiferentesDeNumParametrosAtuais() throws Exception {
		IdMetodo id = new IdMetodo(token().getLexeme());
		id.incluirParametro(new IdParametro(""));
		contexto.pushId(id);
		ContextoMetodo contextoMetodo = new ContextoMetodo();
		contextoMetodo.setNumParametrosAtuais(0);
		contexto.pushContextoMetodo(contextoMetodo);

		semantico.executeAction(139, token());
	}

	@Test
	public void action139DeveGerarCodigoCasoIdSejaMetodoENumParametrosFormaisIgualAoNumParametrosAtuais() throws Exception {
		IdMetodo id = new IdMetodo(token().getLexeme());
		ContextoMetodo contextoMetodo = new ContextoMetodo();
		contextoMetodo.setNumParametrosAtuais(0);
		contexto.pushContextoMetodo(contextoMetodo);
		contexto.pushId(id);

		semantico.executeAction(139, token());

		assertTrue(semantico.isGeradorCodigoAcionado());
	}

	@Test(expected=EmptyStackException.class)
	public void action139DeveDesempilharIdCasoIdSejaMetodoENumParametrosFormaisIgualAoNumParametrosAtuais() throws Exception {
		IdMetodo id = new IdMetodo(token().getLexeme());
		ContextoMetodo contextoMetodo = new ContextoMetodo();
		contextoMetodo.setNumParametrosAtuais(0);
		contexto.pushContextoMetodo(contextoMetodo);
		contexto.pushId(id);
		assertNotNull(contexto.peekId());

		semantico.executeAction(139, token());

		contexto.peekId();
	}

	@Test(expected=SemanticError.class)
	public void action140DeveLancarExcecaoCasoCategoriaDoIdSejaDiferenteDeMetodo() throws Exception {
		contexto.pushId(new IdVariavel(token().getLexeme()));
		semantico.executeAction(140, token());
	}


	@Test(expected=SemanticError.class)
	public void action140DeveLAncarExcecaoCasoCategoriaDoIdSejaMetodoETipoDiferenteDeNulo() throws Exception {
		IdMetodo id = new IdMetodo(token().getLexeme());
		id.setTipo(Tipo.BOOLEANO);
		contexto.pushId(id);
		semantico.executeAction(140, token());
	}

	@Test(expected=SemanticError.class)
	public void action140DeveLAncarExcecaoCasoCategoriaDoIdSejaMetodoTipoNuloENumParametrosFormaisSejaDiferenteDeZero() throws Exception {
		IdMetodo id = new IdMetodo(token().getLexeme());
		id.setTipo(Tipo.NULO);
		id.incluirParametro(new IdParametro(""));
		contexto.pushId(id);
		semantico.executeAction(140, token());
	}

	@Test
	public void action140DeveGerarCodigoCasoCategoriaDoIdSejaMetodoTipoNuloENumParametrosFormaisSejaZero() throws Exception {
		IdMetodo id = new IdMetodo(token().getLexeme());
		id.setTipo(Tipo.NULO);
		contexto.pushId(id);

		semantico.executeAction(140, token());

		assertTrue(semantico.isGeradorCodigoAcionado());
	}

	@Test(expected=EmptyStackException.class)
	public void action140DeveDesempilharIdCasoCategoriaDoIdSejaMetodoTipoNuloENumParametrosFormaisSejaZero() throws Exception {
		IdMetodo id = new IdMetodo(token().getLexeme());
		id.setTipo(Tipo.NULO);
		contexto.pushId(id);
		assertNotNull(contexto.peekId());

		semantico.executeAction(140, token());

		contexto.peekId();
	}


	@Test
	public void action141DeveIncrementarNumParametrosAtuaisCasoContextoEXPRSejaParAtual() throws Exception {
		ContextoMetodo contextoMetodo = new ContextoMetodo();
		contexto.pushContextoMetodo(contextoMetodo);

		IdMetodo idMetodo = new IdMetodo(token().getLexeme());
		IdParametro idParametro = new IdParametro("");
		idParametro.setTipo(Tipo.BOOLEANO);
		idParametro.setMpp(MetodoPassagem.VALOR);
		idMetodo.incluirParametro(idParametro);
		contexto.pushId(idMetodo);
		contexto.pushTipoExpr(Tipo.BOOLEANO);
		contexto.setContextoEXPR(ContextoEXPR.PAR_ATUAL);

		semantico.executeAction(141, token());

		assertEquals(1, contextoMetodo.getNumParametrosAtuais());
	}

	@Test(expected=SemanticError.class)
	public void action141DeveLancarExcecaoCasoParametroAtualNaoExista() throws Exception {
		ContextoMetodo contextoMetodo = new ContextoMetodo();
		contexto.pushContextoMetodo(contextoMetodo);

		IdMetodo idMetodo = new IdMetodo(token().getLexeme());
		contexto.pushId(idMetodo);
		contexto.pushTipoExpr(Tipo.BOOLEANO);
		contexto.setContextoEXPR(ContextoEXPR.PAR_ATUAL);

		semantico.executeAction(141, token());
	}

	@Test(expected=SemanticError.class)
	public void action141DeveLancarExcecaoCasoTipoDoParametroAtualNaoCorrespondaAoTipoDoParametroFormal() throws Exception {
		ContextoMetodo contextoMetodo = new ContextoMetodo();
		contexto.pushContextoMetodo(contextoMetodo);

		IdMetodo idMetodo = new IdMetodo(token().getLexeme());
		IdParametro idParametro = new IdParametro("");
		idParametro.setTipo(Tipo.INTEIRO);
		idParametro.setMpp(MetodoPassagem.VALOR);
		idMetodo.incluirParametro(idParametro);
		contexto.pushId(idMetodo);
		contexto.pushTipoExpr(Tipo.BOOLEANO);
		contexto.setContextoEXPR(ContextoEXPR.PAR_ATUAL);

		semantico.executeAction(141, token());
	}

	@Test(expected=SemanticError.class)
	public void action141DeveLancarExcecaoCasoMppDoParametroFormalSejaRefECategoriaDoParametroAtualSejaDiferenteDeVariavelOuParametro() throws Exception {
		ContextoMetodo contextoMetodo = new ContextoMetodo();
		contexto.pushContextoMetodo(contextoMetodo);

		IdMetodo idMetodo = new IdMetodo("metodo");
		IdParametro idParametro = new IdParametro("param");
		idParametro.setTipo(Tipo.INTEIRO);
		idParametro.setMpp(MetodoPassagem.REFERENCIA);
		idMetodo.incluirParametro(idParametro);
		contexto.pushId(idMetodo);
		contexto.pushTipoExpr(Tipo.INTEIRO);
		contexto.setContextoEXPR(ContextoEXPR.PAR_ATUAL);

		tabela.incluirIdentificador(new IdConstante(token().getLexeme()));

		semantico.executeAction(141, token());
	}

	@Test
	public void action141NaoDeveLancarExcecaoCasoMppDoParametroFormalSejaRefECategoriaDoParametroAtualSejaVariavel() throws Exception {
		ContextoMetodo contextoMetodo = new ContextoMetodo();
		contexto.pushContextoMetodo(contextoMetodo);

		IdMetodo idMetodo = new IdMetodo("metodo");
		IdParametro idParametro = new IdParametro("param");
		idParametro.setTipo(Tipo.INTEIRO);
		idParametro.setMpp(MetodoPassagem.REFERENCIA);
		idMetodo.incluirParametro(idParametro);
		contexto.pushId(idMetodo);
		contexto.pushTipoExpr(Tipo.INTEIRO);
		contexto.setContextoEXPR(ContextoEXPR.PAR_ATUAL);
		contexto.setReferenciaValida(true);

		tabela.incluirIdentificador(new IdVariavel(token().getLexeme()));

		semantico.executeAction(141, token());
	}

	@Test
	public void action141NaoDeveLancarExcecaoCasoMppDoParametroFormalSejaRefECategoriaDoParametroAtualSejaParametro() throws Exception {
		ContextoMetodo contextoMetodo = new ContextoMetodo();
		contexto.pushContextoMetodo(contextoMetodo);

		IdMetodo idMetodo = new IdMetodo("metodo");
		IdParametro idParametro = new IdParametro("param");
		idParametro.setTipo(Tipo.INTEIRO);
		idParametro.setMpp(MetodoPassagem.REFERENCIA);
		idMetodo.incluirParametro(idParametro);
		contexto.pushId(idMetodo);
		contexto.pushTipoExpr(Tipo.INTEIRO);
		contexto.setContextoEXPR(ContextoEXPR.PAR_ATUAL);
		contexto.setReferenciaValida(true);

		tabela.incluirIdentificador(new IdParametro(token().getLexeme()));

		semantico.executeAction(141, token());
	}

	@Test(expected=EmptyStackException.class)
	public void action141DeveDesempilharTipoExprCasoMppDoParametroFormalSejaRefECategoriaDoParametroAtualSejaParametro() throws Exception {
		ContextoMetodo contextoMetodo = new ContextoMetodo();
		contexto.pushContextoMetodo(contextoMetodo);

		IdMetodo idMetodo = new IdMetodo("metodo");
		IdParametro idParametro = new IdParametro("param");
		idParametro.setTipo(Tipo.INTEIRO);
		idParametro.setMpp(MetodoPassagem.REFERENCIA);
		idMetodo.incluirParametro(idParametro);
		contexto.pushId(idMetodo);
		contexto.pushTipoExpr(Tipo.INTEIRO);
		contexto.setContextoEXPR(ContextoEXPR.PAR_ATUAL);
		contexto.setReferenciaValida(true);
		tabela.incluirIdentificador(new IdParametro(token().getLexeme()));
		assertNotNull(contexto.peekTipoExpr());

		semantico.executeAction(141, token());

		contexto.peekTipoExpr();
	}

	@Test(expected=SemanticError.class)
	public void action141DeveLancarExcecaoCasoContextoEXPRSejaImpressaoETipoExpreSejaBooleano() throws Exception {
		contexto.setContextoEXPR(ContextoEXPR.IMPRESSAO);
		contexto.pushTipoExpr(Tipo.BOOLEANO);

		semantico.executeAction(141, token());
	}

	@Test
	public void action141DeveGerarCodigoCasoContextoEXPRSejaImpressaoETipoExpreSejaDiferenteDeBooleano() throws Exception {
		contexto.setContextoEXPR(ContextoEXPR.IMPRESSAO);
		contexto.pushTipoExpr(Tipo.INTEIRO);

		semantico.executeAction(141, token());

		assertTrue(semantico.isGeradorCodigoAcionado());
	}

	@Test(expected=EmptyStackException.class)
	public void action141DeveDesempilharTipoExprCasoContextoEXPRSejaImpressaoETipoExpreSejaDiferenteDeBooleano() throws Exception {
		contexto.setContextoEXPR(ContextoEXPR.IMPRESSAO);
		contexto.pushTipoExpr(Tipo.INTEIRO);
		assertNotNull(contexto.peekTipoExpr());

		semantico.executeAction(141, token());

		contexto.peekTipoExpr();
	}

	@Test
	public void action142DeveEmpilharTipoExprComValorDeTipoExpSimples() throws Exception {
		contexto.pushTipoExprSimples(Tipo.INTEIRO);

		semantico.executeAction(142, token());

		assertEquals(Tipo.INTEIRO, contexto.peekTipoExpr());
	}

	@Test(expected=EmptyStackException.class)
	public void action142DeveDesempilharTipoExprSimples() throws Exception {
		contexto.pushTipoExprSimples(Tipo.INTEIRO);

		semantico.executeAction(142, token());

		contexto.peekTipoExprSimples();
	}

	@Test(expected=SemanticError.class)
	public void action143DeveLancarExcecaoCasoTipoExpSimplesSejaIncompativelComTipoExpr() throws Exception {
		contexto.pushTipoExpr(Tipo.BOOLEANO);
		contexto.pushTipoExprSimples(Tipo.REAL);

		semantico.executeAction(143, token());
	}

	@Test
	public void action143AlterarTipoExprParaBooleanoCasoTipoExpSimplesSejaIncompativelComTipoExpr() throws Exception {
		contexto.pushTipoExpr(Tipo.INTEIRO);
		contexto.pushTipoExprSimples(Tipo.REAL);

		semantico.executeAction(143, token());

		assertEquals(Tipo.BOOLEANO, contexto.peekTipoExpr());
	}

	@Test(expected=EmptyStackException.class)
	public void action143DesempilharTipoExpSimplesCasoTipoExpSimplesSejaIncompativelComTipoExpr() throws Exception {
		contexto.pushTipoExpr(Tipo.INTEIRO);
		contexto.pushTipoExprSimples(Tipo.REAL);

		semantico.executeAction(143, token());

		contexto.peekTipoExprSimples();
	}

	@Test
	public void action144DeveEmpilharOperadorRelacionalEQ() throws Exception {
		semantico.executeAction(144, token());
		assertEquals(OperadorRel.EQ, contexto.popOperadorRel());
	}

	@Test
	public void action145DeveEmpilharOperadorRelacionalLT() throws Exception {
		semantico.executeAction(145, token());
		assertEquals(OperadorRel.LT, contexto.popOperadorRel());
	}

	@Test
	public void action146DeveEmpilharOperadorRelacionalGT() throws Exception {
		semantico.executeAction(146, token());
		assertEquals(OperadorRel.GT, contexto.popOperadorRel());
	}

	@Test
	public void action147DeveEmpilharOperadorRelacionalGE() throws Exception {
		semantico.executeAction(147, token());
		assertEquals(OperadorRel.GE, contexto.popOperadorRel());
	}

	@Test
	public void action148DeveEmpilharOperadorRelacionalLE() throws Exception {
		semantico.executeAction(148, token());
		assertEquals(OperadorRel.LE, contexto.popOperadorRel());
	}

	@Test
	public void action149DeveEmpilharOperadorRelacionalNE() throws Exception {
		semantico.executeAction(149, token());
		assertEquals(OperadorRel.NE, contexto.popOperadorRel());
	}

	@Test
	public void action150DeveEmpilharTipoExpSimplesComValorDeTipoTermo() throws Exception {
		contexto.pushTipoTermo(Tipo.REAL);

		semantico.executeAction(150, token());

		assertEquals(Tipo.REAL, contexto.popTipoExprSimples());
	}

	@Test(expected=EmptyStackException.class)
	public void action150DeveDesempilharTipoTermo() throws Exception {
		contexto.pushTipoTermo(Tipo.REAL);

		semantico.executeAction(150, token());

		contexto.peekTipoTermo();
	}

	@Test(expected=SemanticError.class)
	public void action151DeveLancarExcecaoCasoOperadorAdicaoSejaUsadoComValorDiferenteDeRealOuInteiro() throws Exception {
		contexto.pushTipoExprSimples(Tipo.BOOLEANO);
		contexto.pushOperadorAdd(OperadorAdd.ADD);

		semantico.executeAction(151, token());
	}

	@Test(expected=SemanticError.class)
	public void action151DeveLancarExcecaoCasoOperadorSubtracaoSejaUsadoComValorDiferenteDeRealOuInteiro() throws Exception {
		contexto.pushTipoExprSimples(Tipo.CADEIA);
		contexto.pushOperadorAdd(OperadorAdd.SUB);

		semantico.executeAction(151, token());
	}

	@Test(expected=SemanticError.class)
	public void action151DeveLancarExcecaoCasoOperadorOuSejaUsadoComValorDiferenteDeBooleano() throws Exception {
		contexto.pushTipoExprSimples(Tipo.INTEIRO);
		contexto.pushOperadorAdd(OperadorAdd.OU);

		semantico.executeAction(151, token());
	}

	@Test
	public void action151NaoDeveLancarExcecaoCasoOperadorOuSejaUsadoComValorBooleano() throws Exception {
		contexto.pushTipoExprSimples(Tipo.BOOLEANO);
		contexto.pushOperadorAdd(OperadorAdd.OU);

		semantico.executeAction(151, token());
	}

	@Test
	public void action151NaoDeveLancarExcecaoCasoOperadorAritmeticoSejaUsadoComValorInteiro() throws Exception {
		contexto.pushTipoExprSimples(Tipo.INTEIRO);
		contexto.pushOperadorAdd(OperadorAdd.ADD);

		semantico.executeAction(151, token());
	}

	@Test(expected=SemanticError.class)
	public void action152DeveLancarExcecaoCasoTipoTermoSejaIncompativelComTipoExprSimples() throws Exception {
		contexto.pushTipoExprSimples(Tipo.BOOLEANO);
		contexto.pushTipoTermo(Tipo.REAL);

		semantico.executeAction(152, token());
	}

	@Test
	public void action152DeveGerarCodigoCasoTipoTermoSejaInteiroETipoExprSimplesSejaReal() throws Exception {
		contexto.pushTipoExprSimples(Tipo.REAL);
		contexto.pushTipoTermo(Tipo.INTEIRO);

		semantico.executeAction(152, token());

		assertTrue(semantico.isGeradorCodigoAcionado());
	}

	@Test
	public void action152DeveGerarCodigoCasoTipoTermoSejaRealETipoExprSimplesSejaInteiro() throws Exception {
		contexto.pushTipoExprSimples(Tipo.INTEIRO);
		contexto.pushTipoTermo(Tipo.REAL);

		semantico.executeAction(152, token());

		assertTrue(semantico.isGeradorCodigoAcionado());
	}

	@Test
	public void action152DeveGerarCodigoCasoTipoTermoSejaBooleanoETipoExprSimplesSejaBooleano() throws Exception {
		contexto.pushTipoExprSimples(Tipo.BOOLEANO);
		contexto.pushTipoTermo(Tipo.BOOLEANO);

		semantico.executeAction(152, token());

		assertTrue(semantico.isGeradorCodigoAcionado());
	}

	@Test
	public void action152DeveAlterarValorDeTipoExprSimplesParaBooleanoCasoAmbosOperandosSejamBooleanos() throws Exception {
		contexto.pushTipoExprSimples(Tipo.BOOLEANO);
		contexto.pushTipoTermo(Tipo.BOOLEANO);

		semantico.executeAction(152, token());

		assertEquals(Tipo.BOOLEANO, contexto.popTipoExprSimples());
	}

	@Test
	public void action152DeveAlterarValorDeTipoExprSimplesParaRealCasoTipoTermoSejaInteiroETipoExprSimplesSejaReal() throws Exception {
		contexto.pushTipoExprSimples(Tipo.REAL);
		contexto.pushTipoTermo(Tipo.INTEIRO);

		semantico.executeAction(152, token());

		assertEquals(Tipo.REAL, contexto.popTipoExprSimples());
	}

	@Test
	public void action152DeveAlterarValorDeTipoExprSimplesParaRealCasoTipoTermoSejaRealETipoExprSimplesSejaInteiro() throws Exception {
		contexto.pushTipoExprSimples(Tipo.INTEIRO);
		contexto.pushTipoTermo(Tipo.REAL);

		semantico.executeAction(152, token());

		assertEquals(Tipo.REAL, contexto.popTipoExprSimples());
	}

	@Test
	public void action152DeveAlterarValorDeTipoExprSimplesParaValorDeTipoExprSimplesCasoOperandosSejamDeMesmoTipo() throws Exception {
		contexto.pushTipoExprSimples(Tipo.INTEIRO);
		contexto.pushTipoTermo(Tipo.INTEIRO);

		semantico.executeAction(152, token());

		assertEquals(Tipo.INTEIRO, contexto.popTipoExprSimples());
	}

	@Test(expected=EmptyStackException.class)
	public void action152DeveDesempilharTipoTipoTermoCasoOperandosCompativeis() throws Exception {
		contexto.pushTipoExprSimples(Tipo.INTEIRO);
		contexto.pushTipoTermo(Tipo.INTEIRO);

		semantico.executeAction(152, token());

		contexto.peekTipoTermo();
	}

	@Test
	public void action153DeveEmpilharOperadorAddADD() throws Exception {
		semantico.executeAction(153, token());
		assertEquals(OperadorAdd.ADD, contexto.popOperadorAdd());
	}

	@Test
	public void action154DeveEmpilharOperadorAddSUB() throws Exception {
		semantico.executeAction(154, token());
		assertEquals(OperadorAdd.SUB, contexto.popOperadorAdd());
	}

	@Test
	public void action155DeveEmpilharOperadorAddOU() throws Exception {
		semantico.executeAction(155, token());
		assertEquals(OperadorAdd.OU, contexto.popOperadorAdd());
	}

	@Test
	public void action156DeveAlterarEmpilharTipoTermoComValorDeTipoFator() throws Exception {
		contexto.pushTipoFator(Tipo.REAL);

		semantico.executeAction(156, token());

		assertEquals(Tipo.REAL, contexto.popTipoTermo());
	}

	@Test(expected=EmptyStackException.class)
	public void action156DeveDesempilharTipoFator() throws Exception {
		contexto.pushTipoFator(Tipo.REAL);

		semantico.executeAction(156, token());

		contexto.peekTipoFator();
	}

	@Test(expected=SemanticError.class)
	public void action157DeveLancarExcecaoCasoOperadorMULSejaUsadoComValorDiferenteDeRealOuInteiro() throws Exception {
		contexto.pushOperadorMult(OperadorMult.MUL);
		contexto.pushTipoTermo(Tipo.CADEIA);

		semantico.executeAction(157, token());
	}

	@Test(expected=SemanticError.class)
	public void action157DeveLancarExcecaoCasoOperadorFRCSejaUsadoComValorDiferenteDeRealOuInteiro() throws Exception {
		contexto.pushOperadorMult(OperadorMult.FRC);
		contexto.pushTipoTermo(Tipo.BOOLEANO);

		semantico.executeAction(157, token());
	}

	@Test(expected=SemanticError.class)
	public void action157DeveLancarExcecaoCasoOperadorESejaUsadoComValorDiferenteDeBooleano() throws Exception {
		contexto.pushOperadorMult(OperadorMult.E);
		contexto.pushTipoTermo(Tipo.INTEIRO);

		semantico.executeAction(157, token());
	}

	@Test(expected=SemanticError.class)
	public void action157DeveLancarExcecaoCasoOperadorDIVSejaUsadoComValorDiferenteDeInteiro() throws Exception {
		contexto.pushOperadorMult(OperadorMult.DIV);
		contexto.pushTipoTermo(Tipo.REAL);

		semantico.executeAction(157, token());
	}

	@Test
	public void action157NaoDeveLancarExcecaoCasoOperadorDIVSejaUsadoComValorInteiro() throws Exception {
		contexto.pushOperadorMult(OperadorMult.DIV);
		contexto.pushTipoTermo(Tipo.INTEIRO);

		semantico.executeAction(157, token());
	}

	@Test
	public void action157NaoDeveLancarExcecaoCasoOperadorMULSejaUsadoComValorReal() throws Exception {
		contexto.pushOperadorMult(OperadorMult.MUL);
		contexto.pushTipoTermo(Tipo.REAL);

		semantico.executeAction(157, token());
	}

	@Test
	public void action157NaoDeveLancarExcecaoCasoOperadorFRCSejaUsadoComValorInteiro() throws Exception {
		contexto.pushOperadorMult(OperadorMult.FRC);
		contexto.pushTipoTermo(Tipo.INTEIRO);

		semantico.executeAction(157, token());
	}

	@Test
	public void action157NaoDeveLancarExcecaoCasoOperadorESejaUsadoComValorBOOLEANO() throws Exception {
		contexto.pushOperadorMult(OperadorMult.E);
		contexto.pushTipoTermo(Tipo.BOOLEANO);

		semantico.executeAction(157, token());
	}

	@Test(expected=SemanticError.class)
	public void action158DeveLancarExcecaoCasoOperadorDIVSejaUsadoComTipoTermoDiferenteDeInteiro() throws Exception {
		contexto.pushOperadorMult(OperadorMult.DIV);
		contexto.pushTipoTermo(Tipo.REAL);
		contexto.pushTipoFator(Tipo.INTEIRO);

		semantico.executeAction(158, token());
	}

	@Test(expected=SemanticError.class)
	public void action158DeveLancarExcecaoCasoOperadorDIVSejaUsadoComTipoFatorDiferenteDeInteiro() throws Exception {
		contexto.pushOperadorMult(OperadorMult.DIV);
		contexto.pushTipoTermo(Tipo.INTEIRO);
		contexto.pushTipoFator(Tipo.REAL);

		semantico.executeAction(158, token());
	}

	@Test(expected=SemanticError.class)
	public void action158DeveLancarExcecaoCasoOperadorMULSejaUsadoComTipoFatorIncompativel() throws Exception {
		contexto.pushOperadorMult(OperadorMult.MUL);
		contexto.pushTipoTermo(Tipo.INTEIRO);
		contexto.pushTipoFator(Tipo.CADEIA);

		semantico.executeAction(158, token());
	}

	@Test(expected=SemanticError.class)
	public void action158DeveLancarExcecaoCasoOperadorFRCSejaUsadoComTipoFatorIncompativel() throws Exception {
		contexto.pushOperadorMult(OperadorMult.FRC);
		contexto.pushTipoTermo(Tipo.REAL);
		contexto.pushTipoFator(Tipo.BOOLEANO);

		semantico.executeAction(158, token());
	}

	@Test
	public void action158DeveEmpilharTipoRealEmTipoTermoCasoOperadorFRCSejaUsadoComOperandosCompativeis() throws Exception {
		contexto.pushOperadorMult(OperadorMult.FRC);
		contexto.pushTipoTermo(Tipo.INTEIRO);
		contexto.pushTipoFator(Tipo.INTEIRO);

		semantico.executeAction(158, token());

		assertEquals(Tipo.REAL, contexto.popTipoTermo());
	}

	@Test
	public void action158DeveEmpilharTipoInteiroEmTipoTermoCasoOperadorDIVSejaUsadoComOperandosCompativeis() throws Exception {
		contexto.pushOperadorMult(OperadorMult.DIV);
		contexto.pushTipoTermo(Tipo.INTEIRO);
		contexto.pushTipoFator(Tipo.INTEIRO);

		semantico.executeAction(158, token());

		assertEquals(Tipo.INTEIRO, contexto.popTipoTermo());
	}

	@Test
	public void action158DeveEmpilharTipoRealEmTipoTermoCasoOperadorMULSejaUsadoComOperandosMistos() throws Exception {
		contexto.pushOperadorMult(OperadorMult.MUL);
		contexto.pushTipoTermo(Tipo.INTEIRO);
		contexto.pushTipoFator(Tipo.REAL);

		semantico.executeAction(158, token());

		assertEquals(Tipo.REAL, contexto.popTipoTermo());
	}

	@Test
	public void action158DeveGerarCodigoOperandosSejamCompativeis() throws Exception {
		contexto.pushOperadorMult(OperadorMult.E);
		contexto.pushTipoTermo(Tipo.BOOLEANO);
		contexto.pushTipoFator(Tipo.BOOLEANO);

		semantico.executeAction(158, token());

		assertTrue(semantico.isGeradorCodigoAcionado());
	}

	@Test(expected=EmptyStackException.class)
	public void action158DeveDesempilharTipoFatorCasoOperandosCompativeis() throws Exception {
		contexto.pushOperadorMult(OperadorMult.E);
		contexto.pushTipoFator(Tipo.REAL);
		contexto.pushTipoTermo(Tipo.REAL);

		semantico.executeAction(158, token());

		contexto.peekTipoFator();
	}

	@Test
	public void action158DeveEmpilharValorDeTipoTermoEmTipoTermoCasoOperadorESejaUsadoComBooleanos() throws Exception {
		contexto.pushOperadorMult(OperadorMult.E);
		contexto.pushTipoTermo(Tipo.BOOLEANO);
		contexto.pushTipoFator(Tipo.BOOLEANO);

		semantico.executeAction(158, token());

		assertEquals(Tipo.BOOLEANO, contexto.popTipoTermo());
	}

	@Test
	public void action159DeveEmpilharOperadorMultMUL() throws Exception {
		semantico.executeAction(159, token());
		assertEquals(OperadorMult.MUL, contexto.popOperadorMult());
	}

	@Test
	public void action160DeveEmpilharOperadorMultFRC() throws Exception {
		semantico.executeAction(160, token());
		assertEquals(OperadorMult.FRC, contexto.popOperadorMult());
	}

	@Test
	public void action161DeveEmpilharOperadorMultE() throws Exception {
		semantico.executeAction(161, token());
		assertEquals(OperadorMult.E, contexto.popOperadorMult());
	}

	@Test
	public void action162DeveEmpilharOperadorMultDIV() throws Exception {
		semantico.executeAction(162, token());
		assertEquals(OperadorMult.DIV, contexto.popOperadorMult());
	}

	@Test(expected=SemanticError.class)
	public void action163DeveLancarExcecaoCasoOpNegaSejaVerdadeiro() throws Exception {
		contexto.setOpNega(true);
		semantico.executeAction(163, token());
	}

	@Test
	public void action163DeveAlterarParaVerdadeiroOpNegaCasoOpNegaSejaFalso() throws Exception {
		contexto.setOpNega(false);

		semantico.executeAction(163, token());

		assertTrue(contexto.isOpNega());
	}


	@Test(expected=SemanticError.class)
	public void action164DeveLancarExcecaoCasoTipoFatorSejaDiferenteDeBooleano() throws Exception {
		contexto.pushTipoFator(Tipo.INTEIRO);
		semantico.executeAction(164, token());
	}

	@Test
	public void action164DeveAlterarParaFalsoOpNegaCasoTipoFatorSejaBooleano() throws Exception {
		contexto.pushTipoFator(Tipo.BOOLEANO);

		semantico.executeAction(164, token());

		assertFalse(contexto.isOpNega());
	}

	@Test
	public void action164NaoDeveDesempilharTipoFator() throws Exception {
		contexto.pushTipoFator(Tipo.BOOLEANO);

		semantico.executeAction(164, token());

		assertEquals(Tipo.BOOLEANO, contexto.popTipoFator());
	}

	@Test(expected=SemanticError.class)
	public void action165DeveLancarExcecaoCasoOpUnarioSejaVerdeiro() throws Exception {
		contexto.setOpUnario(true);
		semantico.executeAction(165, token());
	}

	@Test
	public void action165DeveAlterarPAraVerdadeiroOpUnarioCasoOpUnarioSejaFalse() throws Exception {
		contexto.setOpUnario(false);

		semantico.executeAction(165, token());

		assertTrue(contexto.isOpUnario());
	}

	@Test(expected=SemanticError.class)
	public void action166DeveLancarExcecaoCasoTipoFatorSejaDiferenteDeInteiroOuReal() throws Exception {
		contexto.pushTipoFator(Tipo.BOOLEANO);
		semantico.executeAction(166, token());
	}

	@Test
	public void action166DeveAlterarParaFalsoOpUnarioCasoTipoFatorSejaInteiro() throws Exception {
		contexto.pushTipoFator(Tipo.INTEIRO);
		semantico.executeAction(166, token());

		assertFalse(contexto.isOpUnario());
	}

	@Test
	public void action166DeveAlterarParaFalsoOpUnarioCasoTipoFatorSejaReal() throws Exception {
		contexto.pushTipoFator(Tipo.REAL);
		semantico.executeAction(166, token());

		assertFalse(contexto.isOpUnario());
	}

	@Test
	public void action166NaoDeveDesempilharTipoFator() throws Exception {
		contexto.pushTipoFator(Tipo.REAL);

		semantico.executeAction(166, token());

		assertEquals(Tipo.REAL, contexto.popTipoFator());
	}

	@Test
	public void action167DeveAlterarParaFalsoOpNega() throws Exception {
		semantico.executeAction(167, token());
		assertFalse(contexto.isOpNega());
	}

	@Test
	public void action167DeveAlterarPAraFalsoOpUnario() throws Exception {
		semantico.executeAction(167, token());
		assertFalse(contexto.isOpUnario());
	}

	@Test
	public void action168DeveEmpilharValorDeTipoExprEmTipoFator() throws Exception {
		contexto.pushTipoExpr(Tipo.CADEIA);

		semantico.executeAction(168, token());

		assertEquals(Tipo.CADEIA, contexto.popTipoFator());

	}

	@Test(expected=EmptyStackException.class)
	public void action168DeveDesempilharTipoExpr() throws Exception {
		contexto.pushTipoExpr(Tipo.CADEIA);

		semantico.executeAction(168, token());

		contexto.peekTipoExpr();
	}

	@Test
	public void action169DeveEmpilharValorDeTipoVarEmTipoFator() throws Exception {
		contexto.pushTipoVar(Tipo.CARACTER);
		contexto.pushId(new Identificador(token().getLexeme()));

		semantico.executeAction(169, token());

		assertEquals(Tipo.CARACTER, contexto.popTipoFator());
	}

	@Test(expected=EmptyStackException.class)
	public void action169DeveDesempilharTipoVar() throws Exception {
		contexto.pushTipoVar(Tipo.REAL);
		contexto.pushId(new Identificador(token().getLexeme()));

		semantico.executeAction(169, token());

		contexto.peekTipoVar();
	}

	@Test(expected=EmptyStackException.class)
	public void action169DeveDesempilharId() throws Exception {
		contexto.pushTipoVar(Tipo.REAL);
		contexto.pushId(new Identificador(token().getLexeme()));

		semantico.executeAction(169, token());

		contexto.peekId();
	}

	@Test
	public void action170DeveEmpilharValorDeTipoConstEmTipoFator() throws Exception {
		contexto.setTipoConst(Tipo.BOOLEANO);

		semantico.executeAction(170, token());

		assertEquals(Tipo.BOOLEANO, contexto.popTipoFator());
	}

	@Test(expected=SemanticError.class)
	public void action171DeveLancarExcecaoCasoCategoriaDoIdAtualSejaDiferenteDeMetodo() throws Exception {
		contexto.pushId(new IdVariavel(token().getLexeme()));
		semantico.executeAction(171, token());
	}

	@Test(expected=SemanticError.class)
	public void action171DeveLancarExcecaoCasoCategoriaDoIdAtualSejaMetodoMasTipoSejaNulo() throws Exception {
		IdMetodo id = new IdMetodo(token().getLexeme());
		id.setTipo(Tipo.NULO);
		contexto.pushId(id);
		semantico.executeAction(171, token());
	}

	@Test
	public void action171DeveInicializarOutroContextoMetodoCasoCategoriaDoIdAtualSejaMetodoMasTipoSejaDiferenteDeNulo() throws Exception {
		IdMetodo id = new IdMetodo(token().getLexeme());
		id.setTipo(Tipo.BOOLEANO);
		contexto.pushId(id);

		semantico.executeAction(171, token());

		assertNotNull(contexto.popContextoMetodo());
	}

	@Test
	public void action171DeveAlterarNumParametrosAtuaisParaZeroCasoCategoriaDoIdAtualSejaMetodoMasTipoSejaDiferenteDeNulo() throws Exception {
		IdMetodo id = new IdMetodo(token().getLexeme());
		id.setTipo(Tipo.BOOLEANO);
		contexto.pushId(id);

		semantico.executeAction(171, token());

		ContextoMetodo contextoMetodo = contexto.popContextoMetodo();
		assertEquals(0, contextoMetodo.getNumParametrosAtuais());
	}

	@Test
	public void action171DeveAlterarContextoExprParaParAtualCasoCategoriaDoIdAtualSejaMetodoMasTipoSejaDiferenteDeNulo() throws Exception {
		IdMetodo id = new IdMetodo(token().getLexeme());
		id.setTipo(Tipo.BOOLEANO);
		contexto.pushId(id);

		semantico.executeAction(171, token());

		assertEquals(ContextoEXPR.PAR_ATUAL, contexto.getContextoEXPR());
	}

	@Test(expected=SemanticError.class)
	public void action172DeveLancarExcecaoCasoNumParametrosAtuaisSejaDiferenteDoNumParametrosFormais() throws Exception {
		IdMetodo id = new IdMetodo(token().getLexeme());
		contexto.pushId(id);

		ContextoMetodo contextoMetodo = new ContextoMetodo();
		contextoMetodo.setNumParametrosAtuais(1);
		contexto.pushContextoMetodo(contextoMetodo);

		semantico.executeAction(172, token());
	}

	@Test
	public void action172DeveAlterarTipoVarParaTipoMetodoCasoNumParametrosAtuaisSejaIgualAoNumParametrosFormais() throws Exception {
		IdMetodo id = new IdMetodo(token().getLexeme());
		contexto.pushId(id);
		ContextoMetodo contextoMetodo = new ContextoMetodo();
		contextoMetodo.setNumParametrosAtuais(0);
		contexto.pushContextoMetodo(contextoMetodo);

		semantico.executeAction(172, token());

		assertEquals(contexto.popTipoVar(), contexto.peekId().getTipo());
	}

	@Test
	public void action172DeveGerarCodigoCasoNumParametrosAtuaisSejaIgualAoNumParametrosFormais() throws Exception {
		IdMetodo id = new IdMetodo(token().getLexeme());
		contexto.pushId(id);
		ContextoMetodo contextoMetodo = new ContextoMetodo();
		contextoMetodo.setNumParametrosAtuais(0);
		contexto.pushContextoMetodo(contextoMetodo);

		semantico.executeAction(172, token());

		assertTrue(semantico.isGeradorCodigoAcionado());
	}

	@Test(expected=SemanticError.class)
	public void action173DeveLancarExcecaoCasoTipoExprEmpilhadaSejaDiferenteDeInteiro() throws Exception {
		contexto.pushTipoExpr(Tipo.BOOLEANO);
		semantico.executeAction(173, token());
	}

	@Test
	public void action173DeveEmpilharCaracterEmTipoVarCasoTipoExprEmpilhadaSejaInteiroESubCategoriaVarIndexadaEmpilhadaSejaCadeia() throws Exception {
		contexto.pushSubCategoriaVarIndexada(SubCategoria.CADEIA);
		contexto.pushTipoExpr(Tipo.INTEIRO);

		semantico.executeAction(173, token());

		assertEquals(Tipo.CARACTER, contexto.popTipoVar());
	}

	@Test
	public void action173DeveEmpilharTipoDoVetorEmTipoVarCasoTipoExprEmpilhadoSejaInteiroESubCategoriaVarIndexadaEmpilhadaSejaDiferenteDeCadeia() throws Exception {
		contexto.pushSubCategoriaVarIndexada(SubCategoria.VETOR);
		contexto.pushTipoExpr(Tipo.INTEIRO);
		IdVariavel id = new IdVariavel(token().getLexeme());
		id.setTipo(Tipo.BOOLEANO);
		contexto.pushId(id);

		semantico.executeAction(173, token());

		assertEquals(Tipo.BOOLEANO, contexto.popTipoVar());
	}

	@Test(expected=EmptyStackException.class)
	public void action173DeveDesempilharTipoExpr() throws Exception {
		contexto.pushTipoExpr(Tipo.INTEIRO);
		contexto.pushSubCategoriaVarIndexada(SubCategoria.PREDEFINIDO);
		contexto.pushId(new Identificador(""));

		semantico.executeAction(173, token());

		contexto.peekTipoExpr();
	}

	@Test(expected=EmptyStackException.class)
	public void action173DeveDesempilharSubCategoriaVarIndexada() throws Exception {
		contexto.pushTipoExpr(Tipo.INTEIRO);
		contexto.pushSubCategoriaVarIndexada(SubCategoria.PREDEFINIDO);
		contexto.pushId(new Identificador(""));

		semantico.executeAction(173, token());

		contexto.peekSubCategoriaVarIndexada();
	}

	@Test(expected=SemanticError.class)
	public void action174DeveLancarExcecaoCasoCategoriaDoIdSejaVariavelESubCategoriaSejaVetor() throws Exception {
		IdVariavel id = new IdVariavel(token().getLexeme());
		id.setSubCategoria(SubCategoria.VETOR);
		contexto.pushId(id);

		semantico.executeAction(174, token());
	}

	@Test(expected=SemanticError.class)
	public void action174DeveLancarExcecaoCasoCategoriaDoIdSejaParametroESubCategoriaSejaVetor() throws Exception {
		IdParametro id = new IdParametro(token().getLexeme());
		id.setSubCategoria(SubCategoria.VETOR);
		contexto.pushId(id);

		semantico.executeAction(174, token());
	}

	@Test
	public void action174DeveAlterarTipoVarParaTipoDoIdCasoCategoriaDoIdSejaVariavelESubCategoriaSejaDiferenteDeVetor() throws Exception {
		IdParametro id = new IdParametro(token().getLexeme());
		id.setTipo(Tipo.INTEIRO);
		id.setSubCategoria(SubCategoria.PREDEFINIDO);
		contexto.pushId(id);

		semantico.executeAction(174, token());

		assertEquals(id.getTipo(), contexto.popTipoVar());
	}

	@Test(expected=SemanticError.class)
	public void action174DeveLancarExcecaoCasoCategoriaDoIdSejaMetodoETipoDoMetodoSejaNulo() throws Exception {
		IdMetodo id = new IdMetodo(token().getLexeme());
		id.setTipo(Tipo.NULO);
		contexto.pushId(id);

		semantico.executeAction(174, token());
	}

	@Test(expected=SemanticError.class)
	public void action174DeveLancarExcecaoCasoCategoriaDoIdSejaMetodoTipoDoMetodoSejaDiferenteDeNuloMasNumParametrosFormaisSejaDiferenteDeZero() throws Exception {
		IdMetodo id = new IdMetodo(token().getLexeme());
		id.setTipo(Tipo.INTEIRO);
		id.incluirParametro(new IdParametro(""));
		contexto.pushId(id);

		semantico.executeAction(174, token());
	}

	@Test
	public void action174DeveAlterarTipoVarParaTipoMetodoCasoCategoriaDoIdSejaMetodoTipoDoMetodoSejaDiferenteDeNuloENumParametrosFormaisSejaZero() throws Exception {
		IdMetodo id = new IdMetodo(token().getLexeme());
		id.setTipo(Tipo.INTEIRO);
		contexto.pushId(id);

		semantico.executeAction(174, token());

		assertEquals(contexto.peekId().getTipo(), contexto.popTipoVar());
	}

	@Test
	public void action174DeveGerarCodigoCasoCategoriaDoIdSejaMetodoTipoDoMetodoSejaDiferenteDeNuloENumParametrosFormaisSejaZero() throws Exception {
		IdMetodo id = new IdMetodo(token().getLexeme());
		id.setTipo(Tipo.INTEIRO);
		contexto.pushId(id);

		semantico.executeAction(174, token());

		assertTrue(semantico.isGeradorCodigoAcionado());
	}

	@Test
	public void action174DeveAlterarTipoVarParaTipoDoIdAtualCasoCategoriaDoIdAtualSejaConstante() throws Exception {
		IdConstante id = new IdConstante(token().getLexeme());
		id.setTipo(Tipo.BOOLEANO);
		contexto.pushId(id);

		semantico.executeAction(174, token());

		assertEquals(id.getTipo(), contexto.popTipoVar());
	}

	@Test(expected=SemanticError.class)
	public void action174DeveLancarExcecaoCasoCategoriaDoIdSejaPrograma() throws Exception {
		contexto.pushId(new IdPrograma(token().getLexeme()));
		semantico.executeAction(174, token());
	}

	@Test(expected=SemanticError.class)
	public void action175DeveLancarExcecaoCasoIdNaoDeclarado() throws Exception {
		semantico.executeAction(175, token());
	}

	@Test(expected=SemanticError.class)
	public void action175DeveLancarExcecaoCasoIdJaDeclaradoECategoriaDoIdNaoSejaConstante() throws Exception {
		Token token = token();
		contexto.inicializaListaDeclaracao();

		IdVariavel id = new IdVariavel(token.getLexeme());
		tabela.incluirIdentificador(id);

		semantico.executeAction(175, token);
	}

	@Test
	public void action175DeveAlterarTipoConstParaTipoDoIdCasoIdJaDeclaradoECategoriaDoIdSejaConstante() throws Exception {
		Token token = token();
		contexto.inicializaListaDeclaracao();

		IdConstante id = new IdConstante(token.getLexeme());
		id.setTipo(Tipo.INTEIRO);
		tabela.incluirIdentificador(id);

		semantico.executeAction(175, token);

		assertEquals(Tipo.INTEIRO, contexto.getTipoConst());
	}

	@Test
	public void action175DeveAlterarValConstParaValorDaConstanteCasoIdJaDeclaradoECategoriaDoIdSejaConstante() throws Exception {
		Token token = token();
		contexto.inicializaListaDeclaracao();

		IdConstante id = new IdConstante(token.getLexeme());
		id.setValor("valor");
		tabela.incluirIdentificador(id);

		semantico.executeAction(175, token);

		assertEquals("valor", contexto.getValConst());
	}

	@Test
	public void action176DeveAlterarTipoConstParaTipoDaConstanteInteiro() throws Exception {
		String valor = "14";
		semantico.executeAction(176, new Token(1, valor, 0));
		assertEquals(Tipo.INTEIRO, contexto.getTipoConst());
	}

	@Test
	public void action176DeveAlterarValConstParaValorDaConstanteInteiro() throws Exception {
		String valor = "14";
		semantico.executeAction(176, new Token(1, valor, 0));
		assertEquals(valor, contexto.getValConst());
	}

	@Test
	public void action177DeveAlterarTipoConstParaTipoDaConstanteReal() throws Exception {
		String valor = "14.4";
		semantico.executeAction(177, new Token(1, valor, 0));
		assertEquals(Tipo.REAL, contexto.getTipoConst());
	}

	@Test
	public void action177DeveAlterarValConstParaValorDaConstanteReal() throws Exception {
		String valor = "14.4";
		semantico.executeAction(177, new Token(1, valor, 0));
		assertEquals(valor, contexto.getValConst());
	}

	@Test
	public void action178DeveAlterarTipoConstParaTipoDaConstanteBooleanoFalso() throws Exception {
		String valor = "falso";
		semantico.executeAction(178, new Token(1, valor, 0));
		assertEquals(Tipo.BOOLEANO, contexto.getTipoConst());
	}

	@Test
	public void action178DeveAlterarValConstParaValorDaConstanteBooleanoFalso() throws Exception {
		String valor = "falso";
		semantico.executeAction(178, new Token(1, valor, 0));
		assertEquals(valor, contexto.getValConst());
	}

	@Test
	public void action179DeveAlterarTipoConstParaTipoDaConstanteBooleanoVerdadeiro() throws Exception {
		String valor = "verdadeiro";
		semantico.executeAction(179, new Token(1, valor, 0));
		assertEquals(Tipo.BOOLEANO, contexto.getTipoConst());
	}

	@Test
	public void action179DeveAlterarValConstParaValorDaConstanteBooleanoVerdadeiro() throws Exception {
		String valor = "verdadeiro";
		semantico.executeAction(179, new Token(1, valor, 0));
		assertEquals(valor, contexto.getValConst());
	}

	@Test
	public void action180DeveAlterarTipoConstParaTipoDaConstanteCadeia() throws Exception {
		String valor = "'abc'";
		semantico.executeAction(180, new Token(1, valor, 0));
		assertEquals(Tipo.CADEIA, contexto.getTipoConst());
	}

	@Test
	public void action180DeveAlterarValConstParaValorDaConstanteCadeia() throws Exception {
		String valor = "'abc'";
		semantico.executeAction(180, new Token(1, valor, 0));
		assertEquals(valor, contexto.getValConst());
	}

	@Test
	public void action180DeveAlterarTipoConstParaTipoDaConstanteCaractere() throws Exception {
		String valor = "'a'";
		semantico.executeAction(180, new Token(1, valor, 0));
		assertEquals(Tipo.CARACTER, contexto.getTipoConst());
	}

	@Test
	public void action180DeveAlterarValConstParaValorDaConstanteCaractere() throws Exception {
		String valor = "'a'";
		semantico.executeAction(180, new Token(1, valor, 0));
		assertEquals(valor, contexto.getValConst());
	}

	private Token token() {
		return token("lexeme");
	}

	private Token token(String lexeme) {
		return new Token(1, lexeme, 0);
	}

	private Identificador declararIdentificador(Categoria categoriaAtual, Token token) {
		contexto.setCategoriaAtual(categoriaAtual);
		contexto.inicializaListaDeclaracao();
		contexto.setSubCategoria(SubCategoria.PREDEFINIDO);

		List<Identificador> listaDeclaracao = contexto.getListaDeclaracao();
		Identificador idAntes = new Identificador(token.getLexeme());
		listaDeclaracao.add(idAntes);
		return idAntes;
	}

}
