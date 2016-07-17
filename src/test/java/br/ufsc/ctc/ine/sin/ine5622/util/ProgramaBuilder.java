package br.ufsc.ctc.ine.sin.ine5622.util;

import br.ufsc.ctc.ine.sin.ine5622.model.Tipo;

public class ProgramaBuilder {

	private String codigo;

	public ProgramaBuilder() {
		codigo = "";
	}

	public ProgramaBuilder programa(String idPrograma) {
		codigo += "programa " + idPrograma + ";\n";
		return this;
	}

	public ProgramaBuilder linha(String linha) {
		codigo += linha + "\n";
		return this;
	}

	public ProgramaBuilder declaracao(String d) {
		return linha(d);
	}

	public ProgramaBuilder comando(String c) {
		return linha(c);
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

	public ProgramaBuilder declaracaoVetor(Tipo tipo, String dimensao, String... ids) {
		codigo += tipo.getNome() + "[" + dimensao + "] " ;
		codigo += declaracaoIdentificadores(", ", ";\n", ids);
		return this;
	}


	public ProgramaBuilder declaracaoCadeia(String dimensao, String... ids) {
		codigo += Tipo.CADEIA.getNome() + "[" + dimensao + "] " ;
		codigo += declaracaoIdentificadores(", ", ";\n", ids);
		return this;
	}

	public ProgramaBuilder declaracaoMetodo(String id, String parFormais, Tipo tipo) {
		codigo += "metodo " + id;
		codigo += parFormais == null ? "" : "(" + parFormais + ")";
		codigo += (tipo == Tipo.NULO) ? "" : ( ": " + tipo.getNome() );
		codigo += ";\n";
		return this;
	}

	public ProgramaBuilder bloco(String b) {
		codigo += "{\n" + b + "}";
		return this;
	}

	public ProgramaBuilder blocoPrograma(String b) {
		abreBloco();
		comando(b);
		fechaBlocoPrograma();
		return this;
	}

	public ProgramaBuilder blocoPrograma() {
		return blocoPrograma("");
	}

	public ProgramaBuilder blocoMetodo(String b) {
		abreBloco();
		comando(b);
		fechaBlocoMetodo();
		return this;
	}

	public ProgramaBuilder blocoMetodo() {
		return blocoMetodo("");
	}

	public ProgramaBuilder abreBloco() {
		codigo += "{\n";
		return this;
	}

	public ProgramaBuilder fechaBlocoPrograma() {
		codigo += "}.\n";
		return this;
	}

	public ProgramaBuilder fechaBlocoMetodo() {
		codigo += "};\n";
		return this;
	}

	public String build() {
		return codigo;
	}

	public String build(boolean debug) {
		if (debug) {
			System.out.println(codigo);
		}
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
