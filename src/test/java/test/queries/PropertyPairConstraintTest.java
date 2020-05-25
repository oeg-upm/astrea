package test.queries;

import astrea.generators.OwlGenerator;
import astrea.model.ShaclFromOwl;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;
import org.junit.Assert;
import org.junit.Test;

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

    private static final String SH_TARGET_CLASS= "http://www.w3.org/ns/shacl#targetClass";
    private static final String SH_NOT = "http://www.w3.org/ns/shacl#not";
    private static final String SH_CLASS = "http://www.w3.org/ns/shacl#class";
    private static final String SH_PROPERTYSHAPE = "http://www.w3.org/ns/shacl#PropertyShape";

    @Test
    public void compliantWithShEqualsShape() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_EQUIVANCE, "TURTLE");
        Boolean condition = shapes.contains(null,
                ResourceFactory.createProperty(SH_TARGET_CLASS), ResourceFactory.createResource("http://www.w3.org/2000/10/swap/pim/contact#Person"));
        condition &= shapes.contains(null,
                ResourceFactory.createProperty(SH_TARGET_CLASS), ResourceFactory.createResource("http://schema.org/Person"));
        condition &= shapes.contains(null,
                ResourceFactory.createProperty(SH_TARGET_CLASS), ResourceFactory.createResource("http://xmlns.com/foaf/0.1/Person"));
        Assert.assertTrue(condition);
    }

    @Test
    public void compliantWithShDisjointShape() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_DISJOINT, "TURTLE");
        NodeIterator iterator =  shapes.listObjectsOfProperty(ResourceFactory.createProperty(SH_NOT));
        Boolean condition = iterator.hasNext();
        while(iterator.hasNext()) {
        		Resource rangeURI = iterator.next().asResource();
        		Boolean subCondition1= shapes.contains(rangeURI, RDF.type, ResourceFactory.createProperty(SH_PROPERTYSHAPE));
        		Boolean subCondition21= shapes.contains(rangeURI, ResourceFactory.createProperty(SH_CLASS), ResourceFactory.createResource("http://xmlns.com/foaf/0.1/Organization"));
        		Boolean subCondition22 = shapes.contains(rangeURI, ResourceFactory.createProperty(SH_CLASS), ResourceFactory.createResource("http://xmlns.com/foaf/0.1/Project"));
        		condition &= subCondition1 & (subCondition21||subCondition22);
        }
       
        Assert.assertTrue(condition);
    }
}
