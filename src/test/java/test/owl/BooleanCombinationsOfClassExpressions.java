package test.owl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Test;

import astrea.model.ShaclFromOwl;
import sharper.generators.OwlShaper;

public class BooleanCombinationsOfClassExpressions {

	private static final String PREFIXES = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			"@prefix core: <http://iot.linkeddata.es/def/core#> .\n" ;
	
	public static final String OWL_FRAGMENT_UNION_OF_OP_1= PREFIXES+
			":unitType\n" + 
			"  rdf:type owl:ObjectProperty ;\n" + 
			"  rdfs:comment \"The temporal unit which provides the precision of a date-time value or scale of a temporal extent\"@en ;\n" + 
			"  rdfs:domain [\n" + 
			"      rdf:type owl:Class ;\n" + 
			"      owl:unionOf (\n" + 
			"          :GeneralDateTimeDescription\n" + 
			"          :Duration\n" + 
			"        ) ;\n" + 
			"    ] ;\n" + 
			"  rdfs:label \"temporal unit type\"@en ;\n" + 
			"  rdfs:range :TemporalUnit ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_UNION_OF_OP_2= PREFIXES +
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
	public static final String OWL_FRAGMENT_UNION_OF_OP_3= PREFIXES +
	"core:hasValue rdf:type owl:ObjectProperty ;\n" + 
	"          rdfs:domain [ rdf:type owl:Class ;\n" + 
	"                        owl:unionOf ( <http://iot.linkeddata.es/def/wot#InteractionPattern>\n" + 
	"                                      <http://iot.linkeddata.es/def/wot#Thing>\n" + 
	"                                    )\n" + 
	"                      ] ;\n" + 
	"          rdfs:range :Value ;\n" + 
	"          rdfs:comment \"Indicates a value provided by an interaction pattern or a Thing. In case one ore more values are indicated the order will be defined by the time stamp attributes.\"@en ;\n" + 
	"          rdfs:label \"has value\"@en .";
	
	public static final String OWL_FRAGMENT_UNION_OF_EMBEDDED_1 = PREFIXES +
			":TemporalEntity\n" + 
			"  rdf:type owl:Class ;\n" + 
			"  rdfs:comment \"A temporal interval or instant.\"@en ;\n" + 
			"  rdfs:label \"Temporal entity\"@en ;\n" + 
			"  rdfs:subClassOf owl:Thing ;\n" + 
			"  owl:unionOf (\n" + 
			"      :Instant\n" + 
			"      :Interval\n" + 
			"    ) ;\n" + 
			"  skos:definition \"A temporal interval or instant.\"@en ;\n" + 
			"."; 
	public static final String OWL_FRAGMENT_UNION_OF_EMBEDDED_2 = PREFIXES +
			":TimePosition\n" + 
			"  rdf:type owl:Class ;\n" + 
			"  rdfs:comment \"A temporal position described using either a (nominal) value from an ordinal reference system, or a (numeric) value in a temporal coordinate system. \"@en ;\n" + 
			"  rdfs:label \"Time position\"@en ;\n" + 
			"  rdfs:subClassOf :TemporalPosition ;\n" + 
			"  rdfs:subClassOf [\n" + 
			"      rdf:type owl:Class ;\n" + 
			"      owl:unionOf (\n" + 
			"          [\n" + 
			"            rdf:type owl:Restriction ;\n" + 
			"            owl:cardinality \"1\"^^xsd:nonNegativeInteger ;\n" + 
			"            owl:onProperty :numericPosition ;\n" + 
			"          ]\n" + 
			"          [\n" + 
			"            rdf:type owl:Restriction ;\n" + 
			"            owl:cardinality \"1\"^^xsd:nonNegativeInteger ;\n" + 
			"            owl:onProperty :nominalPosition ;\n" + 
			"          ]\n" + 
			"        ) ;\n" + 
			"    ] ;\n" + 
			"  skos:definition \"A temporal position described using either a (nominal) value from an ordinal reference system, or a (numeric) value in a temporal coordinate system. \"@en ;\n" + 
			"."; 
	
	public static final String OWL_FRAGMENT_UNION_OF_EMBEDDED_3 = PREFIXES +
			"<https://w3id.org/def/saref4bldg#BuildingSpace> rdf:type owl:Class ;\n" + 
			"                                                rdfs:subClassOf <http://www.opengis.net/ont/geosparql#Feature> ,\n" + 
			"                                                                [ rdf:type owl:Restriction ;\n" + 
			"                                                                  owl:onProperty <https://w3id.org/def/saref4bldg#contains> ;\n" + 
			"                                                                  owl:allValuesFrom <https://w3id.org/def/saref4bldg#PhysicalObject>\n" + 
			"                                                                ] ,\n" + 
			"                                                                [ rdf:type owl:Restriction ;\n" + 
			"                                                                  owl:onProperty <https://w3id.org/def/saref4bldg#hasSpace> ;\n" + 
			"                                                                  owl:allValuesFrom <https://w3id.org/def/saref4bldg#BuildingSpace>\n" + 
			"                                                                ] ,\n" + 
			"                                                                [ rdf:type owl:Restriction ;\n" + 
			"                                                                  owl:onProperty <https://w3id.org/def/saref4bldg#isSpaceOf> ;\n" + 
			"                                                                  owl:allValuesFrom [ rdf:type owl:Class ;\n" + 
			"                                                                                      owl:unionOf ( <https://w3id.org/def/saref4bldg#Building>\n" + 
			"                                                                                                    <https://w3id.org/def/saref4bldg#BuildingSpace>\n" + 
			"                                                                                                  )\n" + 
			"                                                                                    ]\n" + 
			"                                                                ] ;\n" + 
			"                                                rdfs:comment \"An entity used to define the physical spaces of the building. A building space contains devices or building objects.\"@en ;\n" + 
			"                                                rdfs:isDefinedBy <https://w3id.org/def/saref4bldg#> ;\n" + 
			"                                                rdfs:label \"Building space\"@en .";
	
	
	private static final String SH_OR= "http://www.w3.org/ns/shacl#or";
	@Test
	public void hasUnionOfWithOP1() {
		ShaclFromOwl sharper = new OwlShaper();
		String fragmentStr = OWL_FRAGMENT_UNION_OF_OP_1;
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_UNION_OF_OP_1, "TURTLE");		
		Model fragment = ModelFactory.createDefaultModel();
		 InputStream is = new ByteArrayInputStream( fragmentStr.getBytes() );
		fragment.read(is, null, "TURTLE"); 
		//fragment.listStatements().toList().forEach(elem -> System.out.println(elem.asTriple().toString()));
		
		System.out.println("\n\n----------");
		shapes.write(System.out, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_OR), ResourceFactory.createTypedLiteral(1));
		
		Assert.assertTrue(condition);
	}
	
	

	
}
