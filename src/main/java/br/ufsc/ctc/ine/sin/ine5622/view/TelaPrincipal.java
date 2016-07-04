package br.ufsc.ctc.ine.sin.ine5622.view;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import br.ufsc.ctc.ine.sin.ine5622.control.Gerente;
import br.ufsc.ctc.ine.sin.ine5622.model.AnalysisError;
import br.ufsc.ctc.ine.sin.ine5622.model.LexicalError;
import br.ufsc.ctc.ine.sin.ine5622.model.SemanticError;
import br.ufsc.ctc.ine.sin.ine5622.model.SyntaticError;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class TelaPrincipal {

	private JFrame frmCompiladorLsi;
	private JTextArea textArea;
	private Gerente gerente;

	/**
	 * Main
	 */
	public TelaPrincipal(Gerente gerente) {
		this.gerente = gerente;
		initialize();
	}

	/**
	 * Monta e inicializa a tela principal.
	 */
	private void initialize() {
		frmCompiladorLsi = new JFrame();
		frmCompiladorLsi.setTitle("Compilador LSI-161");
		frmCompiladorLsi.setBounds(100, 100, 450, 300);
		frmCompiladorLsi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		textArea = new JTextArea();
		JScrollPane jScrollPane = new JScrollPane(textArea);
		frmCompiladorLsi.getContentPane().add(jScrollPane, BorderLayout.CENTER);

		JMenuBar menuBar = new JMenuBar();
		frmCompiladorLsi.setJMenuBar(menuBar);
		configurarMenuArquivo(menuBar);
		configurarBotaoAnaliseLexica(menuBar);
		configurarBotaoAnaliseSintatica(menuBar);
		configurarBotaoAjuda(menuBar);
	}

	private void configurarMenuArquivo(JMenuBar menuBar) {
		JMenu menuArquivo = new JMenu("Arquivo");
		menuBar.add(menuArquivo);
		configurarBotaoAbrir(menuArquivo);
		configurarBotaoSalvar(menuArquivo);
		configurarBotaoSalvarComo(menuArquivo);

	}

	private void configurarBotaoSalvarComo(JMenu menuArquivo) {
		JMenuItem menuSalvarComo = new JMenuItem("Salvar Como...");
		menuSalvarComo.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				salvarArquivoComo(textArea.getText());
			}
		});
		menuArquivo.add(menuSalvarComo);
	}

	private void configurarBotaoSalvar(JMenu menuArquivo) {
		JMenuItem menuSalvar = new JMenuItem("Salvar");
		menuSalvar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				salvarArquivo(textArea.getText());
			}
		});
		menuArquivo.add(menuSalvar);
	}

	private void configurarBotaoAbrir(JMenu menuArquivo) {
		JMenuItem menuAbrir = new JMenuItem("Abrir");
		menuAbrir.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(carregarArquivo())
					textArea.setText(gerente.getConteudoArquivo());
			}
		});
		menuArquivo.add(menuAbrir);
	}

	private void configurarBotaoAnaliseLexica(JMenuBar menuBar) {
		JMenu mnLxico = new JMenu("L\u00E9xico");
		menuBar.add(mnLxico);
		JMenuItem mntmAnalisar = new JMenuItem("Analisar");
		mntmAnalisar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				analiseLexica(textArea.getText());
			}
		});
		mnLxico.add(mntmAnalisar);
	}

	private void configurarBotaoAnaliseSintatica(JMenuBar menuBar) {
		JMenu mnSinttico = new JMenu("Sint\u00E1tico");
		menuBar.add(mnSinttico);
		JMenuItem mntmAnalisar_1 = new JMenuItem("Analisar");
		mntmAnalisar_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				analiseSintatica(textArea.getText());
			}
		});
		mnSinttico.add(mntmAnalisar_1);
	}

	private void configurarBotaoAjuda(JMenuBar menuBar) {
		JMenu mnAjuda = new JMenu("Ajuda");
		menuBar.add(mnAjuda);

		JMenuItem mntmSobre = new JMenuItem("Sobre");
		mntmSobre.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				informacoesAplicativo();
			}
		});
		mnAjuda.add(mntmSobre);
	}

	public void analiseLexica (String conteudo) {
		try {
			gerente.analisadorLexico(conteudo);
			informeUsuario("Nenhum erro l�xico.");
		} catch (LexicalError e) {
			tratarErroDeAnalise(e);
		}
	}

	public void analiseSintatica(String conteudo) {
		try {
			gerente.analisadorSintatico(conteudo);
			informeUsuario("Nenhum erro l�xico ou sint�tico.");
		} catch (AnalysisError e) {
			tratarErroDeAnalise(e);
		}
	}

	private void tratarErroDeAnalise(AnalysisError e) {
		this.textArea.setCaretPosition(e.getPosition());
		String mensagem = "Encontrado um erro ";
		mensagem += (e instanceof LexicalError)  ? "l�xico" :
					(e instanceof SyntaticError) ? "sint�tico" :
					(e instanceof SemanticError) ? "sem�ntico" : "";
		mensagem += ":\n\n" + "Msg: " + e.getMessage() + "\nPosi��o: " + e.getPosition();
		this.informeUsuario(mensagem);
	}



	/**
	 * Abrir arquivo
	 */
	public boolean carregarArquivo() {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("."));
		int returnVal = fc.showOpenDialog(this.frmCompiladorLsi);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			gerente.carregarArquivo(fc.getSelectedFile());
			return true;
		} else if (returnVal == JFileChooser.ERROR_OPTION)
			JOptionPane.showMessageDialog(this.frmCompiladorLsi, "Arquivo inv�lido");
		return false;
	}

	/**
	 * Salvar arquivo j� existente
	 */
	public void salvarArquivo(String conteudo) {
		boolean resultadoSalvar = gerente.salvarArquivo(conteudo);
		if (resultadoSalvar)
			JOptionPane.showMessageDialog(this.frmCompiladorLsi, "Arquivo salvo com sucesso");
		else
			salvarArquivoComo(conteudo);
	}

	/**
	 * Salvar novo arquivo
	 */
	public void salvarArquivoComo(String conteudo) {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("."));
		int returnVal = fc.showSaveDialog(this.frmCompiladorLsi);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			if (!fc.getSelectedFile().exists()) {
				gerente.salvarArquivoComo(fc.getSelectedFile(), conteudo);
				// Validar com try ou return
				JOptionPane.showMessageDialog(this.frmCompiladorLsi, "Arquivo criado com sucesso");
			} else {
				int opcaoSubstituir = JOptionPane.showConfirmDialog(this.frmCompiladorLsi,
						"Este arquivo j� existe, deseja substitu�-lo?", "Arquivo existente", JOptionPane.YES_NO_OPTION);
				if (opcaoSubstituir == JOptionPane.YES_OPTION) {
					gerente.salvarArquivoComo(fc.getSelectedFile(), conteudo);
					// Validar com try ou return
					JOptionPane.showMessageDialog(this.frmCompiladorLsi, "Arquivo substituido com sucesso");
				} else
					JOptionPane.showMessageDialog(this.frmCompiladorLsi, "Arquivo n�o foi salvo");
			}
		} else if (returnVal == JFileChooser.ERROR_OPTION)
			JOptionPane.showMessageDialog(this.frmCompiladorLsi, "Ero ao selecionar arquivo");
	}

	/**
	 * Apresentar mensagens ao usu�rio
	 */
	public void informeUsuario (String mensagem){
		JOptionPane.showMessageDialog(this.getFrame(), mensagem);
	}

	/**
	 * Busca e mostra as informa��es do aplicativo
	 */
	private void informacoesAplicativo() {
		informeUsuario(gerente.informacoesAplicativo());
	}

	public JFrame getFrame() {
		return frmCompiladorLsi;
	}

}
