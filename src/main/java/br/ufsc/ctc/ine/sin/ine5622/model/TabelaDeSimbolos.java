/**
 * @author Vitor Schweitzer e Caio Cardoso
 * created on 2016/06/18
 */
package br.ufsc.ctc.ine.sin.ine5622.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsável por armazenar os identificadores e suas informações durante o processo de compilação
 *
 * A estrutura que armazena os identificadores é um HashMap, em que as chaves são os níveis,
 * e os valores são outros HashMap's em que a chave é o lexeme do token que representa o id,
 * e os valores são os identificadores propriamente ditos.
 */
public class TabelaDeSimbolos {

	private int nivelAtual;

	/** A tabela de símbolos propriamente dita */
	private Map<Integer, Map<String, Identificador>> identificadoresPorNivel;

	/** O deslocamento atual em cada nível */
	private Map<Integer, Integer> deslocamentoPorNivel;

	public TabelaDeSimbolos() {
		this.nivelAtual = 0;
		this.deslocamentoPorNivel = new HashMap<Integer, Integer>();
		this.identificadoresPorNivel = new HashMap<Integer, Map<String, Identificador>>();
	}

	/**
	 * Inclui um identificador na tabela de símbolos, no nível atual
	 *
	 * @param id o identificador
	 */
	public void incluirIdentificador(Identificador id) {
		id.setNivel(this.nivelAtual);

		if (this.identificadoresPorNivel.containsKey(nivelAtual)) {
			this.identificadoresPorNivel.get(this.nivelAtual).put(id.getNome(), id);
		} else {
			HashMap<String, Identificador> tabelaNivel = new HashMap<String, Identificador>();
			tabelaNivel.put(id.getNome(), id);
			this.identificadoresPorNivel.put(this.nivelAtual, tabelaNivel);
			this.deslocamentoPorNivel.put(this.nivelAtual, 0);
		}

		int deslocamento = getDeslocamento();
		id.setDeslocamento(deslocamento);
		setDeslocamento(deslocamento + id.getTamanho());
	}

	public int getDeslocamento() {
		if (!identificadoresPorNivel.containsKey(nivelAtual)) {
			this.deslocamentoPorNivel.put(this.nivelAtual, 0);
		}
		return this.deslocamentoPorNivel.get(this.nivelAtual);
	}

	public void setDeslocamento(int deslocamento) {
		this.deslocamentoPorNivel.put(this.nivelAtual, deslocamento);
	}

	public void setNivelAtual(int nivelAtual) {
		this.nivelAtual = nivelAtual;
	}

	public int getNivelAtual() {
		return nivelAtual;
	}

	/**
	 * Retorna um identificador, procurando em todos os níveis anteriores ao atual, incluindo o atual
	 *
	 * @param idName o do token lexeme que representa o identificador
	 * @return o identificador
	 */
	public Identificador getIdentificador(String idName) {
		for (int i = this.nivelAtual; i >= 0; i--) {
			Identificador id = getIdentificador(idName, i);
			if (id != null) {
				return id;
			}
		}
		return null;
	}

	/**
	 * Retorna um identificador em um determinado nível
	 *
	 * @param idName o lexeme do token que representa o identificador
	 * @param nivel o nível desejado
	 * @return o identificador
	 */
	public Identificador getIdentificador(String idName, int nivel) {
		Identificador id = null;
		if (identificadoresPorNivel.containsKey(nivel)) {
			id = identificadoresPorNivel.get(nivel).get(idName);
		}
		return id;
	}

	/**
	 * Retorna a estrutura que armazena os identificadores
	 * @return a estrutura que armazena os identificadores
	 */
	public Map<Integer, Map<String, Identificador>> getIdentificadoresPorNivel() {
		return identificadoresPorNivel;
	}

	/**
	 * Remove todos os símbolos e o limpa o deslocamento do nível atual
	 */
	public void limparNivelAtual() {
		this.identificadoresPorNivel.remove(this.nivelAtual);
		this.deslocamentoPorNivel.remove(this.nivelAtual);
	}

}
