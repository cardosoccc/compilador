package br.ufsc.ctc.ine.sin.ine5622.model;

public class Semantico implements Constants {

	public void executeAction(int action, Token token) throws SemanticError {
		System.out.println("Ação #" + action + ", Token: " + token);
	}
}
