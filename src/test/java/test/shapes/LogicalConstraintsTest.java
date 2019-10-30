package test.shapes;

import astrea.model.ShaclFromOwl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFList;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.junit.Assert;
import org.junit.Test;
import sharper.generators.OwlShaper;

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

    private static final String SH_AND = "http://www.w3.org/ns/shacl#and";
    private static final String SH_NOT = "http://www.w3.org/ns/shacl#not";
    private static final String SH_PROPERTY_SHAPE = "http://www.w3.org/ns/shacl#PropertyShape";
    private static final String SH_PATH= "http://www.w3.org/ns/shacl#path";


    @Test
    public void compliantWithShEqualsShape() {
        ShaclFromOwl sharper = new OwlShaper();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_AND, "TURTLE");
        Boolean condition = shapes.containsResource(ResourceFactory.createResource("http://njh.me/#BurgundyShape"));
        condition &= shapes.containsResource(ResourceFactory.createResource("http://njh.me/#WhiteWineShape")); 
        Assert.assertTrue(condition);
    }

    /*TODO sh:or sh:not*/
    @Test
    public void compliantWithShNotShape() {
        ShaclFromOwl sharper = new OwlShaper();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_NOT, "TURTLE");
        Boolean condition = shapes.contains(ResourceFactory.createResource("http://www.co-ode.org/ontologies/pizza/pizza.owl#NonConsumableThingShape"),
                ResourceFactory.createProperty(SH_NOT), ResourceFactory.createResource("http://www.co-ode.org/ontologies/pizza/pizza.owl#ConsumableThingShape"));
        condition &= shapes.containsResource(ResourceFactory.createResource("http://njh.me/#WhiteWineShape"));
        Assert.assertTrue(condition);
    }

    @Test
    public void compliantWithShNotPropertyShape() {
        ShaclFromOwl sharper = new OwlShaper();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_NOT_PROP, "TURTLE");
        Boolean condition = shapes.contains(ResourceFactory.createResource("http://www.co-ode.org/ontologies/pizza/pizza.owl#WineShape"),
                ResourceFactory.createProperty(SH_NOT), (RDFNode) null);
        condition &= shapes.contains(null, RDF.type, ResourceFactory.createResource(SH_PROPERTY_SHAPE));
        condition &= shapes.contains(null, ResourceFactory.createProperty(SH_PATH), ResourceFactory.createResource("http://www.co-ode.org/ontologies/pizza/pizza.owl#locatedIn"));
        Assert.assertTrue(condition);
    }
}
