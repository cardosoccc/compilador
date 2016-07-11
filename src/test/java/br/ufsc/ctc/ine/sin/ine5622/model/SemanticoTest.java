package br.ufsc.ctc.ine.sin.ine5622.model;

import static org.junit.Assert.*;

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
		contexto.setTipoConst(Tipo.INTEIRO);
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
	public void action118DeveAtualizarNumParametrosFormais() throws Exception {
		semantico.executeAction(118, token());
		assertEquals(1, contexto.getNumParametrosFormais());
	}

	@Test
	public void action119DeveAtualizarTipoDoMetodoAtual() throws Exception {
		Token token = token();
		IdMetodo idMetodo = new IdMetodo(token.getLexeme());
		contexto.setIdMetodoAtual(idMetodo);
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
		Map<String, Identificador> ids = tabela.getIdentificadoresPorNivel().get(1);
		assertNotNull(ids.get(token.getLexeme()));

		semantico.executeAction(120, token);

		assertNull(tabela.getIdentificadoresPorNivel().get(1));
	}

	@Test
	public void action120DeveDecrementarNivelAtual() throws Exception {
		tabela.setNivelAtual(1);
		semantico.executeAction(120, token());

		assertEquals(0, tabela.getNivelAtual());
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
		contexto.setIdMetodoAtual(idMetodo);

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
		contexto.setIdMetodoAtual(idMetodo);
		contexto.setMpp(Mpp.VALOR);

		semantico.executeAction(123, token);

		IdParametro idDepois = (IdParametro)tabela.getIdentificador(token.getLexeme());
		assertEquals(Mpp.VALOR, idDepois.getMpp());
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
		contexto.setIdMetodoAtual(idMetodo);
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
		contexto.setIdMetodoAtual(idMetodo);

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
		assertEquals(Mpp.REFERENCIA, contexto.getMpp());
	}

	@Test
	public void action127DeveAlterarMppParaValor() throws Exception {
		semantico.executeAction(127, token());
		assertEquals(Mpp.VALOR, contexto.getMpp());
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
		return new Token(1, "lexeme", 0);
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
