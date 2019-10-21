package tests.ontologies.time;

import java.util.logging.Logger;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Test;

import astrea.model.ShaclFromOwl;
import sharper.generators.OwlShaper;


/**
 * This unitary Test class aim at validating our proposal with all the UC of https://spinrdf.org/shacl-and-owl.html
 * @author Andrea Cimmino Arriaga, Alba Fernandez Izquierdo
 *
 */
public class CardinalityTest {

	
	public static Logger log = Logger.getLogger(CardinalityTest.class.getName());

	// Extracted from: https://www.w3.org/2006/time#s
	public static final String CARDINALITY_OWL_FRAGMENT_WITH_OWL_CARDINALITY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ." +
			":Duration\n" + 
			"  rdf:type owl:Class ;\n" + 
			"  rdfs:comment \"Duration of a temporal extent expressed as a number scaled by a temporal unit\"@en ;\n" + 
			"  rdfs:label \"Time duration\"@en ;\n" + 
			"  rdfs:subClassOf :TemporalDuration ;\n" + 
			"  rdfs:subClassOf [\n" + 
			"      rdf:type owl:Restriction ;\n" + 
			"      owl:cardinality \"1\"^^xsd:nonNegativeInteger ;\n" + 
			"      owl:onProperty :numericDuration ;\n" + 
			"    ] ;\n" + 
			"  rdfs:subClassOf [\n" + 
			"      rdf:type owl:Restriction ;\n" + 
			"      owl:minCardinality \"2\"^^xsd:nonNegativeInteger ;\n" + 
			"      owl:onProperty :unitType ;\n" + 
			"    ] ;\n" + 
			"  rdfs:subClassOf [\n" + 
			"      rdf:type owl:Restriction ;\n" + 
			"      owl:maxCardinality \"3\"^^xsd:nonNegativeInteger ;\n" + 
			"      owl:onProperty :month ;\n" + 
			"    ] ;\n" + 
			"  skos:definition \"Duration of a temporal extent expressed as a number scaled by a temporal unit\"@en ;\n" + 
			"  skos:note \"Alternative to time:DurationDescription to support description of a temporal duration other than using a calendar/clock system.\"@en ;\n" + 
			".\n";
	
	private static final String SH_MAX_COUNT = "http://www.w3.org/ns/shacl#maxCount";
	private static final String SH_MIN_COUNT = "http://www.w3.org/ns/shacl#minCount";
	

	
	@Test
	public void compliantWithOwlCardinality() {
		ShaclFromOwl sharper = new OwlShaper();
		Model shapes =  sharper.fromOwl(CARDINALITY_OWL_FRAGMENT_WITH_OWL_CARDINALITY, "TURTLE");
		shapes.write(System.out, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_MAX_COUNT), ResourceFactory.createTypedLiteral(1));
		condition &= shapes.contains(null, ResourceFactory.createProperty(SH_MIN_COUNT), ResourceFactory.createTypedLiteral(1));
		Assert.assertTrue(condition);
	}
	
	@Test
	public void compliantWithOwlMinCardinality() {
		ShaclFromOwl sharper = new OwlShaper();
		Model shapes =  sharper.fromOwl(CARDINALITY_OWL_FRAGMENT_WITH_OWL_CARDINALITY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_MIN_COUNT), ResourceFactory.createTypedLiteral(2));
		Assert.assertTrue(condition);
	}
	
	
	@Test
	public void compliantWithOwlMaxCardinality() {
		ShaclFromOwl sharper = new OwlShaper();
		Model shapes =  sharper.fromOwl(CARDINALITY_OWL_FRAGMENT_WITH_OWL_CARDINALITY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_MAX_COUNT), ResourceFactory.createTypedLiteral(3));
		Assert.assertTrue(condition);
	}
	
	
	
	
	
	
}
