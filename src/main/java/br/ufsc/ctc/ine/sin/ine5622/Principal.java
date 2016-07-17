/**
 * @author Vitor Schweitzer e Caio Cardoso
 * created on 2016/06/18
 */
package br.ufsc.ctc.ine.sin.ine5622;

import java.awt.EventQueue;

import br.ufsc.ctc.ine.sin.ine5622.control.Gerente;
import br.ufsc.ctc.ine.sin.ine5622.infra.ManipuladorArquivo;
import br.ufsc.ctc.ine.sin.ine5622.model.Lexico;
import br.ufsc.ctc.ine.sin.ine5622.model.Semantico;
import br.ufsc.ctc.ine.sin.ine5622.model.Sintatico;
import br.ufsc.ctc.ine.sin.ine5622.view.TelaPrincipal;

/**
 * Responsável por inicializar a aplicação
 */
public class Principal {

	public static void main(String[] args) {

		Lexico lexico = new Lexico();
		Sintatico sintatico = new Sintatico();
		Semantico semantico = new Semantico();
		ManipuladorArquivo manipuladorArquivo = new ManipuladorArquivo();

		Gerente gerente = new Gerente(manipuladorArquivo, lexico, sintatico, semantico);

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaPrincipal window = new TelaPrincipal(gerente);
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
