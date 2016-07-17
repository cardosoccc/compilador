/**
 * @author Vitor Schweitzer e Caio Cardoso
 * created on 2016/06/18
 */
package br.ufsc.ctc.ine.sin.ine5622.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represente um identificador da categoria método
 */
public class IdMetodo extends Identificador {

	private List<IdParametro> parametros;

	public IdMetodo(String nome) {
		super(nome);
		this.parametros = new ArrayList<IdParametro>();
		this.setTamanho(1);
	}

	public void incluirParametro(IdParametro idParametro) {
		this.parametros.add(idParametro);
	}

	public List<IdParametro> getParametros() {
		return this.parametros;
	}

	public int getNumParametrosFormais() {
		return this.parametros.size();
	}

}
