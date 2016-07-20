package br.ufsc.ctc.ine.sin.ine5622.control;

import org.junit.Before;
import org.junit.Test;

import br.ufsc.ctc.ine.sin.ine5622.infra.ManipuladorArquivo;
import br.ufsc.ctc.ine.sin.ine5622.model.Lexico;
import br.ufsc.ctc.ine.sin.ine5622.model.SemanticError;
import br.ufsc.ctc.ine.sin.ine5622.model.Semantico;
import br.ufsc.ctc.ine.sin.ine5622.model.Sintatico;
import br.ufsc.ctc.ine.sin.ine5622.model.Tipo;
import br.ufsc.ctc.ine.sin.ine5622.util.ProgramaBuilder;

public class GerenteTest {

	private Sintatico sintatico;
	private Semantico semantico;
	private ManipuladorArquivo ma;
	private Gerente gerente;
	private Lexico lexico;

	@Before
	public void setUp() throws Exception {
		sintatico = new Sintatico();
		lexico = new Lexico();
		semantico = new Semantico();
		ma = new ManipuladorArquivo();
		gerente = new Gerente(ma, lexico, sintatico, semantico);
	}

	@Test
	public void programaVazio() throws Exception {
		String codigo = programa().blocoPrograma().build();
		gerente.analisadorSintatico(codigo);
	}

	@Test
	public void umaUnicaVariavel() throws Exception {
		String codigo = programa().declaracaoVariavel(Tipo.INTEIRO, "var1").blocoPrograma().build();
		gerente.analisadorSintatico(codigo);
	}

	@Test
	public void diversasVariaveisJuntas() throws Exception {
		String codigo = programa().declaracaoVariavel(Tipo.INTEIRO, "var1", "var2").blocoPrograma().build();
		gerente.analisadorSintatico(codigo);
	}

	@Test
	public void umaUnicaConstante() throws Exception {
		String codigo = programa().declaracaoConstante(Tipo.BOOLEANO, "verdadeiro", "var1").blocoPrograma().build();
		gerente.analisadorSintatico(codigo);
	}

	@Test
	public void diversasConstantesJuntas() throws Exception {
		String codigo = programa().declaracaoConstante(Tipo.INTEIRO, "1", "var1", "var2", "var3").blocoPrograma().build();
		gerente.analisadorSintatico(codigo);
	}

	@Test(expected=SemanticError.class)
	public void constanteVetor() throws Exception {
		String codigo = programa().declaracao("inteiro[2] var1 = 1;").blocoPrograma().build();
		gerente.analisadorSintatico(codigo);
	}

	@Test(expected=SemanticError.class)
	public void constanteCadeia() throws Exception {
		String codigo = programa().declaracao("cadeia[3] var1 = 'abc';").blocoPrograma().build();
		gerente.analisadorSintatico(codigo);
	}

	@Test(expected=SemanticError.class)
	public void vetorTipoCadeia() throws Exception {
		String codigo = programa().declaracao("cadeia[3][3] var1;").blocoPrograma().build();
		gerente.analisadorSintatico(codigo);
	}

	@Test(expected=SemanticError.class)
	public void cadeiaComDimensaoMaiorQue256() throws Exception {
		String codigo = programa().declaracaoCadeia("257", "var1").blocoPrograma().build();
		gerente.analisadorSintatico(codigo);
	}

	@Test(expected=SemanticError.class)
	public void vetorComDimensaoDiferenteDeInteiro() throws Exception {
		String codigo = programa().declaracaoVetor(Tipo.REAL, "falso", "var1").blocoPrograma().build();
		gerente.analisadorSintatico(codigo);
	}

	@Test(expected=SemanticError.class)
	public void cadeiaComDimensaoDiferenteDeInteiro() throws Exception {
		String codigo = programa().declaracaoCadeia("falso", "var1").blocoPrograma().build();
		gerente.analisadorSintatico(codigo);
	}

	@Test(expected=SemanticError.class)
	public void constanteComTipoIncorreto() throws Exception {
		String codigo = programa().declaracaoConstante(Tipo.BOOLEANO, "1", "var1").blocoPrograma().build();
		gerente.analisadorSintatico(codigo);
	}

	@Test(expected=SemanticError.class)
	public void variavelComMesmoIdDoPrograma() throws Exception {
		String codigo = programa("teste").declaracaoVariavel(Tipo.INTEIRO, "teste").blocoPrograma().build();
		gerente.analisadorSintatico(codigo);
	}

	@Test(expected=SemanticError.class)
	public void variavelComIdRepetido() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.declaracaoVariavel(Tipo.BOOLEANO, "a")
				.blocoPrograma().build());
	}

	@Test
	public void declaracaoMetodoSemRetorno() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoMetodo("func", null, Tipo.NULO)
				.blocoMetodo()
				.blocoPrograma()
				.build());
	}

	@Test
	public void declaracaoMetodoComRetorno() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoMetodo("func", null, Tipo.INTEIRO)
				.blocoMetodo("retorne 1;")
				.blocoPrograma()
				.build());
	}

	@Test
	public void declaracaoMetodoComParametroRefSemRetorno() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoMetodo("func", "ref a:inteiro" , Tipo.NULO)
				.blocoMetodo()
				.blocoPrograma()
				.build());
	}

	@Test
	public void declaracaoMetodoComParametroValSemRetorno() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoMetodo("func", "val a:inteiro" , Tipo.NULO)
				.blocoMetodo()
				.blocoPrograma()
				.build());
	}

	@Test
	public void declaracaoMetodoComParametroRefComRetorno() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoMetodo("func", "ref a:inteiro" , Tipo.REAL)
				.blocoMetodo("retorne 1.1;")
				.blocoPrograma()
				.build());
	}

	@Test
	public void declaracaoMetodoComParametroValComRetorno() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoMetodo("func", "val a:inteiro" , Tipo.BOOLEANO)
				.blocoMetodo("retorne falso;")
				.blocoPrograma()
				.build());
	}

	@Test
	public void declaracaoMetodoComParametroReFValComRetorno() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoMetodo("func", "val a:inteiro; ref b:booleano" , Tipo.BOOLEANO)
				.blocoMetodo("retorne verdadeiro;")
				.blocoPrograma()
				.build());
	}


	@Test(expected=SemanticError.class)
	public void identificadorNaoDeclarado() throws Exception {
		gerente.analisadorSintatico(programa()
				.blocoPrograma("a := 1;")
				.build());
	}

	@Test(expected=SemanticError.class)
	public void utilizacaoDeVariavelDeclaradaForaDoEscopo() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoMetodo("func", "val a:inteiro", Tipo.NULO)
				.blocoMetodo("a := 2;")
				.blocoPrograma("a := 1;")
				.build());
	}

	@Test(expected=SemanticError.class)
	public void utilizacaoDeVariavelDeclaradaNoEscopoDeOutroMetodo() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoMetodo("func1", "val a:inteiro", Tipo.NULO)
				.abreBloco().comando("a := 2;").fechaBlocoMetodo()
				.declaracaoMetodo("func2", "val b:inteiro", Tipo.NULO)
				.abreBloco().comando("a := 2;").fechaBlocoMetodo()
				.blocoPrograma()
				.build());
	}

	@Test
	public void utilizacaoDeVariavelDeclaradaNoEscopoExterno() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.declaracaoMetodo("func", null, Tipo.NULO)
				.abreBloco().comando("a := 2;").fechaBlocoMetodo()
				.abreBloco().comando("a := 1;").fechaBlocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void atribuicaoEmIdentificadorConstante() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoConstante(Tipo.INTEIRO, "1", "a")
				.abreBloco().comando("a := 2;").fechaBlocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void leituraEmIdentificadorConstante() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoConstante(Tipo.INTEIRO, "1", "a")
				.abreBloco().comando("leia(a);").fechaBlocoPrograma()
				.build());
	}

	@Test
	public void leituraEmIdentificadorVariavel() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.abreBloco().comando("leia(a);").fechaBlocoPrograma()
				.build());
	}

	@Test
	public void metodoSemTipoEmContextoDeComando() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoMetodo("f", null, Tipo.NULO)
				.blocoMetodo()
				.abreBloco().comando("f;").fechaBlocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void metodoSemTipoEmContextoDeExpressao() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.declaracaoMetodo("f", null, Tipo.NULO)
				.abreBloco().comando("a := f;").fechaBlocoPrograma()
				.build());
	}

	@Test
	public void metodoComTipoEmContextoDeExpressao() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.declaracaoMetodo("f", null, Tipo.INTEIRO)
				.abreBloco()
				.comando("retorne 1;")
				.fechaBlocoMetodo()
				.abreBloco().comando("a := f;").fechaBlocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void metodoComTipoEmContextoDeComando() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.declaracaoMetodo("f", null, Tipo.INTEIRO)
				.abreBloco()
				.comando("retorne 1;")
				.fechaBlocoMetodo()
				.abreBloco().comando("f;").fechaBlocoPrograma()
				.build());
	}

	@Test
	public void variavelVetorUsadaComIndiceExpressaoInteira() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVetor(Tipo.INTEIRO, "3", "a")
				.abreBloco().comando("a[1] := 1;").fechaBlocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void variavelVetorUsadaComIndiceExpressaoDiferenteDeInteiro() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVetor(Tipo.INTEIRO, "3", "a")
				.abreBloco().comando("a[1.0] := 1;").fechaBlocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void variavelVetorUsadaSemIndexacao() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVetor(Tipo.INTEIRO, "3", "a")
				.abreBloco().comando("a := 1;").fechaBlocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void variavelInteiraUsadaComIndexacao() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.abreBloco().comando("a[1] := 1;").fechaBlocoPrograma()
				.build());
	}

	@Test
	public void variavelCadeiaUsadaComIndexacao() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoCadeia("3", "a")
				.declaracaoCadeia("3", "b")
				.abreBloco()
				.comando("a := 'abc';")
				.comando("a[1] := 'd';")
				.comando("b := a;")
				.comando("b[2] := a[2];")
				.fechaBlocoPrograma()
				.build());
	}

	@Test
	public void parametroUsadoComoVariavel() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoMetodo("f", "val a:inteiro; val b:real", Tipo.NULO)
				.abreBloco()
				.comando("a := 1;")
				.comando("b := 2.2 + a;")
				.fechaBlocoMetodo()
				.abreBloco()
				.comando("f(1, 2);")
				.fechaBlocoPrograma()
				.build());
	}

	@Test
	public void metodoComTipoPrefinido() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoMetodo("f", null, Tipo.INTEIRO)
				.abreBloco()
				.comando("retorne 1;")
				.fechaBlocoMetodo()
				.blocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void metodoComTipoCadeia() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracao("metodo f : cadeia[3];")
				.abreBloco().comando("retorne 'abc';").fechaBlocoMetodo()
				.blocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void metodoComParametroCadeia() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoMetodo("f", "val a : cadeia[3]", Tipo.NULO)
				.blocoMetodo()
				.blocoPrograma("retorne a;")
				.build());
	}

	@Test
	public void atribuicaoDeCaracterEmCadeia() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.CARACTER, "a")
				.declaracaoCadeia("1", "b")
				.blocoPrograma("b := a;")
				.build());
	}

	@Test
	public void atribuicaoCadeiaIndexadaEmCaracter() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.CARACTER, "a")
				.declaracaoCadeia("3", "b")
				.abreBloco()
				.comando("b := 'abc';")
				.comando("a := b[1];")
				.fechaBlocoPrograma()
				.build());
	}

	@Test
	public void atribuicaoLiteralComUmCaracterEmCaracter() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.CARACTER, "a")
				.abreBloco()
				.comando("a := 'x';")
				.fechaBlocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void atribuicaoLiteralComMaisDeUmCaracterEmCaracter() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.CARACTER, "a")
				.abreBloco()
				.comando("a := 'abc';")
				.fechaBlocoPrograma()
				.build());
	}

	@Test
	public void expressaoDeControleNoComandoSeTipoBooleano() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.abreBloco()
				.comando("se verdadeiro entao a := 1;")
				.fechaBlocoPrograma()
				.build(true));
	}

	@Test
	public void expressaoDeControleNoComandoSeTipoInteiro() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.abreBloco()
				.comando("se a entao a := 1 senao a := 2;")
				.fechaBlocoPrograma()
				.build(true));
	}

	@Test(expected=SemanticError.class)
	public void expressaoDeControleNoComandoSeTipoInvalido() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.CARACTER, "a")
				.abreBloco()
				.comando("se 3.3 entao a := '1';")
				.fechaBlocoPrograma()
				.build(true));
	}

	@Test
	public void expressaoDeControleNoComandoEnquantoTipoBooleano() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoConstante(Tipo.BOOLEANO, "verdadeiro", "a")
				.abreBloco()
				.comando("enquanto a faca ;")
				.fechaBlocoPrograma()
				.build(true));
	}

	@Test
	public void expressaoDeControleNoComandoEnquantoTipoInteiro() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoConstante(Tipo.INTEIRO, "0", "a")
				.abreBloco()
				.comando("enquanto a faca ;;")
				.fechaBlocoPrograma()
				.build(true));
	}

	@Test(expected=SemanticError.class)
	public void expressaoDeControleNoComandoEnquantoTipoInvalido() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.CARACTER, "a")
				.abreBloco()
				.comando("enquanto 'b' faca a := '1';")
				.fechaBlocoPrograma()
				.build(true));
	}

	@Test
	public void leiaVariavelTipoValido() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.CARACTER, "a")
				.abreBloco()
				.comando("leia(a);")
				.fechaBlocoPrograma()
				.build(true));
	}

	@Test(expected=SemanticError.class)
	public void leiaConstante() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoConstante(Tipo.INTEIRO, "1", "a")
				.abreBloco()
				.comando("leia(a);")
				.fechaBlocoPrograma()
				.build(true));
	}

	@Test(expected=SemanticError.class)
	public void leiaMetodo() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.declaracaoMetodo("f", null, Tipo.INTEIRO)
				.blocoMetodo("retorne 1;")
				.abreBloco()
				.comando("leia(a, f);")
				.fechaBlocoPrograma()
				.build(true));
	}

	@Test(expected=SemanticError.class)
	public void leiaVariavelTipoInvalido() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.BOOLEANO, "a")
				.abreBloco()
				.comando("leia(a);")
				.fechaBlocoPrograma()
				.build());
	}

	@Test
	public void leiaVariavelCadeia() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoCadeia("1", "a")
				.abreBloco()
				.comando("leia(a);")
				.fechaBlocoPrograma()
				.build());
	}


	@Test
	public void escrevaVariavelTipoValido() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.CARACTER, "a")
				.abreBloco()
				.comando("escreva(a);")
				.fechaBlocoPrograma()
				.build(true));
	}

	@Test(expected=SemanticError.class)
	public void escrevaVariavelTipoInvalido() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.BOOLEANO, "a")
				.abreBloco()
				.comando("escreva(a);")
				.fechaBlocoPrograma()
				.build());
	}

	@Test
	public void escrevaVariavelCadeia() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoCadeia("1", "a")
				.abreBloco()
				.comando("escreva(a);")
				.fechaBlocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void numeroDeParametrosIncorreto() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoMetodo("f", "val a:inteiro", Tipo.NULO)
				.blocoMetodo()
				.abreBloco()
				.comando("f(1, 2);")
				.fechaBlocoPrograma()
				.build());
	}

	@Test
	public void numeroDeParametrosCorreto() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoMetodo("f", "val a:inteiro", Tipo.NULO)
				.blocoMetodo()
				.abreBloco()
				.comando("f(1);")
				.fechaBlocoPrograma()
				.build());
	}

	@Test
	public void parametroReferenciaRecebendoVariavel() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.declaracaoMetodo("f", "ref b:inteiro", Tipo.NULO)
				.blocoMetodo()
				.abreBloco()
				.comando("f(a);")
				.fechaBlocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void parametroReferenciaRecebendoValor() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.declaracaoMetodo("f", "ref b:inteiro", Tipo.NULO)
				.blocoMetodo()
				.abreBloco()
				.comando("f(1);")
				.fechaBlocoPrograma()
				.build());
	}

	@Test
	public void parametroValorRecebendoVariavel() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.declaracaoMetodo("f", "val b:inteiro", Tipo.NULO)
				.blocoMetodo()
				.abreBloco()
				.comando("f(a);")
				.fechaBlocoPrograma()
				.build());
	}

	@Test
	public void parametroValorRecebendoValor() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoMetodo("f", "val a:inteiro", Tipo.NULO)
				.blocoMetodo()
				.abreBloco()
				.comando("f(1);")
				.fechaBlocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void declaracaoMetodoSemTipoComRetorno() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoMetodo("func", null, Tipo.NULO)
				.blocoMetodo("retorne 1;")
				.blocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void declaracaoMetodoComTipoPeloSemInstrucao() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoMetodo("func", null, Tipo.INTEIRO)
				.blocoMetodo()
				.blocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void metodoComRetornoIncompativel() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoMetodo("func", null, Tipo.INTEIRO)
				.blocoMetodo("retorne falso;")
				.blocoPrograma()
				.build());
	}

	@Test
	public void metodoComRetornoCompativel() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoMetodo("func", null, Tipo.BOOLEANO)
				.blocoMetodo("retorne falso;")
				.blocoPrograma()
				.build());
	}

	@Test
	public void ladoEsquerdoDaAtribuicaoVariavel() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.blocoPrograma("a := 1;")
				.build());
	}

	@Test(expected=SemanticError.class)
	public void ladoEsquerdoDaAtribuicaoConstante() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoConstante(Tipo.INTEIRO, "1", "a")
				.blocoPrograma("a := 1;")
				.build());
	}

	@Test
	public void ladoEsquerdoDaAtribuicaoParametro() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoMetodo("func", "val b:inteiro", Tipo.NULO)
				.blocoMetodo("b := 1;")
				.blocoPrograma()
				.build());
	}

	@Test
	public void tipoExpressaoCompativelComLadoEsquerdoDaAtribuicao() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.blocoPrograma("a := 1;")
				.build());
	}

	@Test(expected=SemanticError.class)
	public void tipoExpressaoIncompativelComLadoEsquerdoDaAtribuicao() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.blocoPrograma("a := 'x';")
				.build());
	}

	@Test
	public void realAceitaInteiro() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.REAL, "a")
				.blocoPrograma("a := 1;")
				.build());
	}

	@Test(expected=SemanticError.class)
	public void inteiroNaoAceitaInteiro() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.blocoPrograma("a := 1.1;")
				.build());
	}

	@Test
	public void cadeialAceitaCaracter() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoCadeia("3", "a")
				.blocoPrograma("a := 'x';")
				.build());
	}

	@Test(expected=SemanticError.class)
	public void caracterNaoAceitaCadeia() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.CARACTER, "a")
				.blocoPrograma("a := 'abc';")
				.build());
	}

	@Test(expected=SemanticError.class)
	public void operandosIncompativeisOperadorADD() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.blocoPrograma("a :=  'a' + 1;")
				.build());
	}

	@Test(expected=SemanticError.class)
	public void operandosIncompativeisOperadorOU() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.BOOLEANO, "a")
				.blocoPrograma("a :=  verdadeiro ou 0;")
				.build());
	}

	@Test
	public void operandosCompativeisInteiroRealOperadoresAdicaoSubtracao() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.REAL, "a")
				.abreBloco()
				.comando("a :=  2.2 + 1;")
				.comando("a :=  1 + 3.3;")
				.comando("a :=  1 - 3.3;")
				.comando("a :=  2.2 - 0;")
				.fechaBlocoPrograma()
				.build());
	}


	@Test
	public void operandosCompativeisBooleanosOperadoresLogicos() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.BOOLEANO, "a")
				.abreBloco()
				.comando("a :=  falso e verdadeiro;")
				.comando("a :=  verdadeiro ou falso;")
				.fechaBlocoPrograma()
				.build(true));
	}

	@Test
	public void operandosCompativeisCadeiaCaracterOperadoresRelacionais() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.BOOLEANO, "a")
				.abreBloco()
				.comando("a :=  'a' > 'b';")
				.comando("a :=  'abc' < 'b';")
				.comando("a :=  'abc' = 'bca';")
				.comando("a :=  'abc' <> 'bca';")
				.fechaBlocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void operandosIncompativeisOperadorDIV() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.REAL, "a")
				.abreBloco()
				.comando("a :=  1.0 div 1.0;")
				.fechaBlocoPrograma()
				.build());
	}

	@Test
	public void operandosCompativeisOperadorDIV() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.abreBloco()
				.comando("a := 2 div 1;")
				.fechaBlocoPrograma()
				.build());
	}

	@Test
	public void operandosCompativeisOperadorFRC() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.REAL, "a")
				.abreBloco()
				.comando("a := 2 / 1;")
				.fechaBlocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void operadorFRCResultaReal() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.abreBloco()
				.comando("a := 2 / 1;")
				.fechaBlocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void operadorUnarioRepetido() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.abreBloco()
				.comando("a := --1;")
				.fechaBlocoPrograma()
				.build());
	}

	@Test
	public void operadorUnario() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.REAL, "a")
				.abreBloco()
				.comando("a := -1;")
				.fechaBlocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void operadorNegacaoRepetido() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.BOOLEANO, "a")
				.abreBloco()
				.comando("a := nao nao verdadeiro;")
				.fechaBlocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void operadorNegacaoComOperandoInvalido() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.BOOLEANO, "a")
				.declaracaoVariavel(Tipo.INTEIRO, "b")
				.abreBloco()
				.comando("a := nao b;")
				.fechaBlocoPrograma()
				.build());
	}

	@Test
	public void operadorNegacao() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.BOOLEANO, "a")
				.abreBloco()
				.comando("a := nao falso;")
				.comando("a := nao verdadeiro;")
				.fechaBlocoPrograma()
				.build());
	}

	@Test
	public void falsoMenorQueVerdadeiro() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoConstante(Tipo.BOOLEANO, "verdadeiro", "a")
				.declaracaoConstante(Tipo.BOOLEANO, "falso", "b")
				.declaracaoVariavel(Tipo.BOOLEANO, "c")
				.abreBloco()
				.comando("c := a < b;")
				.fechaBlocoPrograma()
				.build());
	}

	@Test
	public void atribuicaoValidaDeResultadoDeMetodo() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.BOOLEANO, "c")
				.declaracaoMetodo("f", null, Tipo.BOOLEANO)
				.blocoMetodo("retorne verdadeiro;")
				.abreBloco()
				.comando("c := f;")
				.fechaBlocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void atribuicaoInvalidaDeResultadoDeMetodo() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "c")
				.declaracaoMetodo("f", null, Tipo.BOOLEANO)
				.blocoMetodo("retorne verdadeiro;")
				.abreBloco()
				.comando("c := f;")
				.fechaBlocoPrograma()
				.build());
	}

	@Test
	public void atribuicaoValidaDeExpressaoAritmeticaAninhadaResultadoInteiro() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.abreBloco()
				.comando("a := 1 + (2 - 3) * 2 - (2 + 2);")
				.comando("a := 1 + ( (2 * 5) + 2 - (1 + (2 + 2) ) );")
				.fechaBlocoPrograma()
				.build());
	}

	@Test
	public void atribuicaoValidaDeExpressaoAritmeticaAninhadaResultadoReal() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.REAL, "a")
				.abreBloco()
				.comando("a := 1 + ( (2 * 5) + 2 - (1 + (2 / 2) ) );")
				.fechaBlocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void atribuicaoInvalidaDeExpressaoAritmeticaAninhadaComMetodoResultadoReal() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.declaracaoMetodo("f", null, Tipo.REAL)
				.blocoMetodo("retorne 1.0;")
				.abreBloco()
				.comando("a := 1 + ( (2 * 5 + f) + 2 - (1 + (2 + 2) ) );")
				.fechaBlocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void atribuicaoInvalidaDeExpressaoAritmeticaAninhadaResultadoReal() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.abreBloco()
				.comando("a := 1 + ( (2 * 5) + 2 - (1 + (2 / 2) ) );")
				.fechaBlocoPrograma()
				.build());
	}

	@Test
	public void atribuicaoValidaDeExpressaoLogicaAninhadaComMetodoResultadoBooleano() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.BOOLEANO, "a")
				.declaracaoMetodo("f", null, Tipo.BOOLEANO)
				.blocoMetodo("retorne verdadeiro;")
				.abreBloco()
				.comando("a := 1 + (2 * 5) > 2 - (1 - (-2 + 2) ) ;")
				.comando("a := verdadeiro e a;")
				.comando("a := a ou verdadeiro e (a <> a);")
				.comando("a := f e f e (f e f ou verdadeiro e (f ou falso)) ou f;")
				.comando("a := 'a' < 'abc';")
				.comando("a := falso < verdadeiro;")
				.fechaBlocoPrograma()
				.build());
	}

	@Test
	public void chamadaDeMetodoAninhada() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.declaracaoMetodo("f", "val b:inteiro", Tipo.INTEIRO)
				.blocoMetodo("retorne b div 2;")
				.declaracaoMetodo("g", "val b:inteiro", Tipo.INTEIRO)
				.blocoMetodo("retorne 4;")
				.abreBloco()
				.comando("a := f(g(2));")
				.fechaBlocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void chamadaDeMetodoAninhadaComAtribuicaoInvalida() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.declaracaoMetodo("f", "val b:inteiro", Tipo.INTEIRO)
				.blocoMetodo("retorne b div 2;")
				.declaracaoMetodo("g", "val b:inteiro", Tipo.REAL)
				.blocoMetodo("retorne b / 2;")
				.abreBloco()
				.comando("a := g(f(2));")
				.fechaBlocoPrograma()
				.build());
	}

	@Test
	public void chamadaDeMetodoComExpressaoAninhadaComoParametro() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.REAL, "a")
				.declaracaoMetodo("f", "val b:inteiro", Tipo.INTEIRO)
				.blocoMetodo("retorne b + 2;")
				.declaracaoMetodo("g", "val b:real", Tipo.REAL)
				.blocoMetodo("retorne b / 2;")
				.abreBloco()
				.comando("a := g(f(2 + (3 div 1)) + 1.0);")
				.fechaBlocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void chamadaDeMetodoComExpressaoAninhadaComoParametroTipoInvalido() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.REAL, "a")
				.declaracaoMetodo("f", "val b:inteiro", Tipo.INTEIRO)
				.blocoMetodo("retorne b + 2;")
				.declaracaoMetodo("g", "val b:inteiro", Tipo.REAL)
				.blocoMetodo("retorne b / 2;")
				.abreBloco()
				.comando("a := f(g(2 + (3 div 1)) + 1);")
				.fechaBlocoPrograma()
				.build());
	}

	@Test
	public void declaracaoDeMetodoAninhadaComUtilizacaoDoEscopoExterno() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.REAL, "a")
				.declaracaoMetodo("f", "val b:inteiro", Tipo.REAL)
				.declaracaoVariavel(Tipo.INTEIRO, "d")
				.declaracaoMetodo("g", "ref c:real", Tipo.REAL)
				.blocoMetodo("retorne a + b + c;")
				.abreBloco()
				.comando("retorne b + g(b) + 2;")
				.fechaBlocoMetodo()
				.abreBloco()
				.comando("a := f(1);")
				.fechaBlocoPrograma()
				.build(true));
	}

	@Test(expected=SemanticError.class)
	public void declaracaoDeMetodoAninhadaComReferenciaAVariavelDoEscopoInterno() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.REAL, "a")
				.declaracaoMetodo("f", "val b:inteiro", Tipo.REAL)
				.declaracaoVariavel(Tipo.INTEIRO, "d")
				.declaracaoMetodo("g", "ref c:real", Tipo.REAL)
				.blocoMetodo("retorne a + b + c;")
				.abreBloco()
				.comando("retorne b + g(b) + c;")
				.fechaBlocoMetodo()
				.abreBloco()
				.comando("a := f(1);")
				.fechaBlocoPrograma()
				.build());
	}

	@Test
	public void atribuicaoDeExpressaoLogica() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.BOOLEANO, "t")
				.declaracaoConstante(Tipo.BOOLEANO, "falso", "u")
				.declaracaoConstante(Tipo.BOOLEANO, "verdadeiro", "v")
				.declaracaoConstante(Tipo.INTEIRO, "c")
				.abreBloco()
				.comando("c := 1;")
				.comando("t := nao (u e verdadeiro);")
				.fechaBlocoPrograma()
				.build());
	}

	@Test(expected=SemanticError.class)
	public void expressaoSendoPassadaComoParametroReferenciaIdDepois() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.declaracaoMetodo("f", "ref x:inteiro", Tipo.NULO)
				.blocoMetodo()
				.abreBloco()
				.comando("f(1+a)")
				.fechaBlocoPrograma()
				.build(true));
	}

	@Test(expected=SemanticError.class)
	public void expressaoSendoPassadaComoParametroReferenciaSomaDuasVariaveis() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.declaracaoMetodo("f", "ref x:inteiro", Tipo.NULO)
				.blocoMetodo()
				.abreBloco()
				.comando("f(a+a)")
				.fechaBlocoPrograma()
				.build(true));
	}

	@Test(expected=SemanticError.class)
	public void expressaoSendoPassadaComoParametroReferenciaMutiplicaDuasVariaveis() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.declaracaoMetodo("f", "ref x:inteiro", Tipo.NULO)
				.blocoMetodo()
				.abreBloco()
				.comando("f(a*a)")
				.fechaBlocoPrograma()
				.build(true));
	}

	@Test(expected=SemanticError.class)
	public void expressaoSendoPassadaComoParametroReferenciaComparaDuasVariaveis() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.BOOLEANO, "a")
				.declaracaoMetodo("f", "ref x:booleano", Tipo.NULO)
				.blocoMetodo()
				.abreBloco()
				.comando("f(a=a)")
				.fechaBlocoPrograma()
				.build(true));
	}

	@Test(expected=SemanticError.class)
	public void expressaoSendoPassadaComoParametroReferenciaIdAntes() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.declaracaoMetodo("f", "ref x:inteiro", Tipo.NULO)
				.blocoMetodo()
				.abreBloco()
				.comando("f(a+1)")
				.fechaBlocoPrograma()
				.build(true));
	}

	@Test(expected=SemanticError.class)
	public void expressaoSendoPassadaComoSegundoParametro() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.declaracaoMetodo("f", "ref x:inteiro; ref y:inteiro", Tipo.NULO)
				.blocoMetodo()
				.abreBloco()
				.comando("f(a, 1+a)")
				.fechaBlocoPrograma()
				.build(true));
	}

	@Test(expected=SemanticError.class)
	public void expressaoSendoPassadaComoTerceiroParametro() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.declaracaoMetodo("f", "ref x:inteiro; val y:inteiro; ref z:inteiro", Tipo.NULO)
				.blocoMetodo()
				.abreBloco()
				.comando("f(a, a, -a)")
				.fechaBlocoPrograma()
				.build(true));
	}

	@Test
	public void variavelSendoPassadaComoPrimeiroETerceiroParametro() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.declaracaoMetodo("f", "ref x:inteiro; val y:inteiro; ref z:inteiro", Tipo.NULO)
				.blocoMetodo()
				.abreBloco()
				.comando("f(a, a+1, a)")
				.fechaBlocoPrograma()
				.build(true));
	}

	@Test
	public void variavelSendoPassadaComoSegundoParametro() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.INTEIRO, "a")
				.declaracaoMetodo("f", "val x:inteiro; ref y:inteiro; val z:inteiro", Tipo.NULO)
				.blocoMetodo()
				.abreBloco()
				.comando("f(a+1, a, (a))")
				.fechaBlocoPrograma()
				.build(true));
	}

	@Test
	public void declaracaoDeMetodoAninhadaComVariavelDeclaradaNovamente() throws Exception {
		gerente.analisadorSintatico(programa()
				.declaracaoVariavel(Tipo.REAL, "a")
				.declaracaoMetodo("f", "val b:inteiro", Tipo.REAL)
				.declaracaoVariavel(Tipo.REAL, "a")
				.declaracaoMetodo("g", "ref c:real", Tipo.REAL)
				.blocoMetodo("retorne 1.1")
				.abreBloco()
				.comando("retorne 1.0")
				.fechaBlocoMetodo()
				.abreBloco()
				.comando("a := 1.0;")
				.fechaBlocoPrograma()
				.build(true));
	}

	private ProgramaBuilder programa() {
		return programa("teste");
	}

	private ProgramaBuilder programa(String id) {
		return new ProgramaBuilder().programa(id);
	}

}
