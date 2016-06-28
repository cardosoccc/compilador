package br.ufsc.ctc.ine.sin.ine5622.model;

public class SemanticError extends AnalysisError {

	public SemanticError(String msg, int position) {
		super(msg, position);
	}

	public SemanticError(String msg) {
		super(msg);
	}
}
