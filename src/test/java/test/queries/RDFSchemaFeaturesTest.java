package test.queries;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;
import org.junit.Assert;
import org.junit.Test;

import astrea.model.ShaclFromOwl;
import sharper.generators.OwlGenerator;

public class RDFSchemaFeaturesTest {

	// Extracted from: https://www.w3.org/2006/time#s
		public static final String OWL_FRAGMENT_OF_AN_OWL_CLASS = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
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
		
		public static final String OWL_FRAGMENT_OF_AN_RDFS_CLASS = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
				"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
				"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
				"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
				"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
				"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
				"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ." +
				":Duration\n" + 
				"  rdf:type rdfs:Class ;\n" + 
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
		
		public static final String OWL_FRAGMENT_OF_A_DATATYPE_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
				"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
				"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
				"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
				"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
				"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
				"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
				":generalDay\n" +
	            "  rdf:type rdfs:Datatype ;\n" +
	            "  rdfs:comment \"\"\"Day of month - formulated as a text string with a pattern constraint to reproduce the same lexical form as gDay, except that values up to 99 are permitted, in order to support calendars with more than 31 days in a month. \n" +
	            "Note that the value-space is not defined, so a generic OWL2 processor cannot compute ordering relationships of values of this type.\"\"\"@en ;\n" +
	            "  rdfs:label \"Generalized day\"@en ;\n" +
	            "  owl:onDatatype xsd:string ;\n" +
	            "  owl:withRestrictions (\n" +
	            "      [\n" +
	            "        xsd:pattern \"---(0[1-9]|[1-9][0-9])(Z|(\\\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?\" ;\n" +
	            "      ]\n" +
	            "    ) ;\n" +
	            "  skos:definition \"\"\"Day of month - formulated as a text string with a pattern constraint to reproduce the same lexical form as gDay, except that values up to 99 are permitted, in order to support calendars with more than 31 days in a month. \n" +
	            "Note that the value-space is not defined, so a generic OWL2 processor cannot compute ordering relationships of values of this type.\"\"\"@en ;\n" +
	            ".";
		
		private static final String SH_NODE_SHAPE = "http://www.w3.org/ns/shacl#NodeShape";
		private static final String SH_PROPERTY_SHAPE = "http://www.w3.org/ns/shacl#PropertyShape";
		private static final String SH_DATATYPE = "http://www.w3.org/ns/shacl#datatype";
		private static final String SH_CLASS = "http://www.w3.org/ns/shacl#class";
		
	// Testing creation of sh:NodeShape from owl:Class
	
	@Test
	public void createNodeShapeFromOwlClass() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_OF_AN_OWL_CLASS, "TURTLE");
		Boolean condition = shapes.contains(null, RDF.type, ResourceFactory.createResource(SH_NODE_SHAPE));
		Assert.assertTrue(condition);
	}
	

	@Test
	public void createNodeShapeFromRdfsClass() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_OF_AN_RDFS_CLASS, "TURTLE");
		Boolean condition = shapes.contains(null, RDF.type, ResourceFactory.createResource(SH_NODE_SHAPE));
		Assert.assertTrue(condition);
	}
	
	// Testing creation of sh:PropertyShape from owl:DatatypeProperty, rdf:Property and rdfs:Datatype
	
	@Test
	public void createPropertyShapeFromOwlDataProperty() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_OF_A_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, RDF.type, ResourceFactory.createResource(SH_PROPERTY_SHAPE));
		Assert.assertTrue(condition);
	}
		
		@Test
		public void createPropertyShapeFromRDFDataProperty() {
			ShaclFromOwl sharper = new OwlGenerator();
			Model shapes =  sharper.fromOwl(OWL_FRAGMENT_OF_RDF_DATA_PROPERTY, "TURTLE");
			Boolean condition = shapes.contains(null, RDF.type, ResourceFactory.createResource(SH_PROPERTY_SHAPE));
			Assert.assertTrue(condition);
		}
		
		@Test
		public void createPropertyShapeFromRDFDatatypeProperty() {
			ShaclFromOwl sharper = new OwlGenerator();
			Model shapes =  sharper.fromOwl(OWL_FRAGMENT_OF_A_DATATYPE_PROPERTY, "TURTLE");
			Boolean condition = shapes.contains(null, RDF.type, ResourceFactory.createResource(SH_PROPERTY_SHAPE));
			Assert.assertTrue(condition);
		}
	
	
	
	// Testing creation of sh:PropertyShape from owl:ObjectProperty and rdf:Property
	
	@Test
	public void createPropertyShapeFromObjectProperty() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_OF_A_OBJECT_PROPERTY, "TURTLE");

		Boolean condition = shapes.contains(null, RDF.type, ResourceFactory.createResource(SH_PROPERTY_SHAPE));
		Assert.assertTrue(condition);
	}
	
	@Test
	public void createPropertyShapeFromRDFObjectProperty() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_OF_RDF_OBJECT_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, RDF.type, ResourceFactory.createResource(SH_PROPERTY_SHAPE));
		Assert.assertTrue(condition);
	}
	
	
	
	// Testing that rdfs:range generates class in case of an ObjectProperty (owl:ObjectProperty and owl:DatatypedProperty)

	
	@Test
	public void createShNodeFromObjectProperty() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_OF_A_OBJECT_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_CLASS), ResourceFactory.createResource("http://www.w3.org/2006/time#MonthOfYear"));
		Assert.assertTrue(condition);
	}
	


	@Test
	public void createShDatatypeFromDataProperty() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_OF_A_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_DATATYPE), ResourceFactory.createResource("http://www.w3.org/2001/XMLSchema#nonNegativeInteger"));
		Assert.assertTrue(condition);
	}
	
	
	
}
