package Principal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Init {

	private static Init instance = null;
	private static String[] Alfabeto = { "a", "b", "c", "ç", "d", "e", "f", "g", "h", "i", "j", "l", "m", "n", "o", "p",
			"q", "r", "s", "t", "u", "v", "x", "z", "k", "w", "y", "*" };
	private List<String> palavras = new ArrayList<String>();
	private List<String> palavrasDic = new ArrayList<String>();
	private List<String> NERlist = new ArrayList<String>();
	private List<String> palavrasForaDic = new ArrayList<String>();

	private Init() {
	}

	public static Init getInstance() {
		if (instance == null) {
			instance = new Init();
		}
		return instance;
	}

	public String toStringNERList() {
		return this.NERlist.toString();
	}

	public List<String> getNERList() {
		return this.NERlist;
	}

	public String toStringPOS() {
		return this.palavras.toString();
	}

	public List<String> getPalavrasPOS() {
		return this.palavras;
	}

	public String toStringPalavrasDIC() {
		return this.palavrasDic.toString();
	}

	public List<String> getPalavrasDIC() {
		return this.palavrasDic;
	}

	public String toStringPalavrasForaDIC() {
		return this.palavrasForaDic.toString();
	}

	public List<String> getPalavrasForaDIC() {
		return this.palavrasForaDic;
	}

	private static void replaceAll(StringBuffer builder, String from, String to) {

		/**
		 * Dada uma string (builder), uma segunda string (form) será buscada
		 * dentro da anterior, e substituida por outra string (to)
		 */

		int index = builder.indexOf(from);
		while (index != -1) {
			builder.replace(index, index + from.length(), to);
			index += to.length();
			index = builder.indexOf(from, index);
		}
	}

	private static void replace(StringBuffer Alt1) {

		/**
		 * Subistitue .? sobrando no meio do texto para não atrapalhar na quebra
		 * de sentenças
		 */

		replaceAll(Alt1, ",", "");
		replaceAll(Alt1, ".", "");
		replaceAll(Alt1, ";", "");
		replaceAll(Alt1, ":", "");
		replaceAll(Alt1, "|", "");
		replaceAll(Alt1, "\"", "");
		replaceAll(Alt1, "'", "");
		replaceAll(Alt1, "?", "");
		replaceAll(Alt1, "/", "");
		replaceAll(Alt1, "]", "");
		replaceAll(Alt1, "}", "");
		replaceAll(Alt1, "~", "");
		replaceAll(Alt1, "^", "");
		replaceAll(Alt1, "[", "");
		replaceAll(Alt1, "{", "");
		replaceAll(Alt1, "º", "");
		replaceAll(Alt1, "ª", "");
		replaceAll(Alt1, "´", "");
		replaceAll(Alt1, "`", "");
		replaceAll(Alt1, "=", "");
		replaceAll(Alt1, "+", "");
		replaceAll(Alt1, "§", "");
		replaceAll(Alt1, "_", "");
		replaceAll(Alt1, ")", "");
		replaceAll(Alt1, "(", "");
		replaceAll(Alt1, "*", "");
		replaceAll(Alt1, "&", "");
		replaceAll(Alt1, "¨", "");
		replaceAll(Alt1, "¬", "");
		replaceAll(Alt1, "%", "");
		replaceAll(Alt1, "¢", "");
		replaceAll(Alt1, "$", "");
		replaceAll(Alt1, "£", "");
		replaceAll(Alt1, "#", "");
		replaceAll(Alt1, "@", "");
		replaceAll(Alt1, "!", "");
		replaceAll(Alt1, "°", "");
		replaceAll(Alt1, "1", "");
		replaceAll(Alt1, "2", "");
		replaceAll(Alt1, "3", "");
		replaceAll(Alt1, "4", "");
		replaceAll(Alt1, "5", "");
		replaceAll(Alt1, "6", "");
		replaceAll(Alt1, "7", "");
		replaceAll(Alt1, "8", "");
		replaceAll(Alt1, "9", "");
		replaceAll(Alt1, "0", "");
		replaceAll(Alt1, "¹", "");
		replaceAll(Alt1, "²", "");
		replaceAll(Alt1, "³", "");

	}

	private static String vogalTratamento(String letra1) {

		if ((letra1.equals("Á")) || (letra1.equals("À")) || (letra1.equals("Â"))) {
			return "A";
		} else if ((letra1.equals("É")) || (letra1.equals("È")) || (letra1.equals("Ê"))) {
			return "E";
		} else if ((letra1.equals("Í")) || (letra1.equals("Ì"))) {
			return "I";
		} else if ((letra1.equals("Ó")) || (letra1.equals("Ô"))) {
			return "O";
		} else if (letra1.equals("Ú")) {
			return "U";
		}

		return letra1;

	}

	public void BuscaDicionarioXML(String palavra) {
		String proxLetra = null;
		String proxLetraLida = null;
		boolean chave = false;
		boolean control = false;
		boolean control2 = false;
		byte key = 0;

		palavra = palavra.substring(0, 1).toUpperCase() + palavra.substring(1).toLowerCase();

		String letra1 = palavra.substring(0, 1);
		String letra2 = null;
		if (palavra.length() > 1)
			letra2 = palavra.substring(1, 2);
		else
			letra2 = "";

		letra1 = vogalTratamento(letra1);

		for (int i = 0; i < 26; i++) {
			if (letra2.equals(Alfabeto[i])) {
				proxLetra = Alfabeto[i + 1];
			}
		}
		if ((proxLetra != null) && (proxLetra.equals("*"))) {
			proxLetra = Alfabeto[26];
		}

		StringBuffer aux = new StringBuffer();
		aux.append(palavra);

		if (letra1.equals("A") || letra1.equals("B") || letra1.equals("C") || letra1.equals("D") || letra1.equals("E")
				|| letra1.equals("F") || letra1.equals("G") || letra1.equals("H") || letra1.equals("I")
				|| letra1.equals("J") || letra1.equals("K") || letra1.equals("L") || letra1.equals("M")
				|| letra1.equals("N") || letra1.equals("O") || letra1.equals("P") || letra1.equals("Q")
				|| letra1.equals("R") || letra1.equals("S") || letra1.equals("T") || letra1.equals("U")
				|| letra1.equals("V") || letra1.equals("W") || letra1.equals("X") || letra1.equals("Y")
				|| letra1.equals("Z")) {
			try {
				// doc tratamento e abertura arquivo xml
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse("src/Dicxml/" + letra1 + ".xml");

				// busca tag dentro do xml
				NodeList listaDePalavras = doc.getElementsByTagName("entry");

				int tamanhoLista = listaDePalavras.getLength();

				for (int i = 0; i < tamanhoLista; i++) {

					Node noPalavra = listaDePalavras.item(i);

					if (noPalavra.getNodeType() == Node.ELEMENT_NODE) {

						Element elementoPalavra = (Element) noPalavra;

						String id = elementoPalavra.getAttribute("id");

						if (id.length() > 1)
							proxLetraLida = id.substring(1, 2);

						if (id.contains("(")) {

							String TextAux[] = id.split(Pattern.quote(" "));

							byte per = 0;

							for (per = 0; per < TextAux.length; per++) {
								if (TextAux[per].equals(palavra)) {
									chave = true;
								}
							}
						}

						if ((id.equals(palavra) || (chave == true)) && !(proxLetra.equals(proxLetraLida))) {

							key++;

							if (key <= 1) {
								this.palavrasDic.add(palavra);
							}

							NodeList listaDeFilhosDaPalavra = elementoPalavra.getChildNodes();

							int tamanhoListaFilhos = listaDeFilhosDaPalavra.getLength();

							System.out.println("Palavra: " + palavra + "\r\n");

							control = true;

							for (int j = 0; j < tamanhoListaFilhos; j++) {

								Node noFilho = listaDeFilhosDaPalavra.item(j);

								if (noFilho.getNodeType() == Node.ELEMENT_NODE) {

									Element elementoFilho = (Element) noFilho;

									switch (elementoFilho.getTagName()) {

									case "gramGrp":
										System.out.println("Classificação Gramática:\r\n "
												+ elementoFilho.getTextContent() + "\r\n");
										break;

									case "def":
										System.out.println("Definição: \r\n" + elementoFilho.getTextContent() + "\r\n");
										System.out.println(
												"--------------------------------------------------------------------------");
										break;

									}

								}
							}
						}

						chave = false;

					}
					if (proxLetra.equals(proxLetraLida) && key > 0) {
						break;
					} else if (proxLetra.equals(proxLetraLida) && key == 0) {
						// System.out.println("\r" + palavra + " não está
						// contida no dicionário\r\n");
						control2 = true;
						break;
					}
				}

				/*
				 * if (control == false && control2 == false) {
				 * System.out.println("\r" + palavra +
				 * " não está contida no dicionário\r\n"); }
				 */

			} catch (ParserConfigurationException ex) {
				Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
			} catch (SAXException ex) {
				Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IOException ex) {
				Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

	}

	public void BuscaDicionario(String palavra) {
		String proxLetra = null;
		String proxLetraLida = null;

		palavra = palavra.substring(0, 1).toUpperCase() + palavra.substring(1).toLowerCase();

		String letra1 = palavra.substring(0, 1);
		String letra2 = palavra.substring(1, 2);

		letra1 = vogalTratamento(letra1);

		for (int i = 0; i < 26; i++) {
			if (letra2.equals(Alfabeto[i])) {
				proxLetra = Alfabeto[i + 1];
			}
		}
		if ((proxLetra != null) && (proxLetra.equals("*"))) {
			proxLetra = Alfabeto[26];
		}

		BufferedReader leitor = null;
		StringBuffer aux = new StringBuffer();
		aux.append(palavra);
		int key = 0;

		if (letra1.equals("A") || letra1.equals("B") || letra1.equals("C") || letra1.equals("D") || letra1.equals("E")
				|| letra1.equals("F") || letra1.equals("G") || letra1.equals("H") || letra1.equals("I")
				|| letra1.equals("J") || letra1.equals("K") || letra1.equals("L") || letra1.equals("M")
				|| letra1.equals("N") || letra1.equals("O") || letra1.equals("P") || letra1.equals("Q")
				|| letra1.equals("R") || letra1.equals("S") || letra1.equals("T") || letra1.equals("U")
				|| letra1.equals("V") || letra1.equals("W") || letra1.equals("X") || letra1.equals("Y")
				|| letra1.equals("Z")) {
			try {

				leitor = new BufferedReader(new FileReader("src/Dictxt/" + letra1 + "saida.txt"));

				String linha = "";

				while (((linha = leitor.readLine()) != null) && !(proxLetra.equals(proxLetraLida)) && (key == 0)) {
					if (linha.length() > 0) {
						if (!(linha.substring(0, 1).equals("(")))
							if (!(linha.substring(1, 2).equals("(")))
								proxLetraLida = linha.substring(1, 2);
						// System.out.println(linha);
						if (linha.contains(aux)) {
							this.palavrasDic.add(palavra);
							System.out.println("(" + palavra + ") está contida no dicionário");
							key = 1;
						}
					}

				}
				if (key == 0) {
					System.out.println("(" + palavra + ") não está contida no dicionário");
				}
			} catch (Exception e) {
				System.out.println("Arquivo inexistente");
				// e.printStackTrace();
			} finally {
				try {
					leitor.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.out.println("### A palavra " + palavra + " contém erro ####");
		}
	}

	public void pos(String texto) {

		StringBuffer Alt1 = new StringBuffer();
		String palavraC;
		int count;

		String Alt2[] = texto.split(Pattern.quote(" "));

		for (int i = 0; i < Alt2.length; i++) {
			if (!(Alt2[i].equals(",")) && !(Alt2[i].equals(".")) && !(Alt2[i].equals(";")) && !(Alt2[i].equals(":"))
					&& !(Alt2[i].equals("|")) && !(Alt2[i].equals("\"")) && !(Alt2[i].equals("'"))
					&& !(Alt2[i].equals("?")) && !(Alt2[i].equals("/")) && !(Alt2[i].equals("]"))
					&& !(Alt2[i].equals("}")) && !(Alt2[i].equals("~")) && !(Alt2[i].equals("^"))
					&& !(Alt2[i].equals("[")) && !(Alt2[i].equals("{")) && !(Alt2[i].equals("º"))
					&& !(Alt2[i].equals("ª")) && !(Alt2[i].equals("´")) && !(Alt2[i].equals("`"))
					&& !(Alt2[i].equals("=")) && !(Alt2[i].equals("+")) && !(Alt2[i].equals("§"))
					&& !(Alt2[i].equals("-")) && !(Alt2[i].equals("_")) && !(Alt2[i].equals(")"))
					&& !(Alt2[i].equals("(")) && !(Alt2[i].equals("...")) && !(Alt2[i].equals(".."))
					&& !(Alt2[i].equals("*")) && !(Alt2[i].equals("&")) && !(Alt2[i].equals("¨"))
					&& !(Alt2[i].equals("¬")) && !(Alt2[i].equals("¬¬")) && !(Alt2[i].equals("%"))
					&& !(Alt2[i].equals("¢")) && !(Alt2[i].equals("$")) && !(Alt2[i].equals("£"))
					&& !(Alt2[i].equals("#")) && !(Alt2[i].equals("@")) && !(Alt2[i].equals("!"))
					&& !(Alt2[i].equals("°")) && !(Alt2[i].equals("1")) && !(Alt2[i].equals("2"))
					&& !(Alt2[i].equals("3")) && !(Alt2[i].equals("4")) && !(Alt2[i].equals("5"))
					&& !(Alt2[i].equals("6")) && !(Alt2[i].equals("7")) && !(Alt2[i].equals("8"))
					&& !(Alt2[i].equals("9")) && !(Alt2[i].equals("0")) && !(Alt2[i].equals("¹"))
					&& !(Alt2[i].equals("²")) && !(Alt2[i].equals("³"))) {

				Alt1.append(Alt2[i]);
				replace(Alt1);
				palavraC = new String(Alt1);
				count = Alt1.length();
				Alt1.delete(0, count);
				// System.out.println(palavraC);
				this.palavras.add(palavraC.toLowerCase());

			}
		}

		palavrasForaDic = palavras;

	}

	public void ExecucaoTXT(String frase) {

		System.out.println("================================================================================");
		System.out.println("Frase:");
		System.out.println(frase);
		System.out.println("================================================================================");

		this.pos(frase);

		System.out.println("\nBusca no dicionário de palavras TXT:\n");

		for (int i = 0; i < this.palavras.size(); i++) {
			BuscaDicionario(palavras.get(i));
		}

	}

	public void ExecucaoXML(String frase) {

		System.out.println("================================================================================");
		System.out.println("Frase:");
		System.out.println(frase);
		System.out.println("================================================================================");

		this.pos(frase);

		System.out.println("\nBusca no dicionário de palavras XML:\n");

		for (int i = 0; i < this.palavras.size(); i++) {
			BuscaDicionarioXML(palavras.get(i));
		}

	}

	public void ExecucaoNER() {

		for (int i = 0; i < this.palavrasForaDic.size(); i++) {
			for (int j = 0; j < this.palavrasDic.size(); j++) {
				if (this.palavrasForaDic.get(i).equals(this.palavrasDic.get(j).toLowerCase())) {
					this.palavrasForaDic.remove(i);
				}
			}
		}

		System.out.println("================================================================================");

		System.out.println("\nBuscando palavras na base de dados Wikipédia:\n");

		for (int p = 0; p < this.palavrasForaDic.size(); p++) {

			NERwiki(this.palavrasForaDic.get(p));

		}

		System.out.println("\nBusca Concluída!\n");

		for (int i = 0; i < this.palavrasForaDic.size(); i++) {
			for (int j = 0; j < this.NERlist.size(); j++) {
				if (this.palavrasForaDic.get(i).equals(this.NERlist.get(j).toLowerCase())) {
					this.palavrasForaDic.remove(i);
				}
			}
		}

		System.out.println("================================================================================");

	}

	public void MAINxml(String frase) {
		ExecucaoXML(frase);
		ExecucaoNER();
	}

	public void MAINtxt(String frase) {
		ExecucaoTXT(frase);
		ExecucaoNER();
	}

	public void NERwiki(String palavraNER) {

		String stringURL = "https://pt.wikipedia.org/w/index.php?search=";
		stringURL = stringURL.concat(palavraNER);
		String resposta = "";

		try {
			URL url = new URL(stringURL);
			URLConnection connection = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer sb = new StringBuffer();
			while ((inputLine = in.readLine()) != null)
				sb.append(inputLine);
			resposta = sb.toString();
			in.close();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println("sem conexão com a internet");
			// ex.printStackTrace();
		}

		String Alt[] = resposta.split(Pattern.quote(">"));
		StringBuffer aux = new StringBuffer();
		aux.append("<link rel=\"canonical\"");
		String tag = "";
		for (int i = 0; i < Alt.length; i++) {
			if (Alt[i].contains(aux)) {
				tag = Alt[i];
			}
		}

		StringBuffer aux2 = new StringBuffer();
		aux2.append("/index.php?");

		if (!tag.contains(aux2)) {
			this.NERlist.add(palavraNER);
		}

		// diferença na inspeção do elemento <link rel="canonical"
		// href="https://pt.wikipedia.org/wiki/Mateus">
	}

}
