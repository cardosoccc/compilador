package br.ufsc.ctc.ine.sin.ine5622.model;

public class IdVariavel extends Identificador implements IdComSubCategoria {

	private SubCategoria subCategoria;

	public IdVariavel(String nome) {
		super(nome);
	}

	public IdVariavel(Identificador id) {
		super(id.getNome());
		this.setDeslocamento(id.getDeslocamento());
		this.setNivel(id.getNivel());
	}

	public SubCategoria getSubCategoria() {
		return subCategoria;
	}

	public void setSubCategoria(SubCategoria subCategoria) {
		this.subCategoria = subCategoria;
	}

}
