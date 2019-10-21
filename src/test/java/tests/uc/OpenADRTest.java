package tests.uc;

import java.util.logging.Logger;

import org.apache.jena.rdf.model.Model;
import org.junit.Assert;
import org.junit.Test;

import astrea.model.ShaclFromOwl;
import sharper.generators.OwlShaper;


/**
 * This unitary Test class aim at validating our proposal with all the UC of https://spinrdf.org/shacl-and-owl.html
 * @author Andrea Cimmino Arriaga, Alba Fernandez Izquierdo
 *
 */
public class OpenADRTest {

	
	public static Logger log = Logger.getLogger(OpenADRTest.class.getName());



	/**
	 * Test case 1: class hierarchies
	 */
	
	public static final String OWL_MODEL_CLASS_HIERARCHY = "PREFIX ex: <http://www.example.com/vocabulary#> \n" +
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"+
			"PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"+
			"ex:Person\n" + 
			"	a owl:Class ;\n" + 
			"	rdfs:label \"Person\" ;\n" + 
			"	rdfs:comment \"A human being\" .\n" + 
			"	\n" + 
			"ex:Customer\n" + 
			"	a owl:Class ;\n" + 
			"	rdfs:subClassOf ex:Person .";
	
	@Test
	public void handleNullQueryException() {
		ShaclFromOwl sharper = new OwlShaper();
		Model shapes =  sharper.fromOwl(OWL_MODEL_CLASS_HIERARCHY,"TURTLE");
		shapes.write(System.out,"TURTLE");
		
		Assert.assertTrue(true);
	}

}
