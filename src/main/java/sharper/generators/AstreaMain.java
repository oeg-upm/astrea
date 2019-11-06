package sharper.generators;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import org.apache.jena.rdf.model.Model;

import astrea.model.ShaclFromOwl;

public class AstreaMain {

	public static void main(String[] args) throws IOException {
		ShaclFromOwl sharper = new OptimisedOwlGenerator();
		Model shapes =  sharper.fromURL("http://iot.linkeddata.es/def/core/ontology.ttl");
		shapes.write(System.out, "TURTLE");
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
