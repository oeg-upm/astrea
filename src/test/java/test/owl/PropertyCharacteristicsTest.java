package test.owl;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Test;

import astrea.model.ShaclFromOwl;
import sharper.generators.OwlShaper;

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
	private static final String SH_MAX_COUNT = "http://www.w3.org/ns/shacl#maxCount";
	
	@Test
	public void hasShMaxCount() {
		ShaclFromOwl sharper = new OwlShaper();
		Model shapes =  sharper.fromOwl(HAS_VALUE_OWL_FRAGMENT_WITH_FUNCTIONAL_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_MAX_COUNT), ResourceFactory.createTypedLiteral(1));
		Assert.assertTrue(condition);
	}
	
	@Test
	public void hasNotShMaxCount() {
		ShaclFromOwl sharper = new OwlShaper();
		Model shapes =  sharper.fromOwl(HAS_VALUE_OWL_FRAGMENT_WITHOUT_FUNCTIONAL_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_MAX_COUNT), ResourceFactory.createTypedLiteral(1));
		Assert.assertFalse(condition);
	}
}
