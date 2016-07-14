package br.ufsc.ctc.ine.sin.ine5622.util;

import br.ufsc.ctc.ine.sin.ine5622.model.Tipo;

public class ProgramaBuilder {

	private String codigo;

	public ProgramaBuilder() {
		codigo = "";
	}

	public ProgramaBuilder programa(String idPrograma) {
		codigo = "programa " + idPrograma + ";\n";
		return this;
	}

	public ProgramaBuilder declaracao(String declaracao) {
		codigo += declaracao + "\n";
		return this;
	}

	public ProgramaBuilder declaracaoVariavel(Tipo tipo, String... ids) {
		codigo += tipo.getNome() + " " ;
		codigo += declaracaoIdentificadores(", ", ";\n", ids);
		return this;
	}

	public ProgramaBuilder declaracaoConstante(Tipo tipo, String valor, String... ids) {
		codigo += tipo.getNome() + " " ;
		codigo += declaracaoIdentificadores(", ", " = ", ids);
		codigo += valor;
		codigo += ";\n";
		return this;
	}

	public ProgramaBuilder declaracaoVetor(Tipo tipo, int dimensao, String... ids) {
		codigo += tipo.getNome() + "[" + dimensao + "] " ;
		declaracaoIdentificadores(", ", ";\n", ids);
		return this;
	}


	public ProgramaBuilder declaracaoCadeia(int dimensao, String... ids) {
		codigo += Tipo.CADEIA.getNome() + "[" + dimensao + "] " ;
		declaracaoIdentificadores(", ", ";\n", ids);
		return this;
	}


	public ProgramaBuilder bloco(String bloco) {
		codigo += "{" + bloco + "}.";
		return this;
	}

	public ProgramaBuilder bloco() {
		codigo += "{}.";
		return this;
	}

	public String build() {
		return codigo;
	}

	private String declaracaoIdentificadores(String separador, String fim, String... ids) {
		String trechoCodigo = "";
		for (int i = 0; i < ids.length; i++) {
			trechoCodigo += ids[i];
			if (i < ids.length - 1) {
				trechoCodigo += separador;
			} else {
				trechoCodigo += fim;
			}
		}
		return trechoCodigo;
	}
}
