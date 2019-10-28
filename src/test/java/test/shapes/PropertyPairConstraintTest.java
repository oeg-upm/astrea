package test.shapes;

import astrea.model.ShaclFromOwl;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Test;
import sharper.generators.OwlShaper;

public class PropertyPairConstraintTest {


    // Extracted from: https://www.w3.org/2006/time#s
    public static final String OWL_FRAGMENT_EQUIVANCE = "@prefix foaf: <http://xmlns.com/foaf/0.1/> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix dc11: <http://purl.org/dc/elements/1.1/> .\n" +
            "@prefix wot: <http://xmlns.com/wot/0.1/> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@prefix ns0: <http://www.w3.org/2003/06/sw-vocab-status/ns#> .\n" +
            "@prefix schema: <http://schema.org/> .\n" +
            "@prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#> .\n" +
            "@prefix dc: <http://purl.org/dc/terms/> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> ." +
            "foaf:Person\n" +
            "  a rdfs:Class, owl:Class ;\n" +
            "  rdfs:label \"Person\" ;\n" +
            "  rdfs:comment \"A person.\" ;\n" +
            "  ns0:term_status \"stable\" ;\n" +
            "  owl:equivalentClass schema:Person, <http://www.w3.org/2000/10/swap/pim/contact#Person> ;\n" +
            "  rdfs:isDefinedBy foaf: ;\n";


    public static final String OWL_FRAGMENT_DISJOINT = "@prefix foaf: <http://xmlns.com/foaf/0.1/> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix dc11: <http://purl.org/dc/elements/1.1/> .\n" +
            "@prefix wot: <http://xmlns.com/wot/0.1/> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@prefix ns0: <http://www.w3.org/2003/06/sw-vocab-status/ns#> .\n" +
            "@prefix schema: <http://schema.org/> .\n" +
            "@prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#> .\n" +
            "@prefix dc: <http://purl.org/dc/terms/> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> ." +
            "foaf:Person\n" +
            "  a rdfs:Class, owl:Class ;\n" +
            "  rdfs:label \"Person\" ;\n" +
            "  rdfs:comment \"A person.\" ;\n" +
            "  ns0:term_status \"stable\" ;\n" +
            "  rdfs:isDefinedBy foaf: ;\n" +
            "  owl:disjointWith foaf:Organization, foaf:Project ";

    private static final String SH_EQUALS = "http://www.w3.org/ns/shacl#equals";
    private static final String SH_DISJOINT = "http://www.w3.org/ns/shacl#disjoint";


    @Test
    public void compliantWithShEqualsShape() {
        ShaclFromOwl sharper = new OwlShaper();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_EQUIVANCE, "TURTLE");
        Boolean condition = shapes.contains(ResourceFactory.createResource("http://xmlns.com/foaf/0.1/Person"),
                ResourceFactory.createProperty(SH_EQUALS), ResourceFactory.createResource("http://xmlns.com/foaf/0.1/Agent"));
        condition &= shapes.contains(ResourceFactory.createResource("http://xmlns.com/foaf/0.1/Person"),
                ResourceFactory.createProperty(SH_EQUALS), ResourceFactory.createResource("http://www.w3.org/2003/01/geo/wgs84_pos#SpatialThing"));

        Assert.assertTrue(condition);
    }

    @Test
    public void compliantWithShDisjointShape() {
        ShaclFromOwl sharper = new OwlShaper();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_DISJOINT, "TURTLE");
        Boolean condition = shapes.contains(ResourceFactory.createResource("http://xmlns.com/foaf/0.1/Person"),
                ResourceFactory.createProperty(SH_DISJOINT), ResourceFactory.createResource("http://xmlns.com/foaf/0.1/Organization"));
        condition &= shapes.contains(ResourceFactory.createResource("http://xmlns.com/foaf/0.1/Person"),
                ResourceFactory.createProperty(SH_DISJOINT), ResourceFactory.createResource("http://xmlns.com/foaf/0.1/Project"));
        Assert.assertTrue(condition);
    }
}
