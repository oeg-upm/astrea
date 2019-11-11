package test.queries;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;
import org.junit.Assert;
import org.junit.Test;

import astrea.model.ShaclFromOwl;
import sharper.generators.OwlGenerator;

public class DatatypeTest {

	public static final String OWL_FRAGMENT_OF_A_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":minute\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:comment \"Minute position in a calendar-clock system.\"@en ;\n" + 
			"  rdfs:domain :GeneralDateTimeDescription ;\n" + 
			"  rdfs:label \"minute\"@en ;\n" + 
			"  rdfs:range xsd:nonNegativeInteger ;\n" + 
			"  skos:definition \"Minute position in a calendar-clock system.\"@en ;\n" + 
			".";
	public static final String OWL_FRAGMENT_OF_RDF_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":minute\n" + 
			"  rdf:type rdf:Property ;\n" + 
			"  rdfs:comment \"Minute position in a calendar-clock system.\"@en ;\n" + 
			"  rdfs:domain :GeneralDateTimeDescription ;\n" + 
			"  rdfs:label \"minute\"@en ;\n" + 
			"  rdfs:range xsd:nonNegativeInteger ;\n" + 
			"  skos:definition \"Minute position in a calendar-clock system.\"@en ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_OF_A_OBJECT_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":monthOfYear\n" + 
			"  rdf:type owl:ObjectProperty ;\n" + 
			"  rdfs:comment \"The month of the year, whose value is a member of the class time:MonthOfYear\"@en ;\n" + 
			"  rdfs:domain :GeneralDateTimeDescription ;\n" + 
			"  rdfs:label \"month of year\"@en ;\n" + 
			"  rdfs:range :MonthOfYear ;\n" + 
			"  skos:definition \"The month of the year, whose value is a member of the class time:MonthOfYear\"@en ;\n" + 
			"  skos:editorialNote \"Feature at risk - added in 2017 revision, and not yet widely used. \"@en ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_OF_RDF_OBJECT_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":monthOfYear\n" + 
			"  rdf:type rdf:Property ;\n" + 
			"  rdfs:comment \"The month of the year, whose value is a member of the class time:MonthOfYear\"@en ;\n" + 
			"  rdfs:domain :GeneralDateTimeDescription ;\n" + 
			"  rdfs:label \"month of year\"@en ;\n" + 
			"  rdfs:range :MonthOfYear ;\n" + 
			"  skos:definition \"The month of the year, whose value is a member of the class time:MonthOfYear\"@en ;\n" + 
			"  skos:editorialNote \"Feature at risk - added in 2017 revision, and not yet widely used. \"@en ;\n" + 
			".";
	
	
	private static final String SH_PROPERTY_SHAPE = "http://www.w3.org/ns/shacl#PropertyShape";
	
// Testing creation of sh:NodeShape from owl:Class

@Test
public void createNodeShapeFromOwlClass() {
	ShaclFromOwl sharper = new OwlGenerator();
	Model shapes =  sharper.fromOwl(OWL_FRAGMENT_OF_RDF_OBJECT_PROPERTY, "TURTLE");
	Boolean condition = shapes.contains(null, RDF.type, ResourceFactory.createResource(SH_PROPERTY_SHAPE));
	Assert.assertTrue(condition);
}

}
