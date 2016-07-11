package br.ufsc.ctc.ine.sin.ine5622.model;

import java.util.ArrayList;
import java.util.List;

public class IdMetodo extends Identificador {

	private List<IdParametro> parametros;

	public IdMetodo(String nome) {
		super(nome);
		this.parametros = new ArrayList<IdParametro>();
	}

	public void incluirParametro(IdParametro idParametro) {
		this.parametros.add(idParametro);
	}

	public List<IdParametro> getParametros() {
		return this.parametros;
	}

}
