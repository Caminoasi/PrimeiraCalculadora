package calc.modelo;

import java.util.ArrayList;
import java.util.List;

public class Memoria {

	private enum TipoComando {
		ZERAR, SINAL, NUMERO, DIV, MULTI, SUBI, SOMA, IGUAL, VIRGULA;
	}

	private static final Memoria instancia = new Memoria();
	private String textoBuffer = "";
	private String textoAtual = "";
	private boolean substutuir = false;
	private TipoComando ultimaoperacao = null;

	private Memoria() {

	}

	// Criar o método para adicionar observadores
	private final List<MemoriaObservador> observadores = new ArrayList<>();

	public void adicionarObservadores(MemoriaObservador observador) {
		observadores.add(observador);
	}

	// Chamado para registro de número
	public static Memoria getInstancia() {
		return instancia;
	}

	public String getTextoAtual() {
		return textoAtual.isEmpty() ? "0" : textoAtual;
	}

	// Alterar o número
	public void processadorComando(String texto) {
		TipoComando tipoComando = detectarTipoComando(texto);
		if (tipoComando == null) {
			return;
		} else if (tipoComando == TipoComando.ZERAR) {
			substutuir = false;
			textoBuffer = "";
			textoAtual = "";
			ultimaoperacao = null;
		} else if (tipoComando == TipoComando.NUMERO || tipoComando == TipoComando.VIRGULA) {
			textoAtual = substutuir ? texto : textoAtual + texto;
			substutuir = false;
		} else if (tipoComando == TipoComando.SINAL && textoAtual.contains("-")) {
			textoAtual = textoAtual.substring(1);
		} else if (tipoComando == TipoComando.SINAL && !textoAtual.contains("-")) {
			textoAtual = "-" + textoAtual;
		} else {
			substutuir = true;
			textoAtual = obterResultadoOperacao();
			textoBuffer = textoAtual;
			ultimaoperacao = tipoComando;
		}
		// Notificar os observadores
		observadores.forEach(o -> o.valorAlterado(getTextoAtual()));
	}

	private String obterResultadoOperacao() {
		if (ultimaoperacao == null || ultimaoperacao == TipoComando.IGUAL) {
			return textoAtual;
		}
		double numeroBuffer = Double.parseDouble(textoBuffer.replace(",", "."));
		double numeroAtual = Double.parseDouble(textoAtual.replace(",", "."));
		double resultado = 0;
		if (ultimaoperacao == TipoComando.SOMA) {
			resultado = numeroBuffer + numeroAtual;
		} else if (ultimaoperacao == TipoComando.SUBI) {
			resultado = numeroBuffer - numeroAtual;
		} else if (ultimaoperacao == TipoComando.MULTI) {
			resultado = numeroBuffer * numeroAtual;
		} else if (ultimaoperacao == TipoComando.DIV) {
			resultado = numeroBuffer / numeroAtual;
		}
		String resultadoString = Double.toString(resultado).replace(".", ",");
		boolean inteiro = resultadoString.endsWith(",0");
		return inteiro ? resultadoString.replace(",0", "") : resultadoString;
	}

	private TipoComando detectarTipoComando(String texto) {
		if (textoAtual.isEmpty() && texto == "0") {
			return null;
		}
		try {
			Integer.parseInt(texto);
			return TipoComando.NUMERO;
		} catch (NumberFormatException e) {
			// Quando não for Número
			if ("AC".equals(texto)) {
				return TipoComando.ZERAR;
			} else if ("*".equals(texto)) {
				return TipoComando.MULTI;
			} else if ("/".equals(texto)) {
				return TipoComando.DIV;
			} else if ("+".equals(texto)) {
				return TipoComando.SOMA;
			} else if ("-".equals(texto)) {
				return TipoComando.SUBI;
			} else if (",".equals(texto) && !textoAtual.contains(",")) {
				return TipoComando.VIRGULA;
			} else if ("=".equals(texto)) {
				return TipoComando.IGUAL;
			} else if ("±".equals(texto)) {
				return TipoComando.SINAL;
			}
		}

		return null;
	}
}
