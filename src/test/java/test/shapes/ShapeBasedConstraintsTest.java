package test.shapes;

import astrea.model.ShaclFromOwl;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;
import org.junit.Assert;
import org.junit.Test;
import sharper.generators.OwlShaper;

public class ShapeBasedConstraintsTest {
    // Extracted from     http://xmlns.com/foaf/0.1/
    private static final String OWL_FRAGMENT_FOR_NODE = "@prefix foaf: <http://xmlns.com/foaf/0.1/> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix dc11: <http://purl.org/dc/elements/1.1/> .\n" +
            "@prefix wot: <http://xmlns.com/wot/0.1/> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@prefix ns0: <http://www.w3.org/2003/06/sw-vocab-status/ns#> .\n" +
            "@prefix schema: <http://schema.org/> .\n" +
            "@prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#> .\n" +
            "@prefix dc: <http://purl.org/dc/terms/> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> ."+
            "foaf:Person\n" +
            "  a rdfs:Class, owl:Class ;\n" +
            "  rdfs:label \"Person\" ;\n" +
            "  rdfs:comment \"A person.\" ;\n" +
            "  ns0:term_status \"stable\" .\n" ;

    // Extracted from    http://www.w3.org/2006/time#
    private static final String OWL_FRAGMENT_FOR_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" +
            "@prefix dct: <http://purl.org/dc/terms/> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" +
            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ." +
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
            "                            owl:maxQualifiedCardinality \"1\"^^xsd:nonNegativeInteger ;\n" +
            "                            owl:onClass :Service\n" +
            "                          ], \n" +
            "                          [ rdf:type owl:Restriction ;\n" +
            "                            owl:onProperty :serviceOwner ;\n" +
            "                            owl:minQualifiedCardinality \"1\"^^xsd:nonNegativeInteger ;\n" +
            "                            owl:onClass :Agent\n" +
            "                          ] ,\n" +
            "                          [ rdf:type owl:Restriction ;\n" +
            "                            owl:onProperty :servicePetitioner ;\n" +
            "                            owl:qualifiedCardinality \"1\"^^xsd:nonNegativeInteger ;\n" +
            "                            owl:onClass :Agent\n" +
            "                          ] ;"+
            "          rdfs:comment \"Contract between organizations and requested services.\"@en ;\n" +
            "          rdfs:label \"Contract\"@en .";

    private static final String SH_NODE_SHAPE = "http://www.w3.org/ns/shacl#NodeShape";
    private static final String SH_TARGET_CLASS = "http://www.w3.org/ns/shacl#targetClass";
    private static final String SH_PROPERTY = "http://www.w3.org/ns/shacl#property";
    private static final String SH_PATH = "http://www.w3.org/ns/shacl#path";
    private static final String SH_QUALIFIED_VALUE = "http://www.w3.org/ns/shacl#qualifiedValueShape";
    private static final String SH_QUALIFIED_MAX_COUNT = "http://www.w3.org/ns/shacl#qualifiedMaxCount";
    private static final String SH_QUALIFIED_MIN_COUNT = "http://www.w3.org/ns/shacl#qualifiedMinCount";
    private static final String SH_QUALIFIED_COUNT = "http://www.w3.org/ns/shacl#qualifiedCount";

    @Test
    public void compliantWithShNodeShape() {
        ShaclFromOwl sharper = new OwlShaper();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_FOR_NODE, "TURTLE");
        Boolean condition = shapes.contains(null, RDF.type, ResourceFactory.createResource(SH_NODE_SHAPE));

        condition &= shapes.contains(null,  ResourceFactory.createProperty(SH_TARGET_CLASS), ResourceFactory.createResource("http://xmlns.com/foaf/0.1/Person"));
        Assert.assertTrue(condition);
    }

    @Test
    public void compliantWithShPropertyShape() {
        ShaclFromOwl sharper = new OwlShaper();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_FOR_PROPERTY, "TURTLE");
        shapes.write(System.out, "TURTLE");
        Boolean condition = shapes.contains(null, RDF.type, ResourceFactory.createResource(SH_NODE_SHAPE));
        condition &= shapes.contains(ResourceFactory.createResource("http://www.w3.org/2006/time#DateTimeDescriptionShape"), ResourceFactory.createProperty(SH_PROPERTY), (RDFNode) null);
        condition &= shapes.contains(null, ResourceFactory.createProperty(SH_PATH), ResourceFactory.createResource("http://www.w3.org/2006/time#day"));
        Assert.assertTrue(condition);
    }

    @Test
    public void compliantWithShMaxQualifiedCardinalityShape() {
        ShaclFromOwl sharper = new OwlShaper();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_FOR_QUALIFIED_CARDINALITY, "TURTLE");
        shapes.write(System.out, "TURTLE");
        Boolean condition = shapes.contains(null, RDF.type, ResourceFactory.createResource(SH_NODE_SHAPE));
        condition &= shapes.contains(ResourceFactory.createResource("http://iot.linkeddata.es/def/core#ContractShape"), ResourceFactory.createProperty(SH_PROPERTY), (RDFNode) null);
        condition &= shapes.contains(null, ResourceFactory.createProperty(SH_QUALIFIED_VALUE), (RDFNode) null);
        condition &= shapes.contains(null, ResourceFactory.createProperty(SH_PATH), ResourceFactory.createResource("http://iot.linkeddata.es/def/core#requestedService"));
        condition &= shapes.contains(null, ResourceFactory.createProperty(SH_QUALIFIED_MAX_COUNT), ResourceFactory.createPlainLiteral("1"));

        Assert.assertTrue(condition);
    }

    @Test
    public void compliantWithShMinQualifiedCardinalityShape() {
        ShaclFromOwl sharper = new OwlShaper();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_FOR_QUALIFIED_CARDINALITY, "TURTLE");
        shapes.write(System.out, "TURTLE");
        Boolean condition = shapes.contains(null, RDF.type, ResourceFactory.createResource(SH_NODE_SHAPE));
        condition &= shapes.contains(ResourceFactory.createResource("http://iot.linkeddata.es/def/core#ContractShape"), ResourceFactory.createProperty(SH_PROPERTY), (RDFNode) null);
        condition &= shapes.contains(null, ResourceFactory.createProperty(SH_QUALIFIED_VALUE), (RDFNode) null);
        condition &= shapes.contains(null, ResourceFactory.createProperty(SH_PATH), ResourceFactory.createResource("http://iot.linkeddata.es/def/core#serviceOwner"));
        condition &= shapes.contains(null, ResourceFactory.createProperty(SH_QUALIFIED_MIN_COUNT), ResourceFactory.createPlainLiteral("1"));
        Assert.assertTrue(condition);
    }

    @Test
    public void compliantWithShQualifiedCardinalityShape() {
        ShaclFromOwl sharper = new OwlShaper();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_FOR_QUALIFIED_CARDINALITY, "TURTLE");
        shapes.write(System.out, "TURTLE");
        Boolean condition = shapes.contains(null, RDF.type, ResourceFactory.createResource(SH_NODE_SHAPE));
        condition &= shapes.contains(ResourceFactory.createResource("http://iot.linkeddata.es/def/core#ContractShape"), ResourceFactory.createProperty(SH_PROPERTY), (RDFNode) null);
        condition &= shapes.contains(null, ResourceFactory.createProperty(SH_QUALIFIED_VALUE), (RDFNode) null);
        condition &= shapes.contains(null, ResourceFactory.createProperty(SH_PATH), ResourceFactory.createResource("http://iot.linkeddata.es/def/core#servicePetitioner"));
        condition &= shapes.contains(null, ResourceFactory.createProperty(SH_QUALIFIED_COUNT), ResourceFactory.createPlainLiteral("1"));
        Assert.assertTrue(condition);
    }



}
