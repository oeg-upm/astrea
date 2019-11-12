package test.optimised.shapes;

import astrea.generators.OptimisedOwlGenerator;
import astrea.model.ShaclFromOwl;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Test;

public class CardinalityConstraintTest {
    // Extracted from: https://www.w3.org/2006/time#s
    public static final String CARDINALITY_OWL_FRAGMENT_MIN_COUNT = "@prefix : <http://www.w3.org/2006/time#> .\n" +
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
            "      owl:minCardinality \"1\"^^xsd:nonNegativeInteger ;\n" +
            "      owl:onProperty :unitType ;\n" +
            "    ] ;\n" +
            "  skos:definition \"Duration of a temporal extent expressed as a number scaled by a temporal unit\"@en ;\n" +
            "  skos:note \"Alternative to time:DurationDescription to support description of a temporal duration other than using a calendar/clock system.\"@en ;\n" +
            ".\n";
    // Extracted from: https://www.w3.org/2006/time#s --> modified
    public static final String CARDINALITY_OWL_FRAGMENT_MAX_COUNT = "@prefix : <http://www.w3.org/2006/time#> .\n" +
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
            "      owl:maxCardinality \"1\"^^xsd:nonNegativeInteger ;\n" +
            "      owl:onProperty :unitType ;\n" +
            "    ] ;\n" +
            "  skos:definition \"Duration of a temporal extent expressed as a number scaled by a temporal unit\"@en ;\n" +
            "  skos:note \"Alternative to time:DurationDescription to support description of a temporal duration other than using a calendar/clock system.\"@en ;\n" +
            ".\n";

    private static final String SH_MAX_COUNT = "http://www.w3.org/ns/shacl#maxCount";
    private static final String SH_MIN_COUNT = "http://www.w3.org/ns/shacl#minCount";

    @Test
    public void compliantWithShMinCountShape() {
        ShaclFromOwl sharper = new OptimisedOwlGenerator();
        Model shapes =  sharper.fromOwl(CARDINALITY_OWL_FRAGMENT_MIN_COUNT, "TURTLE");
        Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_MIN_COUNT),ResourceFactory.createTypedLiteral(1));
        Assert.assertTrue(condition);
    }

    @Test
    public void compliantWithShMaxCountShape() {
        ShaclFromOwl sharper = new OptimisedOwlGenerator();
        Model shapes =  sharper.fromOwl(CARDINALITY_OWL_FRAGMENT_MAX_COUNT, "TURTLE");
        Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_MAX_COUNT),ResourceFactory.createTypedLiteral(1));
        Assert.assertTrue(condition);
    }
}
