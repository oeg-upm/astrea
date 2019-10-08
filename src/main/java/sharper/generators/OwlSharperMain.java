package sharper.generators;

import java.io.StringWriter;
import java.io.Writer;

import org.apache.jena.rdf.model.Model;

import shaper.model.ShaperModel;

public class OwlSharperMain {

	public static void main(String[] args) {
		ShaperModel sharper = new OwlShaper();
		Model shapes =  sharper.fromModelToShapes("https://albaizq.github.io/OpenADRontology/OnToology/ontology/openADRontology.owl/documentation/ontology.ttl");
		
	}

	

	public static String toStringRDF(Model model) {
		Writer output = new StringWriter();
		model.write(output, "TURTLE");
		return output.toString();
	}
	

	public static String toStringRDF(Model model, String format) {
		Writer output = new StringWriter();
		model.write(output, format);
		return output.toString();
	}
	
}
