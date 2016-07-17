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

	private int deslocamento;
	private int nivelAtual;

	/** A tabela de símbolos propriamente dita */
	private Map<Integer, Map<String, Identificador>> identificadoresPorNivel;

	public TabelaDeSimbolos() {
		this.nivelAtual = 0;
		this.deslocamento = 0;
		this.identificadoresPorNivel = new HashMap<Integer, Map<String, Identificador>>();
	}

	/**
	 * Inclui um identificador na tabela de símbolos, no nível atual
	 *
	 * @param id o identificador
	 */
	public void incluirIdentificador(Identificador id) {
		id.setNivel(this.nivelAtual);
		id.setDeslocamento(this.deslocamento);
		if (identificadoresPorNivel.containsKey(nivelAtual)) {
			identificadoresPorNivel.get(this.nivelAtual).put(id.getNome(), id);
		} else {
			HashMap<String, Identificador> tabelaNivel = new HashMap<String, Identificador>();
			tabelaNivel.put(id.getNome(), id);
			identificadoresPorNivel.put(this.nivelAtual, tabelaNivel);
		}
		this.deslocamento += id.getTamanho();
	}


	public void setDeslocamento(int deslocamento) {
		this.deslocamento = deslocamento;
	}

	public void setNivelAtual(int nivelAtual) {
		this.nivelAtual = nivelAtual;
	}

	public int getDeslocamento() {
		return this.deslocamento;
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
	 * Retorna um idetificador em um determinado nível
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
	 * Remove todos os símbolos do nível atual
	 */
	public void limparNivelAtual() {
		this.identificadoresPorNivel.remove(this.nivelAtual);
	}

}
