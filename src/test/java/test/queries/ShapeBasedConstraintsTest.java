package test.queries;

import astrea.generators.OwlGenerator;
import astrea.model.ShaclFromOwl;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;
import org.junit.Assert;
import org.junit.Test;

public class ShapeBasedConstraintsTest {
    // Extracted from     http://iot.linkeddata.es/def/core
    private static final String OWL_FRAGMENT_FOR_NODE = "@prefix : <http://iot.linkeddata.es/def/core#> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix xml: <http://www.w3.org/XML/1998/namespace> .\n" +
            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@base <http://iot.linkeddata.es/def/core#> ." +
            ":Actuator rdf:type owl:Class ;\n" +
            "          rdfs:subClassOf [ rdf:type owl:Restriction ;\n" +
            "                            owl:onProperty :actsOnFeatureProperty ;\n" +
            "                            owl:allValuesFrom :FeatureProperty\n" +
            "                          ] ;\n" +
            "          rdfs:comment \"An actuator is a device that accepts digital inputs and which acts on (changes) one or more properties of a physical entity on the basis of those inputs. An actuator is a specialization of an IoT device. An actuator acts on a physical entity. (definition taken from ISO/IEC 11404:2007)\"@en ;\n" +
            "          rdfs:label \"Actuator\"@en ;\n" +
            "          rdfs:seeAlso \"ISO/IEC CC 30141. Information technology - Internet of Things Reference architecutre (IoT RA)\"@en .\n" +
            "\n";

    // Extracted from    http://www.w3.org/2006/time#
    private static final String OWL_FRAGMENT_FOR_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix xml: <http://www.w3.org/XML/1998/namespace> .\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" +
            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@base <http://iot.linkeddata.es/def/core#> ." +
            ":DateTimeDescription\n" +
            "  rdf:type owl:Class ;\n" +
            "  rdfs:comment \"Description of date and time structured with separate values for the various elements of a calendar-clock system. The temporal reference system is fixed to Gregorian Calendar, and the range of year, month, day properties restricted to corresponding XML Schema types xsd:gYear, xsd:gMonth and xsd:gDay, respectively.\"@en ;\n" +
            "  rdfs:label \"Date-Time description\"@en ;\n" +
            "  rdfs:subClassOf [\n" +
            "      rdf:type owl:Restriction ;\n" +
            "      owl:allValuesFrom xsd:gDay ;\n" +
            "      owl:onProperty :day ;\n" +
            "    ] ;\n" +
            "  skos:definition \"Description of date and time structured with separate values for the various elements of a calendar-clock system. The temporal reference system is fixed to Gregorian Calendar, and the range of year, month, day properties restricted to corresponding XML Schema types xsd:gYear, xsd:gMonth and xsd:gDay, respectively.\"@en ;\n" +
            ".";


    private static final String OWL_FRAGMENT_FOR_QUALIFIED_CARDINALITY="@prefix : <http://iot.linkeddata.es/def/core#> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix xml: <http://www.w3.org/XML/1998/namespace> .\n" +
            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@base <http://iot.linkeddata.es/def/core#> .\n"+
            ":Contract rdf:type owl:Class ;\n" +
            "          rdfs:subClassOf  [ rdf:type owl:Restriction ;\n" +
            "                            owl:onProperty :requestedService ;\n" +
            "                            owl:maxQualifiedCardinality \"1\" ;\n" +
            "                            owl:onClass :Service\n" +
            "                          ], \n" +
            "                          [ rdf:type owl:Restriction ;\n" +
            "                            owl:onProperty :serviceOwner ;\n" +
            "                            owl:minQualifiedCardinality \"1\" ;\n" +
            "                            owl:onClass :Agent\n" +
            "                          ] ,\n" +
            "                          [ rdf:type owl:Restriction ;\n" +
            "                            owl:onProperty :servicePetitioner ;\n" +
            "                            owl:qualifiedCardinality \"1\" ;\n" +
            "                            owl:onClass :Agent\n" +
            "                          ] ;"+
            "          rdfs:comment \"Contract between organizations and requested services.\"@en ;\n" +
            "          rdfs:label \"Contract\"@en .";

    private static final String SH_NODE_SHAPE = "http://www.w3.org/ns/shacl#NodeShape";
    private static final String SH_NODE = "http://www.w3.org/ns/shacl#node";
    private static final String SH_PROPERTY = "http://www.w3.org/ns/shacl#property";
    private static final String SH_PATH = "http://www.w3.org/ns/shacl#path";
    private static final String SH_QUALIFIED_VALUE = "http://www.w3.org/ns/shacl#qualifiedValueShape";
    private static final String SH_QUALIFIED_MAX_COUNT = "http://www.w3.org/ns/shacl#qualifiedMaxCount";
    private static final String SH_QUALIFIED_MIN_COUNT = "http://www.w3.org/ns/shacl#qualifiedMinCount";
    private static final String SH_CLASS = "http://www.w3.org/ns/shacl#class";

    @Test
    public void compliantWithShNodeShape() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_FOR_NODE, "TURTLE");

        Boolean condition = shapes.contains(ResourceFactory.createResource("http://iot.linkeddata.es/def/core#ActuatorShape"), RDF.type, ResourceFactory.createResource(SH_NODE_SHAPE));
        condition &= shapes.contains(ResourceFactory.createResource("http://iot.linkeddata.es/def/core#ActuatorShape"),  ResourceFactory.createProperty(SH_NODE), ResourceFactory.createResource("http://iot.linkeddata.es/def/core#FeaturePropertyShape"));
        Assert.assertTrue(!condition);
    }

    @Test
    public void compliantWithShPropertyShape() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_FOR_PROPERTY, "TURTLE");
        
        Boolean condition = shapes.contains(ResourceFactory.createResource("http://www.w3.org/2006/time#DateTimeDescriptionShape"), RDF.type, ResourceFactory.createResource(SH_NODE_SHAPE));
        condition &= shapes.contains(ResourceFactory.createResource("http://www.w3.org/2006/time#DateTimeDescriptionShape"), ResourceFactory.createProperty(SH_PROPERTY), ResourceFactory.createResource("http://www.w3.org/2006/time#day-EmbeddedRestriction"));
        condition &= shapes.contains(ResourceFactory.createResource("http://www.w3.org/2006/time#day-EmbeddedRestriction"), ResourceFactory.createProperty(SH_PATH), ResourceFactory.createResource("http://www.w3.org/2006/time#day"));

        Assert.assertTrue(condition);
    }

    @Test
    public void compliantWithShMaxQualifiedCardinalityShape() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_FOR_QUALIFIED_CARDINALITY, "TURTLE");
        
        Boolean condition = shapes.contains(ResourceFactory.createResource("http://iot.linkeddata.es/def/core#ContractShape"), RDF.type, ResourceFactory.createResource(SH_NODE_SHAPE));
        condition &= shapes.contains(ResourceFactory.createResource("http://iot.linkeddata.es/def/core#ContractShape"), ResourceFactory.createProperty(SH_PROPERTY), ResourceFactory.createResource("http://iot.linkeddata.es/def/core#requestedService-EmbeddedRestriction"));        
        condition &= shapes.contains(null, ResourceFactory.createProperty(SH_PATH), ResourceFactory.createResource("http://iot.linkeddata.es/def/core#requestedService"));
        condition &= shapes.contains(null, ResourceFactory.createProperty(SH_QUALIFIED_MAX_COUNT), ResourceFactory.createStringLiteral("1"));
        RDFNode blankNode = shapes.listObjectsOfProperty(ResourceFactory.createResource("http://iot.linkeddata.es/def/core#requestedService-EmbeddedRestriction"), ResourceFactory.createProperty(SH_QUALIFIED_VALUE)).toList().get(0);
        condition &= shapes.contains(blankNode.asResource(),  ResourceFactory.createProperty(SH_CLASS),ResourceFactory.createResource("http://iot.linkeddata.es/def/core#Service"));
      
        
        Assert.assertTrue(condition);
    }

    @Test
    public void compliantWithShMinQualifiedCardinalityShape() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_FOR_QUALIFIED_CARDINALITY, "TURTLE");
        Boolean condition = shapes.contains(ResourceFactory.createResource("http://iot.linkeddata.es/def/core#ContractShape"), RDF.type, ResourceFactory.createResource(SH_NODE_SHAPE));
        condition &= shapes.contains(ResourceFactory.createResource("http://iot.linkeddata.es/def/core#ContractShape"), ResourceFactory.createProperty(SH_PROPERTY), ResourceFactory.createResource("http://iot.linkeddata.es/def/core#serviceOwner-EmbeddedRestriction"));
        condition &= shapes.contains(null, ResourceFactory.createProperty(SH_QUALIFIED_MIN_COUNT), ResourceFactory.createStringLiteral("1"));
        condition &= shapes.contains(null, ResourceFactory.createProperty(SH_PATH), ResourceFactory.createResource("http://iot.linkeddata.es/def/core#serviceOwner"));
        RDFNode blankNode = shapes.listObjectsOfProperty(ResourceFactory.createResource("http://iot.linkeddata.es/def/core#serviceOwner-EmbeddedRestriction"), ResourceFactory.createProperty(SH_QUALIFIED_VALUE)).toList().get(0);
        condition &= shapes.contains(blankNode.asResource(),  ResourceFactory.createProperty(SH_CLASS),ResourceFactory.createResource("http://iot.linkeddata.es/def/core#Agent"));
      
        
        Assert.assertTrue(condition);
    }

    @Test
    public void compliantWithShQualifiedCardinalityShape() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_FOR_QUALIFIED_CARDINALITY, "TURTLE");

        Boolean condition = shapes.contains(ResourceFactory.createResource("http://iot.linkeddata.es/def/core#ContractShape"), RDF.type, ResourceFactory.createResource(SH_NODE_SHAPE));
        condition &= shapes.contains(ResourceFactory.createResource("http://iot.linkeddata.es/def/core#ContractShape"), ResourceFactory.createProperty(SH_PROPERTY), ResourceFactory.createResource("http://iot.linkeddata.es/def/core#servicePetitioner-EmbeddedRestriction"));
        condition &= shapes.contains(null, ResourceFactory.createProperty(SH_QUALIFIED_MAX_COUNT), ResourceFactory.createStringLiteral("1"));
        condition &= shapes.contains(null, ResourceFactory.createProperty(SH_QUALIFIED_MIN_COUNT), ResourceFactory.createStringLiteral("1"));
        condition &= shapes.contains(null, ResourceFactory.createProperty(SH_PATH), ResourceFactory.createResource("http://iot.linkeddata.es/def/core#servicePetitioner"));
        RDFNode blankNode = shapes.listObjectsOfProperty(ResourceFactory.createResource("http://iot.linkeddata.es/def/core#servicePetitioner-EmbeddedRestriction"), ResourceFactory.createProperty(SH_QUALIFIED_VALUE)).toList().get(0);
        condition &= shapes.contains(blankNode.asResource(),  ResourceFactory.createProperty(SH_CLASS),ResourceFactory.createResource("http://iot.linkeddata.es/def/core#Agent"));
        Assert.assertTrue(condition);
    }



}
