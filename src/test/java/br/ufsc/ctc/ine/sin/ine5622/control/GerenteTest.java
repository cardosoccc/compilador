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
		String codigo = programa().bloco().build();
		gerente.analisadorSintatico(codigo);
	}

	@Test
	public void umaUnicaVariavel() throws Exception {
		String codigo = programa().declaracaoVariavel(Tipo.INTEIRO, "var1").bloco().build();
		gerente.analisadorSintatico(codigo);
	}

	@Test
	public void diversasVariaveisJuntas() throws Exception {
		String codigo = programa().declaracaoVariavel(Tipo.INTEIRO, "var1", "var2").bloco().build();
		gerente.analisadorSintatico(codigo);
	}

	@Test
	public void umaUnicaConstante() throws Exception {
		String codigo = programa().declaracaoConstante(Tipo.BOOLEANO, "verdadeiro", "var1").bloco().build();
		gerente.analisadorSintatico(codigo);
	}

	@Test
	public void programaComDiversasConstantesJuntas() throws Exception {
		String codigo = programa().declaracaoConstante(Tipo.INTEIRO, "1", "var1", "var2", "var3").bloco().build();
		gerente.analisadorSintatico(codigo);
	}

	@Test(expected=SemanticError.class)
	public void constanteVetor() throws Exception {
		String codigo = programa().declaracao("inteiro[2] var1 = 1;").bloco().build();
		gerente.analisadorSintatico(codigo);
	}

	@Test(expected=SemanticError.class)
	public void constanteCadeia() throws Exception {
		String codigo = programa().declaracao("cadeia[3] var1 = 'abc';").bloco().build();
		gerente.analisadorSintatico(codigo);
	}

	@Test(expected=SemanticError.class)
	public void vetorTipoCadeia() throws Exception {
		String codigo = programa().declaracao("cadeia[3][3] var1;").bloco().build();
		gerente.analisadorSintatico(codigo);
	}

	@Test(expected=SemanticError.class)
	public void cadeiaComDimensaoMaiorQue256() throws Exception {
		String codigo = programa().declaracaoCadeia(257, "var1").bloco().build();
		gerente.analisadorSintatico(codigo);
	}

	@Test(expected=SemanticError.class)
	public void vetorComDimensaoDiferenteDeInteiro() throws Exception {
		String codigo = programa().declaracao("booleano[falso] var1;").bloco().build();
		gerente.analisadorSintatico(codigo);
	}

	@Test(expected=SemanticError.class)
	public void constanteComTipoIncorreto() throws Exception {
		String codigo = programa().declaracaoConstante(Tipo.BOOLEANO, "1", "var1").bloco().build();
		gerente.analisadorSintatico(codigo);
	}

	private ProgramaBuilder programa() {
		return new ProgramaBuilder().programa("teste");
	}

}
