/**
 * @author Vitor Schweitzer e Caio Cardoso
 * created on 2016/06/18
 */
package br.ufsc.ctc.ine.sin.ine5622.model;

@SuppressWarnings("serial")
public class LexicalError extends AnalysisError {

	public LexicalError(String msg, int position) {
		super(msg, position);
	}

	public LexicalError(String msg) {
		super(msg);
	}

}
