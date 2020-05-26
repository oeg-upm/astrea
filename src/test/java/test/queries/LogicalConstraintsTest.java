package test.queries;

import astrea.generators.OwlGenerator;
import astrea.model.ShaclFromOwl;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;
import org.junit.Assert;
import org.junit.Test;

public class LogicalConstraintsTest {


    public static final String OWL_FRAGMENT_AND = "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n"+
            "<http://njh.me/#WhiteBurgundy>\n" +
            "  a owl:Class ;\n" +
            "  owl:intersectionOf (\n" +
            "   <http://njh.me/#Burgundy>\n" +
            "   <http://njh.me/#WhiteWine>\n" +
            " ) .";

    public static final String OWL_FRAGMENT_NOT = "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix dc11: <http://purl.org/dc/elements/1.1/> .\n" +
            "@prefix dc: <http://purl.org/dc/terms/> .\n" +
            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> ."+
            "<http://www.co-ode.org/ontologies/pizza/pizza.owl#NonConsumableThing>\n" +
            "  a owl:Class ;\n" +
            "  owl:complementOf <http://www.co-ode.org/ontologies/pizza/pizza.owl#ConsumableThing> .";

    public static final String OWL_FRAGMENT_NOT_PROP = "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix dc11: <http://purl.org/dc/elements/1.1/> .\n" +
            "@prefix dc: <http://purl.org/dc/terms/> .\n" +
            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> ."+
            "<http://www.co-ode.org/ontologies/pizza/pizza.owl#Wine>\n" +
            "  a owl:Class ;\n" +
            "  owl:complementOf [\n" +
            "    a owl:Restriction ;\n" +
            "    owl:onProperty <http://www.co-ode.org/ontologies/pizza/pizza.owl#locatedIn> ;\n" +
            "    owl:hasValue <http://www.co-ode.org/ontologies/pizza/pizza.owl#FrenchRegion>\n" +
            "  ] .";

    public static final String OWL_FRAGMENT_OR = "@prefix : <http://www.w3.org/2006/time#> .\n" +
            "@prefix dct: <http://purl.org/dc/terms/> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" +
            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ."+
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
    
    public static final String OWL_FRAGMENT_OR_2 = "@prefix : <http://www.w3.org/2006/time#> .\n" +
            "@prefix dct: <http://purl.org/dc/terms/> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" +
            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ."+
            "<https://w3id.org/def/saref4bldg#BuildingSpace> rdf:type owl:Class ;\n" + 
            "  rdfs:subClassOf [ rdf:type owl:Restriction ;\n" + 
            "    owl:onProperty <https://w3id.org/def/saref4bldg#isSpaceOf> ;\n" + 
            "    owl:allValuesFrom [ rdf:type owl:Class ;\n" + 
            "      owl:unionOf ( <https://w3id.org/def/saref4bldg#Building> <https://w3id.org/def/saref4bldg#BuildingSpace> )\n" + 
            "  ]\n" + 
            "] ;";

    public static final String OWL_FRAGMENT_OR_3 = "@prefix : <http://www.w3.org/2006/time#> .\n" +
            "@prefix dct: <http://purl.org/dc/terms/> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" +
            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ."+
		    ":hasValue rdf:type owl:ObjectProperty ;\n" + 
		    "          rdfs:domain [ rdf:type owl:Class ;\n" + 
		    "                        owl:unionOf ( <http://iot.linkeddata.es/def/wot#InteractionPattern>\n" + 
		    "                                      <http://iot.linkeddata.es/def/wot#Thing>\n" + 
		    "                                    )\n" + 
		    "                      ] ;"+
		    "          rdfs:range :Value ;\n" + 
		    "          rdfs:comment \"Indicates a value provided by an interaction pattern or a Thing. In case one ore more values are indicated the order will be defined by the time stamp attributes.\"@en ;\n" + 
		    "          rdfs:label \"has value\"@en .";
    
    public static final String OWL_FRAGMENT_OR_4 = "@prefix : <http://www.w3.org/2006/time#> .\n" +
            "@prefix dct: <http://purl.org/dc/terms/> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" +
            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ."+
		    ":hasValue rdf:type owl:ObjectProperty ;\n" + 
		    "          rdfs:domain :Value; \n"+
		    "          rdfs:range [ rdf:type owl:Class ;\n" + 
		    "                        owl:unionOf ( <http://iot.linkeddata.es/def/wot#InteractionPattern>\n" + 
		    "                                      <http://iot.linkeddata.es/def/wot#Thing>\n" + 
		    "                                    )\n" + 
		    "                      ] ;"+
		    "          rdfs:comment \"Indicates a value provided by an interaction pattern or a Thing. In case one ore more values are indicated the order will be defined by the time stamp attributes.\"@en ;\n" + 
		    "          rdfs:label \"has value\"@en .";
    
    public static final String OWL_FRAGMENT_DIJOIN = "@prefix : <http://www.w3.org/2006/time#> .\n" +
            "@prefix dct: <http://purl.org/dc/terms/> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" +
            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ."+
            ""+
		    ":intervalEquals\n" + 
		    "  rdf:type owl:ObjectProperty ;\n" + 
		    "  rdfs:comment \"If a proper interval T1 is intervalEquals another proper interval T2, then the beginning of T1 is coincident with the beginning of T2, and the end of T1 is coincident with the end of T2.\"@en ;\n" + 
		    "  rdfs:domain :ProperInterval ;\n" + 
		    "  rdfs:label \"interval equals\"@en ;\n" + 
		    "  rdfs:range :ProperInterval ;\n" + 
		    "  owl:propertyDisjointWith :intervalIn ;\n" + 
		    "  skos:definition \"If a proper interval T1 is intervalEquals another proper interval T2, then the beginning of T1 is coincident with the beginning of T2, and the end of T1 is coincident with the end of T2.\"@en ;";
  
    public static final String OWL_FRAGMENT_DIJOIN_NO_DOMAIN = "@prefix : <http://www.w3.org/2006/time#> .\n" +
            "@prefix dct: <http://purl.org/dc/terms/> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" +
            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ."+
			""+
			":intervalEquals\n" + 
			"  rdf:type owl:ObjectProperty ;\n" + 
			"  rdfs:comment \"If a proper interval T1 is intervalEquals another proper interval T2, then the beginning of T1 is coincident with the beginning of T2, and the end of T1 is coincident with the end of T2.\"@en ;\n" + 
			"  rdfs:label \"interval equals\"@en ;\n" + 
			"  rdfs:range :ProperInterval ;\n" + 
			"  owl:propertyDisjointWith :intervalIn ;\n" + 
			"  skos:definition \"If a proper interval T1 is intervalEquals another proper interval T2, then the beginning of T1 is coincident with the beginning of T2, and the end of T1 is coincident with the end of T2.\"@en ;";
	
    public static final String OWL_FRAGMENT_DIJOIN_NO_RANGE = "@prefix : <http://www.w3.org/2006/time#> .\n" +
            "@prefix dct: <http://purl.org/dc/terms/> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" +
            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ."+
			""+
			":intervalEquals\n" + 
			"  rdf:type owl:ObjectProperty ;\n" + 
			"  rdfs:comment \"If a proper interval T1 is intervalEquals another proper interval T2, then the beginning of T1 is coincident with the beginning of T2, and the end of T1 is coincident with the end of T2.\"@en ;\n" + 
			"  rdfs:label \"interval equals\"@en ;\n" + 
			"  rdfs:domain :ProperInterval ;\n" + 
			"  owl:propertyDisjointWith :intervalIn ;\n" + 
			"  skos:definition \"If a proper interval T1 is intervalEquals another proper interval T2, then the beginning of T1 is coincident with the beginning of T2, and the end of T1 is coincident with the end of T2.\"@en ;";
	
    
    public static final String OWL_FRAGMENT_DIJOIN_NO_DOMAIN_RANGE = "@prefix : <http://www.w3.org/2006/time#> .\n" +
            "@prefix dct: <http://purl.org/dc/terms/> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" +
            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ."+
			""+
			":intervalEquals\n" + 
			"  rdf:type owl:ObjectProperty ;\n" + 
			"  rdfs:comment \"If a proper interval T1 is intervalEquals another proper interval T2, then the beginning of T1 is coincident with the beginning of T2, and the end of T1 is coincident with the end of T2.\"@en ;\n" + 
			"  rdfs:label \"interval equals\"@en ;\n" + 
			"  owl:propertyDisjointWith :intervalIn ;\n" + 
			"  skos:definition \"If a proper interval T1 is intervalEquals another proper interval T2, then the beginning of T1 is coincident with the beginning of T2, and the end of T1 is coincident with the end of T2.\"@en ;";
			    
    public static final String OWL_FRAGMENT_DISJOINT_UNION = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ." +
			"@prefix : <http://iot.linkeddata.es/def/core#> ." +
			"" +
			"[ rdf:type owl:AllDisjointProperties ;\n" + 
			"  owl:members ( :P\n" + 
			"                :P1\n" + 
			"                :P2\n" + 
			"              )\n" + 
			"] .";
    
    
    public static final String OWL_FRAGMENT_EQUIVALENT = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ." +
			"@prefix : <http://iot.linkeddata.es/def/core#> ." +
			"" +
			"[ rdf:type owl:AllDisjointProperties ;\n" + 
			"  owl:members ( :P\n" + 
			"                :P1\n" + 
			"                :P2\n" + 
			"              )\n" + 
			"] .";
    
    private static final String SH_AND = "http://www.w3.org/ns/shacl#and";
    private static final String SH_NOT = "http://www.w3.org/ns/shacl#not";
    private static final String SH_PROPERTY_SHAPE = "http://www.w3.org/ns/shacl#PropertyShape";
    private static final String SH_NODE_SHAPE = "http://www.w3.org/ns/shacl#NodeShape";
    private static final String SH_PATH= "http://www.w3.org/ns/shacl#path";
    private static final String SH_OR= "http://www.w3.org/ns/shacl#or";
    private static final String SH_PROPERTY= "http://www.w3.org/ns/shacl#property";
    private static final String SH_TARGET_CLASS= "http://www.w3.org/ns/shacl#targetClass";
    private static final String SH_CLASS= "http://www.w3.org/ns/shacl#class";
    private static final String SH_DISJOINT= "http://www.w3.org/ns/shacl#disjoint";

    @Test
    public void compliantWithShAndShape() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_AND, "TURTLE");
       
        Boolean condition = shapes.containsResource(ResourceFactory.createResource("https://astrea.linkeddata.es/shapes#7f5634e9110aafe50c4d98fb2ed51c4b"));
        condition &= shapes.containsResource(ResourceFactory.createResource("https://astrea.linkeddata.es/shapes#bc2308dbde3337abf001d6b7d45e689f"));
        condition &= shapes.containsResource(ResourceFactory.createResource(SH_AND));

        Assert.assertTrue(condition);
    }

    @Test
    public void compliantWithShNotShape() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_NOT, "TURTLE");
        Boolean condition = shapes.contains(null,
                ResourceFactory.createProperty(SH_NOT), ResourceFactory.createResource("https://astrea.linkeddata.es/shapes#70549ffd180bcd03793a6da9016ffd42"));
        
        Assert.assertTrue(condition);
    }

    @Test
    public void compliantWithShNotPropertyShape() {
    		OwlGenerator sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_NOT_PROP, "TURTLE");
        Boolean condition = shapes.contains(null,ResourceFactory.createProperty(SH_NOT), (RDFNode) null);
        condition &= shapes.contains(null, RDF.type, ResourceFactory.createResource(SH_PROPERTY_SHAPE));
        condition &= shapes.contains(null, ResourceFactory.createProperty(SH_PATH), ResourceFactory.createResource("http://www.co-ode.org/ontologies/pizza/pizza.owl#locatedIn"));
        Assert.assertTrue(condition);
    }


    @Test
    public void compliantWithShOrShape() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_OR, "TURTLE");
        Boolean condition = shapes.containsResource(ResourceFactory.createResource("https://astrea.linkeddata.es/shapes#f1ed01ff4ad7e0110c28d4231cd9616c"));
        condition &= shapes.containsResource(ResourceFactory.createResource("https://astrea.linkeddata.es/shapes#1277b387effe1ea8b7cf6171d6155a1b"));
        condition &= shapes.containsResource(ResourceFactory.createResource(SH_OR));
        Assert.assertTrue(condition);
    }
    
    
    @Test
    public void compliantWithShOrShapeTwo() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_OR_2, "TURTLE");
        Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_CLASS), ResourceFactory.createProperty("https://w3id.org/def/saref4bldg#Building"));
        condition &= shapes.contains(null, ResourceFactory.createProperty(SH_CLASS), ResourceFactory.createProperty("https://w3id.org/def/saref4bldg#BuildingSpace"));
        condition &= shapes.contains(null, ResourceFactory.createProperty(SH_PATH), ResourceFactory.createProperty("https://w3id.org/def/saref4bldg#isSpaceOf"));
        condition &= shapes.containsResource(ResourceFactory.createResource(SH_OR));
        Assert.assertTrue(condition);
    }
    
    @Test
    public void compliantWithShOrShapeThree() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_OR_3, "TURTLE");
        
        Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_CLASS), ResourceFactory.createProperty("http://www.w3.org/2006/time#Value"));
        
        condition &= shapes.contains(ResourceFactory.createResource("https://astrea.linkeddata.es/shapes#74b49bc808dec30741e3e1eb1ed53d1a"), RDF.type, ResourceFactory.createProperty(SH_NODE_SHAPE));
        condition &= shapes.contains(ResourceFactory.createResource("https://astrea.linkeddata.es/shapes#74b49bc808dec30741e3e1eb1ed53d1a"), ResourceFactory.createProperty(SH_PROPERTY), ResourceFactory.createProperty("https://astrea.linkeddata.es/shapes#3a924da1a58e2af120f486a6606eb9ad"));
        condition &= shapes.contains(ResourceFactory.createResource("https://astrea.linkeddata.es/shapes#74b49bc808dec30741e3e1eb1ed53d1a"), ResourceFactory.createProperty(SH_TARGET_CLASS), ResourceFactory.createProperty("http://iot.linkeddata.es/def/wot#Thing"));
        
        condition &= shapes.contains(ResourceFactory.createResource("https://astrea.linkeddata.es/shapes#9729101e06843ef8782157f878647544"), RDF.type, ResourceFactory.createProperty(SH_NODE_SHAPE));
        condition &= shapes.contains(ResourceFactory.createResource("https://astrea.linkeddata.es/shapes#9729101e06843ef8782157f878647544"), ResourceFactory.createProperty(SH_PROPERTY), ResourceFactory.createProperty("https://astrea.linkeddata.es/shapes#3a924da1a58e2af120f486a6606eb9ad"));
        condition &= shapes.contains(ResourceFactory.createResource("https://astrea.linkeddata.es/shapes#9729101e06843ef8782157f878647544"), ResourceFactory.createProperty(SH_TARGET_CLASS), ResourceFactory.createProperty("http://iot.linkeddata.es/def/wot#InteractionPattern"));
        
        Assert.assertTrue(condition);
    }


    @Test
    public void compliantWithShOrShapeFour() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_OR_4, "TURTLE");
        
        Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_CLASS), ResourceFactory.createProperty("http://iot.linkeddata.es/def/wot#InteractionPattern"));
        condition &= shapes.contains(null, ResourceFactory.createProperty(SH_CLASS), ResourceFactory.createProperty("http://iot.linkeddata.es/def/wot#Thing"));
        condition &= shapes.contains(null, ResourceFactory.createProperty(SH_PATH), ResourceFactory.createProperty("http://www.w3.org/2006/time#hasValue"));

         
        Assert.assertTrue(condition);
    }
    
    
    @Test
    public void compliantWithShDISJOINT() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_DIJOIN, "TURTLE");
        Boolean condition = shapes.contains(ResourceFactory.createResource("https://astrea.linkeddata.es/shapes#a1ecba995aef4ada322156e17bc0998c"), ResourceFactory.createProperty(SH_DISJOINT), ResourceFactory.createProperty("http://www.w3.org/2006/time#intervalIn"));
        Assert.assertTrue(condition);
    }
    
    
    @Test
    public void compliantWithShDISJOINTNoDomain() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_DIJOIN_NO_DOMAIN, "TURTLE");
  
        Boolean condition = shapes.contains(ResourceFactory.createResource("https://astrea.linkeddata.es/shapes#a1ecba995aef4ada322156e17bc0998c"), ResourceFactory.createProperty(SH_DISJOINT), ResourceFactory.createProperty("http://www.w3.org/2006/time#intervalIn"));
        Assert.assertTrue(condition);
    }
    
    @Test
    public void compliantWithShDISJOINTNoRange() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_DIJOIN_NO_RANGE, "TURTLE");

        Boolean condition = shapes.contains(ResourceFactory.createResource("https://astrea.linkeddata.es/shapes#a1ecba995aef4ada322156e17bc0998c"), ResourceFactory.createProperty(SH_DISJOINT), ResourceFactory.createProperty("http://www.w3.org/2006/time#intervalIn"));
        Assert.assertTrue(condition);
    }
    
    @Test
    public void compliantWithShDISJOINTNoDomainNoRange() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_DIJOIN_NO_DOMAIN_RANGE, "TURTLE");

        Boolean condition = shapes.contains(ResourceFactory.createResource("https://astrea.linkeddata.es/shapes#a1ecba995aef4ada322156e17bc0998c"), ResourceFactory.createProperty(SH_DISJOINT), ResourceFactory.createProperty("http://www.w3.org/2006/time#intervalIn"));
        Assert.assertTrue(condition);
    }
    
    @Test
    public void compliantWithShDISJOINTUnion() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_DISJOINT_UNION, "TURTLE");
        Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_DISJOINT), ResourceFactory.createProperty("http://iot.linkeddata.es/def/core#P"));
        condition &= shapes.contains(null, ResourceFactory.createProperty(SH_DISJOINT), ResourceFactory.createProperty("http://iot.linkeddata.es/def/core#P1"));
        condition &= shapes.contains(null, ResourceFactory.createProperty(SH_DISJOINT), ResourceFactory.createProperty("http://iot.linkeddata.es/def/core#P2"));
        Assert.assertTrue(condition);
    }
    
    
    
}
