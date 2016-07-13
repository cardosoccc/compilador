package br.ufsc.ctc.ine.sin.ine5622.model;

public class IdParametro extends Identificador implements IdComSubCategoria {

	private Mpp mpp;
	private SubCategoria subCategoria;

	public IdParametro(String nome) {
		super(nome);
	}

	public IdParametro(Identificador id) {
		super(id.getNome());
		this.setDeslocamento(id.getDeslocamento());
		this.setNivel(id.getNivel());
	}

	public Mpp getMpp() {
		return mpp;
	}

	public void setMpp(Mpp mpp) {
		this.mpp = mpp;
	}

	public SubCategoria getSubCategoria() {
		return subCategoria;
	}

	public void setSubCategoria(SubCategoria subCategoria) {
		this.subCategoria = subCategoria;
	}

}
