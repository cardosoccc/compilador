package br.ufsc.ctc.ine.sin.ine5622.model;

public class SyntaticError extends AnalysisError {

	public SyntaticError(String msg, int position) {
		super(msg, position);
	}

	public SyntaticError(String msg) {
		super(msg);
	}
}
