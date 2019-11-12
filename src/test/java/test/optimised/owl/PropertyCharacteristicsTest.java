package test.optimised.owl;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Test;

import astrea.generators.OptimisedOwlGenerator;
import astrea.model.ShaclFromOwl;

public class PropertyCharacteristicsTest {
	
	private static final String HAS_VALUE_OWL_FRAGMENT_WITH_FUNCTIONAL_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ." +
			":hasTRS\n" + 
			"  rdf:type owl:FunctionalProperty ;\n" + 
			"  rdf:type owl:ObjectProperty ;\n" + 
			"  rdfs:comment \"The temporal reference system used by a temporal position or extent description. \"@en ;\n" + 
			"  rdfs:domain [\n" + 
			"      rdf:type owl:Class ;\n" + 
			"      owl:unionOf (\n" + 
			"          :TemporalPosition\n" + 
			"          :GeneralDurationDescription\n" + 
			"        ) ;\n" + 
			"    ] ;\n" + 
			"  rdfs:label \"Temporal reference system used\"@en ;\n" + 
			"  rdfs:range :TRS ;\n" + 
			"  skos:definition \"The temporal reference system used by a temporal position or extent description. \"@en ;\n" + 
			".";
	private static final String HAS_VALUE_OWL_FRAGMENT_WITHOUT_FUNCTIONAL_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ." +
			":hasTRS\n" + 
			// "  rdf:type owl:FunctionalProperty ;\n" + Missing line
			"  rdf:type owl:ObjectProperty ;\n" + 
			"  rdfs:comment \"The temporal reference system used by a temporal position or extent description. \"@en ;\n" + 
			"  rdfs:domain [\n" + 
			"      rdf:type owl:Class ;\n" + 
			"      owl:unionOf (\n" + 
			"          :TemporalPosition\n" + 
			"          :GeneralDurationDescription\n" + 
			"        ) ;\n" + 
			"    ] ;\n" + 
			"  rdfs:label \"Temporal reference system used\"@en ;\n" + 
			"  rdfs:range :TRS ;\n" + 
			"  skos:definition \"The temporal reference system used by a temporal position or extent description. \"@en ;\n" + 
			".";
	
	private static final String HAS_VALUE_OWL_FRAGMENT_WITH_INVERSE_FUNCTIONAL_PROPERTY_OP = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ." +
			"@prefix foaf: <http://xmlns.com/foaf/0.1/> .\n" +
			"foaf:mbox\n" + 
			"  a rdf:Property, owl:InverseFunctionalProperty, owl:ObjectProperty ;\n" + 
			"  foaf:term_status \"stable\" ;\n" + 
			"  rdfs:label \"personal mailbox\" ;\n" + 
			"  rdfs:comment \"A  personal mailbox, ie. an Internet mailbox associated with exactly one owner, the first owner of this mailbox. This is a 'static inverse functional property', in that  there is (across time and change) at most one individual that ever has any particular value for foaf:mbox.\" ;\n" + 
			"  rdfs:domain foaf:Agent ;\n" + 
			"  rdfs:range owl:Thing ;\n" + 
			"  rdfs:isDefinedBy foaf: .\n";
	private static final String HAS_VALUE_OWL_FRAGMENT_WITH_INVERSE_FUNCTIONAL_PROPERTY_DP = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ." +
			"@prefix foaf: <http://xmlns.com/foaf/0.1/> .\n" +
			"foaf:mbox_sha1sum\n" + 
			"  a rdf:Property, owl:InverseFunctionalProperty, owl:DatatypeProperty ;\n" + 
			"  foaf:term_status \"testing\" ;\n" + 
			"  rdfs:label \"sha1sum of a personal mailbox URI name\" ;\n" + 
			"  rdfs:comment \"The sha1sum of the URI of an Internet mailbox associated with exactly one owner, the  first owner of the mailbox.\" ;\n" + 
			"  rdfs:domain foaf:Agent ;\n" + 
			"  rdfs:range rdfs:Literal ;\n" + 
			"  rdfs:isDefinedBy foaf: .";
	
	
	
	private static final String SH_MAX_COUNT = "http://www.w3.org/ns/shacl#maxCount";
	private static final String SH_INVERSE_PATH = "http://www.w3.org/ns/shacl#inversePath";
	private static final String SH_PATH = "http://www.w3.org/ns/shacl#path";
	
	@Test
	public void hasShMaxCount() {
		ShaclFromOwl sharper = new OptimisedOwlGenerator();
		Model shapes =  sharper.fromOwl(HAS_VALUE_OWL_FRAGMENT_WITH_FUNCTIONAL_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_MAX_COUNT), ResourceFactory.createTypedLiteral(1));
		Assert.assertTrue(condition);
	}
	
	@Test
	public void hasNotShMaxCount() {
		ShaclFromOwl sharper = new OptimisedOwlGenerator();
		Model shapes =  sharper.fromOwl(HAS_VALUE_OWL_FRAGMENT_WITHOUT_FUNCTIONAL_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_MAX_COUNT), ResourceFactory.createTypedLiteral(1));
		Assert.assertFalse(condition);
	}
	
	
	@Test
	public void hasInverseFunctionalWithOP() {
		ShaclFromOwl sharper = new OptimisedOwlGenerator();
		Model shapes =  sharper.fromOwl(HAS_VALUE_OWL_FRAGMENT_WITH_INVERSE_FUNCTIONAL_PROPERTY_OP, "TURTLE");		
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_MAX_COUNT), ResourceFactory.createTypedLiteral(1));
		condition &= shapes.contains(null, ResourceFactory.createProperty(SH_INVERSE_PATH), ResourceFactory.createResource("http://xmlns.com/foaf/0.1/mbox"));
		condition &= !shapes.contains(null, ResourceFactory.createProperty(SH_PATH), (RDFNode) null);
		Assert.assertTrue(condition);
	}
	
	@Test
	public void hasInverseFunctionalWithDP() {
		ShaclFromOwl sharper = new OptimisedOwlGenerator();
		Model shapes =  sharper.fromOwl(HAS_VALUE_OWL_FRAGMENT_WITH_INVERSE_FUNCTIONAL_PROPERTY_DP, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_MAX_COUNT), ResourceFactory.createTypedLiteral(1));
		condition &= shapes.contains(null, ResourceFactory.createProperty(SH_INVERSE_PATH), ResourceFactory.createResource("http://xmlns.com/foaf/0.1/mbox_sha1sum"));
		condition &= !shapes.contains(null, ResourceFactory.createProperty(SH_PATH), (RDFNode) null);
		Assert.assertTrue(condition);
	}
}
