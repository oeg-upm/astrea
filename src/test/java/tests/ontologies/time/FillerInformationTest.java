package tests.ontologies.time;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Test;

import astrea.model.ShaclFromOwl;
import sharper.generators.OwlShaper;

public class FillerInformationTest {

	private static final String HAS_VALUE_OWL_FRAGMENT_WITH_HAS_VALUE = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ." +
			":January\n" + 
			"  rdf:type owl:Class ;\n" + 
			"  rdf:type owl:DeprecatedClass ;\n" + 
			"  rdfs:label \"January\" ;\n" + 
			"  rdfs:subClassOf :DateTimeDescription ;\n" + 
			"  rdfs:subClassOf [\n" + 
			"      rdf:type owl:Restriction ;\n" + 
			"      owl:hasValue :unitMonth ;\n" + 
			"      owl:onProperty :unitType ;\n" + 
			"    ] ;\n" + 
			"  rdfs:subClassOf [\n" + 
			"      rdf:type owl:Restriction ;\n" + 
			"      owl:hasValue \"--01\" ;\n" + 
			"      owl:onProperty :month ;\n" + 
			"    ] ;\n" + 
			"  owl:deprecated \"true\"^^xsd:boolean ;\n" + 
			"  skos:historyNote \"This class was present in the 2006 version of OWL-Time. It was presented as an example of how DateTimeDescription could be specialized, but does not belong in the revised ontology. \" ;\n" + 
			".";
	
	private static final String HAS_VALUE_OWL_FRAGMENT_WITHOUT_HAS_VALUE = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ." +
			":January\n" + 
			"  rdf:type owl:Class ;\n" + 
			"  rdf:type owl:DeprecatedClass ;\n" + 
			"  rdfs:label \"January\" ;\n" + 
			"  rdfs:subClassOf :DateTimeDescription ;\n" + 
			"  rdfs:subClassOf [\n" + 
			"      rdf:type owl:Restriction ;\n" + 
			"      owl:onProperty :unitType ;\n" + 
			"    ] ;\n" + 
			"  rdfs:subClassOf [\n" + 
			"      rdf:type owl:Restriction ;\n" + 
			"      owl:onProperty :month ;\n" + 
			"    ] ;\n" + 
			"  owl:deprecated \"true\"^^xsd:boolean ;\n" + 
			"  skos:historyNote \"This class was present in the 2006 version of OWL-Time. It was presented as an example of how DateTimeDescription could be specialized, but does not belong in the revised ontology. \" ;\n" + 
			".";
	private static final String SH_HAS_VALUE = "http://www.w3.org/ns/shacl#hasValue";
	
	@Test
	public void hasValueWithLiteral() {
		ShaclFromOwl sharper = new OwlShaper();
		Model shapes =  sharper.fromOwl(HAS_VALUE_OWL_FRAGMENT_WITH_HAS_VALUE, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_HAS_VALUE), ResourceFactory.createPlainLiteral("--01"));
		Assert.assertTrue(condition);
	}
	
	@Test
	public void hasValueWithUri() {
		ShaclFromOwl sharper = new OwlShaper();
		Model shapes =  sharper.fromOwl(HAS_VALUE_OWL_FRAGMENT_WITH_HAS_VALUE, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_HAS_VALUE), ResourceFactory.createResource("http://www.w3.org/2006/time#unitMonth"));
		Assert.assertTrue(condition);
	}
	
	@Test
	public void hasValueMissing() {
		ShaclFromOwl sharper = new OwlShaper();
		Model shapes =  sharper.fromOwl(HAS_VALUE_OWL_FRAGMENT_WITHOUT_HAS_VALUE, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_HAS_VALUE), (RDFNode) null);
		Assert.assertFalse(condition);
	}
	
	
}
