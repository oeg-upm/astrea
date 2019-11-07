package test.owl.query;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Test;

import astrea.model.ShaclFromOwl;
import sharper.generators.OwlGenerator;

public class EquialityTest {

	public static final String ANNOTATION_PROPERTIES_OWL_FRAGMENT ="@prefix foaf: <http://xmlns.com/foaf/0.1/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix dc11: <http://purl.org/dc/elements/1.1/> .\n" + 
			"@prefix wot: <http://xmlns.com/wot/0.1/> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix ns0: <http://www.w3.org/2003/06/sw-vocab-status/ns#> .\n" + 
			"@prefix schema: <http://schema.org/> .\n" + 
			"@prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#> .\n" + 
			"@prefix dc: <http://purl.org/dc/terms/> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" +
			"\n" + 
			"foaf:maker\n" + 
			"  a rdf:Property, owl:ObjectProperty ;\n" + 
			"  ns0:term_status \"stable\" ;\n" + 
			"  rdfs:label \"maker\" ;\n" + 
			"  rdfs:comment \"An agent that  made this thing.\" ;\n" + 
			"  owl:equivalentProperty dc:creator ;\n" + 
			"  rdfs:domain owl:Thing ;\n" + 
			"  rdfs:range foaf:Agent ;\n" + 
			"  rdfs:isDefinedBy foaf: ;\n" + 
			"  owl:inverseOf foaf:made .";
	
	
	public static final String SH_EQUIVALENT = "http://www.w3.org/ns/shacl#equivalent";


	// -- Tests for NodeShapes
	@Test
	public void compliantWithShNameNodeShape() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(ANNOTATION_PROPERTIES_OWL_FRAGMENT, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_EQUIVALENT), ResourceFactory.createResource("http://purl.org/dc/terms/creator"));
		Assert.assertTrue(condition);
	}
	
	
}
