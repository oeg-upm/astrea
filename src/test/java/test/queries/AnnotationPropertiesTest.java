package test.queries;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDFS;
import org.junit.Assert;
import org.junit.Test;

import astrea.generators.OwlGenerator;
import astrea.model.ShaclFromOwl;

public class AnnotationPropertiesTest {

	
	public static final String ANNOTATION_PROPERTIES_OWL_FRAGMENT_FOR_NODESHAPES ="@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ."+
			"@prefix : <https://w3id.org/def/openadr#> ." +
			":VEN rdf:type owl:Class ;\n" + 
			"     rdfs:subClassOf [ rdf:type owl:Restriction ;\n" + 
			"                       owl:onProperty :manages ;\n" + 
			"                       owl:someValuesFrom :Resource\n" + 
			"                     ] ;\n" + 
			"     rdfs:comment \"This is the OpenADR Virtual End Node that is used to interact with the VTN\" ;\n" + 
			"     rdfs:isDefinedBy \"OpenADR 2.0 Demand Response Program Implementation Guide\" ;\n" + 
			"     rdfs:seeAlso \"See also the online Demand Response Program Implementation Guide (OpenADR 2.0)\" ;\n" + 
			"     rdfs:label \"Virtual End Node (VEN)\" .";
	public static final String ANNOTATION_PROPERTIES_OWL_FRAGMENT_FOR_PROPERTYSHAPES_OBJECT_PROPERTIES ="@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ."+
			"@prefix : <https://w3id.org/def/openadr#> ." +
			"\n" + 
			":hasTemporalDuration\n" + 
			"  rdf:type owl:ObjectProperty ;\n" + 
			"  rdfs:comment \"Duration of a temporal entity.\" ;\n" + 
			"  rdfs:domain :TemporalEntity ;\n" + 
			"  rdfs:label \"has temporal duration\" ;\n" + 
			"  rdfs:range :TemporalDuration ;\n" + 
			"  rdfs:seeAlso \"See also the online documentation\" ;\n" + 
			"  rdfs:isDefinedBy \"Duration of a temporal entity.\" ;";
	
	public static final String ANNOTATION_PROPERTIES_OWL_FRAGMENT_FOR_PROPERTYSHAPES_DATA_PROPERTIES ="@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ."+
			"@prefix : <https://w3id.org/def/openadr#> ." +
			"\n" + 
			":hasXSDDuration\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:comment \"Extent of a temporal entity, expressed using xsd:duration\" ;\n" + 
			"  rdfs:domain :TemporalEntity ;\n" + 
			"  rdfs:label \"has XSD duration\" ;\n" + 
			"  rdfs:range xsd:duration ;\n" + 
			"  rdfs:isDefinedBy \"Extent of a temporal entity, expressed using xsd:duration\" ;\n" + 
			"  rdfs:seeAlso \"Feature at risk - added in 2017 revision, and not yet widely used.\" ;\n" + 
			".";
	

	
	public static final String SH_NAME = "http://www.w3.org/ns/shacl#name";
	public static final Property RDFS_LABEL = RDFS.label;
	public static final Property RDFS_SEE_ALSO = RDFS.seeAlso;
	public static final Property RDFS_DEFINED_BY = RDFS.isDefinedBy;

	// -- Tests for NodeShapes
	@Test
	public void compliantWithShNameNodeShape() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(ANNOTATION_PROPERTIES_OWL_FRAGMENT_FOR_NODESHAPES, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_NAME), ResourceFactory.createTypedLiteral("Virtual End Node (VEN)"));
		Assert.assertTrue(condition);
	}
	
	@Test
	public void compliantWithRfsLabelAndRdfsCommentNodeShape() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(ANNOTATION_PROPERTIES_OWL_FRAGMENT_FOR_NODESHAPES, "TURTLE");
		Boolean condition = shapes.contains(null, RDFS_LABEL, ResourceFactory.createTypedLiteral("Virtual End Node (VEN)"));
		condition &= shapes.contains(null, RDFS_LABEL, ResourceFactory.createTypedLiteral("This is the OpenADR Virtual End Node that is used to interact with the VTN"));
		Assert.assertTrue(condition);
	}
	
	@Test
	public void compliantWithRfsIsDefinedByNodeShape() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(ANNOTATION_PROPERTIES_OWL_FRAGMENT_FOR_NODESHAPES, "TURTLE");
		Boolean condition = shapes.contains(null, RDFS_DEFINED_BY, ResourceFactory.createTypedLiteral("OpenADR 2.0 Demand Response Program Implementation Guide"));
		Assert.assertTrue(condition);
	}
	
	@Test
	public void compliantWithRfsSeeAlsoNodeShape() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(ANNOTATION_PROPERTIES_OWL_FRAGMENT_FOR_NODESHAPES, "TURTLE");
		Boolean condition = shapes.contains(null, RDFS_SEE_ALSO, ResourceFactory.createTypedLiteral("See also the online Demand Response Program Implementation Guide (OpenADR 2.0)"));
		Assert.assertTrue(condition);
	}
	
	// -- Tests for property shapes from object properties
	@Test
	public void compliantWithShNamePropertyShapesFromObjectProperties() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(ANNOTATION_PROPERTIES_OWL_FRAGMENT_FOR_PROPERTYSHAPES_OBJECT_PROPERTIES, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_NAME), ResourceFactory.createTypedLiteral("has temporal duration"));
		Assert.assertTrue(condition);
	}
	
	@Test
	public void compliantWithRfsLabelAndRdfsCommentPropertyShapeFromObjectProperties() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(ANNOTATION_PROPERTIES_OWL_FRAGMENT_FOR_PROPERTYSHAPES_OBJECT_PROPERTIES, "TURTLE");
		Boolean condition = shapes.contains(null, RDFS_LABEL, ResourceFactory.createTypedLiteral("has temporal duration"));
		condition &= shapes.contains(null, RDFS_LABEL, ResourceFactory.createTypedLiteral("Duration of a temporal entity."));
		Assert.assertTrue(condition);
	}
	
	@Test
	public void compliantWithRfsIsDefinedByPropertyShapeFromObjectProperties() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(ANNOTATION_PROPERTIES_OWL_FRAGMENT_FOR_PROPERTYSHAPES_OBJECT_PROPERTIES, "TURTLE");
		
		Boolean condition = shapes.contains(null, RDFS_DEFINED_BY, ResourceFactory.createTypedLiteral("Duration of a temporal entity."));
		Assert.assertTrue(condition);
	}
	
	@Test
	public void compliantWithRfsSeeAlsoPropertyShapeFromObjectProperties() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(ANNOTATION_PROPERTIES_OWL_FRAGMENT_FOR_PROPERTYSHAPES_OBJECT_PROPERTIES, "TURTLE");
		Boolean condition = shapes.contains(null, RDFS_SEE_ALSO, ResourceFactory.createTypedLiteral("See also the online documentation"));
		Assert.assertTrue(condition);
	}
	
	// -- Tests for property shapes from data properties
	@Test
	public void compliantWithShNamePropertyShapesFromDataProperties() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(ANNOTATION_PROPERTIES_OWL_FRAGMENT_FOR_PROPERTYSHAPES_DATA_PROPERTIES, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_NAME), ResourceFactory.createTypedLiteral("has XSD duration"));
		Assert.assertTrue(condition);
	}
	
	@Test
	public void compliantWithRfsLabelAndRdfsCommentPropertyShapeFromDataProperties() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(ANNOTATION_PROPERTIES_OWL_FRAGMENT_FOR_PROPERTYSHAPES_DATA_PROPERTIES, "TURTLE");
		Boolean condition = shapes.contains(null, RDFS_LABEL, ResourceFactory.createTypedLiteral("has XSD duration"));
		condition &= shapes.contains(null, RDFS_LABEL, ResourceFactory.createTypedLiteral("Extent of a temporal entity, expressed using xsd:duration"));
		Assert.assertTrue(condition);
	}
	
	@Test
	public void compliantWithRfsIsDefinedByPropertyShapeFromDataProperties() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(ANNOTATION_PROPERTIES_OWL_FRAGMENT_FOR_PROPERTYSHAPES_DATA_PROPERTIES, "TURTLE");
		Boolean condition = shapes.contains(null, RDFS_DEFINED_BY, ResourceFactory.createTypedLiteral("Extent of a temporal entity, expressed using xsd:duration"));
		Assert.assertTrue(condition);
	}
	
	@Test
	public void compliantWithRfsSeeAlsoPropertyShapeFromDataProperties() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(ANNOTATION_PROPERTIES_OWL_FRAGMENT_FOR_PROPERTYSHAPES_DATA_PROPERTIES, "TURTLE");
		Boolean condition = shapes.contains(null, RDFS_SEE_ALSO, ResourceFactory.createTypedLiteral("Feature at risk - added in 2017 revision, and not yet widely used."));
		Assert.assertTrue(condition);
	}
	

	
	
}
