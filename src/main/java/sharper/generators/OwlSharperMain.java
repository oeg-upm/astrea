package sharper.generators;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.jena.rdf.model.Model;

import com.github.jsonldjava.core.DocumentLoader;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;

import shaper.model.ShaperModel;

public class OwlSharperMain {

	public static void main(String[] args) throws IOException {
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
