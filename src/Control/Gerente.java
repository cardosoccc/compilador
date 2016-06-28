package Control;

import java.io.File;
import java.io.StringReader;

import Analisador.AnalysisError;
import Analisador.LexicalError;
import Analisador.Lexico;
import Analisador.Semantico;
import Analisador.Sintatico;
import Analisador.Token;
import Model.ManipuladorArquivo;

public class Gerente {

	private ManipuladorArquivo ma;
	private Lexico lexico;
	private Sintatico sintatico;
	private Semantico semantico;

	/**
	 * Construtor
	 */
	public Gerente() {
		ma = new ManipuladorArquivo();
		lexico = new Lexico();
		sintatico = new Sintatico();
	}

	/**
	 * Manda o manipulador de arquivos carregar o arquivo passado como parametro
	 *
	 * @param file
	 *            arquivo a ser carregado
	 */
	public void carregarArquivo(File file) {
		ma.carregarArquivo(file);
	}

	/**
	 * Manda salvar o conte�do no mesmo arquivo diret�rio que o arquivo atual,
	 * caso n�o houver arquivo atual, ele retorna false para que a interface
	 * pe�a um diret�rio
	 *
	 * @param conteudo
	 *            Conte�do a ser salvo no arquivo
	 *
	 * @return true caso conseguiu gravar, false caso n�o h� um arquivo atual
	 */
	public boolean salvarArquivo(String conteudo) {
		return ma.salvarArquivo(conteudo);
	}

	/**
	 * Salva o String em um arquivo espec�fico
	 *
	 * @param file
	 *            Endere�o do arquivo
	 *
	 * @param conteudo
	 *            String a ser gravado no arquivo
	 */
	public void salvarArquivoComo(File file, String conteudo) {
		ma.salvarArquivoComo(file, conteudo);
	}

	/**
	 * Retorna o conte�do do arquivo lido
	 *
	 * @return Conte�do do arquivo lido
	 */
	public String getConteudoArquivo() {
		return ma.getConteudoArquivo();
	}

	/**
	 * Chama o analisador l�xico
	 *
	 * @param entrada
	 *            texto do c�digo a ser analisado
	 *
	 * @param tela
	 *            fram que possui m�todo para envio de mensagem ao usu�rio
	 *
	 * @return posi�ao em que ocorreu um erro ou negativo caso n�o encontre erro
	 * @throws LexicalError
	 */
	public void analisadorLexico(String entrada) throws LexicalError {
		lexico.setInput(new StringReader(entrada));
		Token t = null;
		while ((t = lexico.nextToken()) != null) {
			System.out.println(t.toString());
		}
	}

	/**
	 * Chama o analisador l�xico e sint�tico
	 *
	 * @param entrada
	 *            texto do c�digo a ser analisado
	 *
	 * @throws AnalysisError
	 */
	public void analisadorSintatico(String entrada) throws AnalysisError {
		lexico.setInput(new StringReader(entrada));
		sintatico.parse(lexico, semantico);
	}

	/**
	 * Informa��es sobre o aplicativo
	 *
	 * @return Mensagem a ser apresentada na tela do Sobre
	 */
	public String informacoesAplicativo() {
		return "Compilador da linguagem LSI-161\n\n" + "Autores: \n\tVitor Augusto Schweitzer"
				+ "\n\tCaio Cargnin Cardoso";
	}

}
