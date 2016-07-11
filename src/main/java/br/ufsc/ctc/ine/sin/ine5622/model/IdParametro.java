package br.ufsc.ctc.ine.sin.ine5622.model;

public class IdParametro extends Identificador {

	private String mpp;

	public IdParametro(String nome) {
		super(nome);
	}

	public IdParametro(Identificador id) {
		super(id.getNome());
		this.setDeslocamento(id.getDeslocamento());
		this.setNivel(id.getNivel());
	}

	public String getMpp() {
		return mpp;
	}

	public void setMpp(String mpp) {
		this.mpp = mpp;
	}

}
