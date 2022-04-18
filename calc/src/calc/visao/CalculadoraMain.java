package calc.visao;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class CalculadoraMain extends JFrame {


	public CalculadoraMain() {

		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(232, 322);
		organizarLayout();
	}

	private void organizarLayout() {
		setLayout(new BorderLayout());
		// Configuração localizações
		Display display = new Display();
		Teclado teclado = new Teclado();
		add(display, BorderLayout.NORTH);
		add(teclado, BorderLayout.CENTER);
		display.setPreferredSize(new Dimension(233, 60));
	}

	public static void main(String[] args) {
		new CalculadoraMain();
	}
}
