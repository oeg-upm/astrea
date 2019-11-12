package test.queries;

import astrea.generators.OwlGenerator;
import astrea.model.ShaclFromOwl;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Test;

public class NonValidatingPropertyShapeCharacteristicsTest {

    private static final String OWL_FRAGMENT_FOR_NAME_DESC = "@prefix : <http://www.w3.org/2006/time#> .\n" +
            "@prefix dct: <http://purl.org/dc/terms/> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" +
            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ." +
            ":Duration\n" +
            "  rdf:type owl:Class ;\n" +
            "  rdfs:label \"the duration label\"@en ;\n" +
            "  rdfs:comment \"Duration of a temporal extent expressed as a number scaled by a temporal unit\"@en ;\n" +
            "  rdfs:subClassOf :TemporalDuration ;\n" +
            "  rdfs:subClassOf [\n" +
            "      rdf:type owl:Restriction ;\n" +
            "      owl:minCardinality \"1\"^^xsd:nonNegativeInteger ;\n" +
            "      owl:onProperty :unitType ;\n" +
            "    ] ;\n" +
            "  skos:definition \"Duration of a temporal extent expressed as a number scaled by a temporal unit\"@en ;\n" +
            "  skos:note \"Alternative to time:DurationDescription to support description of a temporal duration other than using a calendar/clock system.\"@en ;\n" +
            ".\n";

    public static final String SH_NAME = "http://www.w3.org/ns/shacl#name";
    public static final String SH_DESCRIPTION = "http://www.w3.org/ns/shacl#description";

    @Test
    public void compliantWithShNameShape() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_FOR_NAME_DESC, "TURTLE");
       
        Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_NAME), ResourceFactory.createLangLiteral("the duration label", "en"));
        Assert.assertTrue(condition);
    }

    @Test
    public void compliantWithShDescriptionShape() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_FOR_NAME_DESC, "TURTLE");
        Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_DESCRIPTION),
                ResourceFactory.createLangLiteral("Duration of a temporal extent expressed as a number scaled by a temporal unit", "en"));
        Assert.assertTrue(condition);
    }
}
