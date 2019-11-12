package test.queries;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;
import org.junit.Assert;
import org.junit.Test;

import astrea.generators.OwlGenerator;
import astrea.model.ShaclFromOwl;

public class PropertyRestrictionsTest {

	
	public static final String OWL_FRAGMENT_OF_OWL_CLASS = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
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
			"      owl:cardinality \"1\"^^xsd:nonNegativeInteger ;\n" + 
			"      owl:onProperty :unitType ;\n" + 
			"    ] ;\n" +
			" rdfs:subClassOf [\n" + 
			"      rdf:type owl:Restriction ;\n" + 
			"      owl:allValuesFrom xsd:gMonth ;\n" + 
			"      owl:onProperty :month ;\n" + 
			"    ] ;" + 
			"  skos:definition \"Duration of a temporal extent expressed as a number scaled by a temporal unit\"@en ;\n" + 
			"  skos:note \"Alternative to time:DurationDescription to support description of a temporal duration other than using a calendar/clock system.\"@en ;\n" + 
			".";
	
	
	private static final String SH_NODE_SHAPE = "http://www.w3.org/ns/shacl#NodeShape";
	private static final String SH_PROPERTY_SHAPE = "http://www.w3.org/ns/shacl#PropertyShape";
	private static final String SH_PATH = "http://www.w3.org/ns/shacl#path";
	
	
	@Test
	public void createEmbeddedPorpertyShapeFromOnProperty() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_OF_OWL_CLASS, "TURTLE");

		Boolean condition = shapes.contains(null, RDF.type, ResourceFactory.createResource(SH_NODE_SHAPE));
		condition &= shapes.contains(null, RDF.type, ResourceFactory.createResource(SH_PROPERTY_SHAPE));
		condition &= shapes.contains(null, ResourceFactory.createProperty(SH_PATH), ResourceFactory.createResource("http://www.w3.org/2006/time#numericDuration"));
		Assert.assertTrue(condition);
	}
	
	
	@Test
	public void createEmbeddedPorpertyShapeFromAllValuesFrom() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_OF_OWL_CLASS, "TURTLE");
		Boolean condition = shapes.contains(null, RDF.type, ResourceFactory.createResource(SH_NODE_SHAPE));
		condition &= shapes.contains(null, RDF.type, ResourceFactory.createResource(SH_PROPERTY_SHAPE));
		condition &= shapes.contains(null, ResourceFactory.createProperty(SH_PATH), ResourceFactory.createResource("http://www.w3.org/2006/time#numericDuration"));
		Assert.assertTrue(condition);
	}
}
