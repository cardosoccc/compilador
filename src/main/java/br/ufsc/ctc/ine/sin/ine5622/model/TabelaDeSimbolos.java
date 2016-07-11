package br.ufsc.ctc.ine.sin.ine5622.model;

import java.util.HashMap;
import java.util.Map;

public class TabelaDeSimbolos {

	private int deslocamento;
	private int nivelAtual;

	private Map<Integer, Map<String, Identificador>> identificadoresPorNivel;

	public TabelaDeSimbolos() {
		this.nivelAtual = 0;
		this.deslocamento = 0;
		this.identificadoresPorNivel = new HashMap<Integer, Map<String, Identificador>>();
	}


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


	public Identificador getIdentificador(String idName) {
		return getIdentificador(idName, this.nivelAtual);
	}

	public Identificador getIdentificador(String idName, int nivel) {
		Identificador id = null;
		if (identificadoresPorNivel.containsKey(nivel)) {
			id = identificadoresPorNivel.get(nivel).get(idName);
		}
		return id;
	}

	public Map<Integer, Map<String, Identificador>> getIdentificadoresPorNivel() {
		return identificadoresPorNivel;
	}

	public void limparNivelAtual() {
		this.identificadoresPorNivel.remove(this.nivelAtual);
	}

}
