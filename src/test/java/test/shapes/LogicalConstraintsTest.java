package test.shapes;

import astrea.model.ShaclFromOwl;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
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

    private static final String SH_AND = "http://www.w3.org/ns/shacl#and";


    @Test
    public void compliantWithShEqualsShape() {
        ShaclFromOwl sharper = new OwlShaper();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_AND, "TURTLE");
        Boolean condition = shapes.contains(null,
                ResourceFactory.createProperty(SH_AND), (RDFNode) null); /*TODO completar*/

        Assert.assertTrue(condition);
    }

    /*TODO sh:or sh:not*/

}
