package br.ufsc.ctc.ine.sin.ine5622.infra;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ManipuladorArquivo {

	private File file;

	/**
	 * Construtor
	 */
	public ManipuladorArquivo() {
		file = null;
	}

	/**
	 * Seta o atributo arquivo com o arquivo passado pelo carregamento
	 */
	public void carregarArquivo(File file) {
		this.setFile(file);
		System.out.println("Arquivo carregado: " + file.getPath());
	}

	/**
	 * Extrai do arquivo o seu texto
	 */
	public String getConteudoArquivo() {
		BufferedReader input = null;
		String conteudoArquivo = "";
		String linha;

		try {
			input = new BufferedReader(new FileReader(this.getFile()));

			linha = input.readLine();
			while (linha != null) {
				conteudoArquivo += linha;
				linha = input.readLine();
				if (linha != null && !linha.equals(""))
					conteudoArquivo += "\n";

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return conteudoArquivo;
	}

	/**
	 * Salva arquivo já existente
	 */
	public boolean salvarArquivo(String conteudo) {
		System.out.println(conteudo);
		if (!(this.getFile() == null)) {
			salvarArquivoComo(this.getFile(), conteudo);
			return true; // trocar depois por exceção
		} else {
			return false;
		}
	}

	/**
	 * Salva novo arquivo
	 */
	public void salvarArquivoComo(File file, String conteudo) {
		BufferedWriter output = null;
		conteudo = ajusteQuebraDeLinha(conteudo);
		try {
			this.setFile(file);
			output = new BufferedWriter(new FileWriter(file));
			output.write(conteudo);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Substitui o \n do textArea pela quebra de linha aceita pelo SO. Windows é
	 * \r\n
	 */
	public String ajusteQuebraDeLinha(String entrada) {
		return entrada.replaceAll("\n", System.lineSeparator());
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
