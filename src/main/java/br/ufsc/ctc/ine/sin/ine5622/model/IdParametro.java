/**
 * @author Vitor Schweitzer e Caio Cardoso
 * created on 2016/06/18
 */
package br.ufsc.ctc.ine.sin.ine5622.model;

/**
 * Representa um identificador do tipo parâmetro
 */
public class IdParametro extends Identificador {

	private MetodoPassagem mpp;
	private SubCategoria subCategoria;

	public IdParametro(String nome) {
		super(nome);
		this.setTamanho(1);
	}

	/**
	 * Permite inicializar o identificador com um identificador genérico pré-existente
	 *
	 * @param id o identificador pré-existente
	 */
	public IdParametro(Identificador id) {
		super(id.getNome());
		this.setDeslocamento(id.getDeslocamento());
		this.setNivel(id.getNivel());
		this.setTamanho(1);
	}

	public MetodoPassagem getMpp() {
		return mpp;
	}

	public void setMpp(MetodoPassagem mpp) {
		this.mpp = mpp;
	}

	public SubCategoria getSubCategoria() {
		return subCategoria;
	}

	public void setSubCategoria(SubCategoria subCategoria) {
		this.subCategoria = subCategoria;
	}

}
