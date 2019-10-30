package test.shapes;

import astrea.model.ShaclFromOwl;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Test;
import sharper.generators.OwlShaper;

public class ValueTypeConstraintTest {

    public static final String OWL_FRAGMENT_FOR_SHCLASS="@prefix : <https://w3id.org/def/openadr#> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix xml: <http://www.w3.org/XML/1998/namespace> .\n" +
            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@base <https://w3id.org/def/openadr> ."+
            "<http://www.opengis.net/ont/geosparql#Feature> rdf:type owl:Class ;\n" +
            "                                               rdfs:subClassOf <http://www.opengis.net/ont/geosparql#SpatialObject> ,\n" +
            "                                                               [ rdf:type owl:Restriction ;\n" +
            "                                                                 owl:onProperty <http://www.opengis.net/ont/geosparql#hasGeometry> ;\n" +
            "                                                                 owl:allValuesFrom <http://www.opengis.net/ont/geosparql#Geometry>\n" +
            "                                                               ] ;\n" +
            "                                               rdfs:comment \"This class represents the top-level feature type. This class is equivalent to GFI_Feature defined in ISO 19156:2011, and it is superclass of all feature types\" ;\n" +
            "                                               rdfs:isDefinedBy <http://www.opengis.net/ont/geosparql> ;\n" +
            "                                               rdfs:label \"Feature\" .\n";

    public static final String OWL_FRAGMENT_FOR_SHDATATYPE="@prefix : <https://w3id.org/def/openadr#> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix xml: <http://www.w3.org/XML/1998/namespace> .\n" +
            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@base <https://w3id.org/def/openadr> ."+
            ":Event rdf:type owl:Class ;\n" +
            "       rdfs:subClassOf [ rdf:type owl:Restriction ;\n" +
            "                         owl:onProperty :version ;\n" +
            "                         owl:allValuesFrom xsd:string\n" +
            "                       ] ;\n" +
            "       rdfs:comment \"An event is a notification from the utility to demand side resources requesting load shed starting at a specific time, over a specified duration, and may include targeting information designating specific resources that should participate in the event\" ;\n" +
            "       rdfs:isDefinedBy \"OpenADR 2.0 Demand Response Program Implementation Guide\" ;\n" +
            "       rdfs:label \"Event\" .";


    public static final String SH_CLASS= "http://www.w3.org/ns/shacl#class";
    public static final String SH_DATATYPE= "http://www.w3.org/ns/shacl#datatype";
    public static final String SH_NODEKIND= "http://www.w3.org/ns/shacl#nodeKind";
    public static final String SH_IRI= "http://www.w3.org/ns/shacl#IRI";

    @Test
    public void compliantWithShClassShape() {
        ShaclFromOwl sharper = new OwlShaper();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_FOR_SHCLASS, "TURTLE");
        Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_CLASS), ResourceFactory.createResource("http://www.opengis.net/ont/geosparql#Geometry"));
        condition &= !shapes.containsResource(ResourceFactory.createProperty(SH_DATATYPE));
        Assert.assertTrue(condition);
    }

    @Test
    public void compliantWithShDatatypeShape() {
        ShaclFromOwl sharper = new OwlShaper();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_FOR_SHDATATYPE, "TURTLE");
        Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_DATATYPE), ResourceFactory.createResource("http://www.w3.org/2001/XMLSchema#string"));
        condition &= !shapes.containsResource(ResourceFactory.createProperty(SH_CLASS));
        Assert.assertTrue(condition);
    }

    @Test
    public void compliantWithShNodeKindShape() {
        ShaclFromOwl sharper = new OwlShaper();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_FOR_SHCLASS, "TURTLE");
        Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_NODEKIND), ResourceFactory.createResource(SH_IRI));
        Assert.assertTrue(condition);
    }
}
