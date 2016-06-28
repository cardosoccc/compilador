package Control;

import java.awt.EventQueue;

import View.TelaPrincipal;

public class Principal {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		Gerente gerente = new Gerente();

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
