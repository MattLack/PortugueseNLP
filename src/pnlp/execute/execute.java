package pnlp.execute;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.cogroo.analyzer.Analyzer;
import org.cogroo.analyzer.ComponentFactory;
import org.cogroo.checker.CheckDocument;
import org.cogroo.checker.GrammarChecker;
import org.cogroo.entities.Mistake;
import org.cogroo.text.Document;
import org.cogroo.text.impl.DocumentImpl;
import org.cogroo.util.TextUtils;

import Principal.Init;

public class execute {

	public static void main(String[] args) throws IllegalArgumentException, IOException {

		long start = System.nanoTime();

		String input = "Isto é um exemplo de erro gramaticais.";

		ComponentFactory factory = ComponentFactory.create(new Locale("pt", "BR"));

		Analyzer pipe = factory.createPipe();
		GrammarChecker gc = new GrammarChecker(pipe);

		System.out.println("Loading time [" + ((System.nanoTime() - start) / 1000000) + "ms]");

		Document document = new DocumentImpl();
		document.setText(input);
		pipe.analyze(document);

		CheckDocument document2 = new CheckDocument(input);

		gc.analyze(document2);
		
		System.out.println(TextUtils.nicePrint(document));
		//System.out.println(document2);

		List<Mistake> m = document2.getMistakes();

		//System.out.println(m.get(0).getShortMessage());
		System.out.println(m.get(0).getLongMessage());


		// =======================================================================================//
		
		Init.getInstance().MAINxml(input);
		System.out.println("Palavras no DIc: "+ Init.getInstance().toStringPalavrasDIC());
		System.out.println("Palavras ner: "+ Init.getInstance().toStringNERList());
		System.out.println("Palavras fora do dic: "+Init.getInstance().toStringPalavrasForaDIC());
		
		
	}
}
