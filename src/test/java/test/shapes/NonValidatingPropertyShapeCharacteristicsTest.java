package test.shapes;

import astrea.model.ShaclFromOwl;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Test;
import sharper.generators.OwlShaper;

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
        ShaclFromOwl sharper = new OwlShaper();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_FOR_NAME_DESC, "TURTLE");
        Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_NAME), ResourceFactory.createLangLiteral("Date-Time description", "en"));
        Assert.assertTrue(condition);
    }

    @Test
    public void compliantWithShDescriptionShape() {
        ShaclFromOwl sharper = new OwlShaper();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_FOR_NAME_DESC, "TURTLE");
        shapes.write(System.out, "TURTLE");
        Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_DESCRIPTION),
                ResourceFactory.createLangLiteral("Description of date and time structured with separate values for the various elements of a calendar-clock system. The temporal reference system is fixed to Gregorian Calendar, and the range of year, month, day properties restricted to corresponding XML Schema types xsd:gYear, xsd:gMonth and xsd:gDay, respectively.", "en"));
        Assert.assertTrue(condition);
    }
}
