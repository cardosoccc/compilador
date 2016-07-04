package br.ufsc.ctc.ine.sin.ine5622.model;

import org.junit.Before;
import org.junit.Test;

public class SemanticoTest {

	private Semantico semantico = new Semantico();;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() throws Exception {
		semantico.executeAction(101, new Token(101, "", 101));
	}

}
