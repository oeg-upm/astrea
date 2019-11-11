package test.queries;

import astrea.model.ShaclFromOwl;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Test;
import sharper.generators.OwlGenerator;

public class StringBasedConstraintTest {

    //Extracted form https://www.w3.org/2006/time
    public static final String OWL_FRAGMENT_PATTERN = "" +
            "@prefix : <http://www.w3.org/2006/time#> .\n" +
            "@prefix dct: <http://purl.org/dc/terms/> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" +
            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ." +
            ":generalDay\n" +
            "  rdf:type rdfs:Datatype ;\n" +
            "  rdfs:comment \"\"\"Day of month - formulated as a text string with a pattern constraint to reproduce the same lexical form as gDay, except that values up to 99 are permitted, in order to support calendars with more than 31 days in a month. \n" +
            "Note that the value-space is not defined, so a generic OWL2 processor cannot compute ordering relationships of values of this type.\"\"\"@en ;\n" +
            "  rdfs:label \"Generalized day\"@en ;\n" +
            "  owl:onDatatype xsd:string ;\n" +
            "  owl:withRestrictions (\n" +
            "      [\n" +
            "        xsd:pattern \"---(0[1-9]|[1-9][0-9])(Z|(\\\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?\" ;\n" +
            "      ]\n" +
            "    ) ;\n" +
            "  skos:definition \"\"\"Day of month - formulated as a text string with a pattern constraint to reproduce the same lexical form as gDay, except that values up to 99 are permitted, in order to support calendars with more than 31 days in a month. \n" +
            "Note that the value-space is not defined, so a generic OWL2 processor cannot compute ordering relationships of values of this type.\"\"\"@en ;\n" +
            ".";

    //Modified
    public static final String OWL_FRAGMENT_LANG = "" +
            "@prefix : <http://www.w3.org/2006/time#> .\n" +
            "@prefix dct: <http://purl.org/dc/terms/> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" +
            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ." +
            ":generalDay\n" +
            "  rdf:type rdfs:Datatype ;\n" +
            "  rdfs:comment \"\"\"Day of month - formulated as a text string with a pattern constraint to reproduce the same lexical form as gDay, except that values up to 99 are permitted, in order to support calendars with more than 31 days in a month. \n" +
            "Note that the value-space is not defined, so a generic OWL2 processor cannot compute ordering relationships of values of this type.\"\"\"@en ;\n" +
            "  rdfs:label \"Generalized day\"@en ;\n" +
            "  owl:onDatatype xsd:string ;\n" +
            "  owl:withRestrictions (\n" +
            "      [\n" +
            "           rdf:langRange  \"de-DE\"" +
            "      ]\n" +
            "    ) ;\n" +
            "  skos:definition \"\"\"Day of month - formulated as a text string with a pattern constraint to reproduce the same lexical form as gDay, except that values up to 99 are permitted, in order to support calendars with more than 31 days in a month. \n" +
            "Note that the value-space is not defined, so a generic OWL2 processor cannot compute ordering relationships of values of this type.\"\"\"@en ;\n" +
            ".";

    //Modified
    public static final String OWL_FRAGMENT_MIN_MAX_LENGTH = "" +
            "@prefix : <http://www.w3.org/2006/time#> .\n" +
            "@prefix dct: <http://purl.org/dc/terms/> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" +
            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ." +
            ":generalDay\n" +
            "  rdf:type rdfs:Datatype ;\n" +
            "  rdfs:comment \"\"\"Day of month - formulated as a text string with a pattern constraint to reproduce the same lexical form as gDay, except that values up to 99 are permitted, in order to support calendars with more than 31 days in a month. \n" +
            "Note that the value-space is not defined, so a generic OWL2 processor cannot compute ordering relationships of values of this type.\"\"\"@en ;\n" +
            "  rdfs:label \"Generalized day\"@en ;\n" +
            "  owl:onDatatype xsd:string ;\n" +
            "  owl:withRestrictions (\n" +
            "      [\n" +
            "           xsd:minLength  2" +
            "      ]\n" +
            "      [\n" +
            "           xsd:maxLength  4" +
            "      ]\n" +
            "    ) ;\n" +
            "  skos:definition \"\"\"Day of month - formulated as a text string with a pattern constraint to reproduce the same lexical form as gDay, except that values up to 99 are permitted, in order to support calendars with more than 31 days in a month. \n" +
            "Note that the value-space is not defined, so a generic OWL2 processor cannot compute ordering relationships of values of this type.\"\"\"@en ;\n" +
            ".";
    public static final String OWL_FRAGMENT_LENGTH = "" +
            "@prefix : <http://www.w3.org/2006/time#> .\n" +
            "@prefix dct: <http://purl.org/dc/terms/> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" +
            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ." +
            ":generalDay\n" +
            "  rdf:type rdfs:Datatype ;\n" +
            "  rdfs:comment \"\"\"Day of month - formulated as a text string with a pattern constraint to reproduce the same lexical form as gDay, except that values up to 99 are permitted, in order to support calendars with more than 31 days in a month. \n" +
            "Note that the value-space is not defined, so a generic OWL2 processor cannot compute ordering relationships of values of this type.\"\"\"@en ;\n" +
            "  rdfs:label \"Generalized day\"@en ;\n" +
            "  owl:onDatatype xsd:string ;\n" +
            "  owl:withRestrictions (\n" +
            "      [\n" +
            "           xsd:length  2" +
            "      ]\n" +
            "    ) ;\n" +
            "  skos:definition \"\"\"Day of month - formulated as a text string with a pattern constraint to reproduce the same lexical form as gDay, except that values up to 99 are permitted, in order to support calendars with more than 31 days in a month. \n" +
            "Note that the value-space is not defined, so a generic OWL2 processor cannot compute ordering relationships of values of this type.\"\"\"@en ;\n" +
            ".";
    
    //Modified
    public static final String OWL_FRAGMENT_MIN_LENGTH = "" +
            "@prefix : <http://www.w3.org/2006/time#> .\n" +
            "@prefix dct: <http://purl.org/dc/terms/> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" +
            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ." +
            ":generalDay\n" +
            "  rdf:type rdfs:Datatype ;\n" +
            "  rdfs:comment \"\"\"Day of month - formulated as a text string with a pattern constraint to reproduce the same lexical form as gDay, except that values up to 99 are permitted, in order to support calendars with more than 31 days in a month. \n" +
            "Note that the value-space is not defined, so a generic OWL2 processor cannot compute ordering relationships of values of this type.\"\"\"@en ;\n" +
            "  rdfs:label \"Generalized day\"@en ;\n" +
            "  owl:onDatatype xsd:string ;\n" +
            "  owl:withRestrictions (\n" +
            "      [\n" +
            "           xsd:minLength  2" +
            "      ]\n" +
            "      [\n" +
            "           xsd:maxLength  4" +
            "      ]\n" +
            "    ) ;\n" +
            "  skos:definition \"\"\"Day of month - formulated as a text string with a pattern constraint to reproduce the same lexical form as gDay, except that values up to 99 are permitted, in order to support calendars with more than 31 days in a month. \n" +
            "Note that the value-space is not defined, so a generic OWL2 processor cannot compute ordering relationships of values of this type.\"\"\"@en ;\n" +
            ".";
    
    public static final String OWL_FRAGMENT_MIN_MAX_EXCLUSIVE = "" +
            "@prefix : <http://www.w3.org/2006/time#> .\n" +
            "@prefix dct: <http://purl.org/dc/terms/> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" +
            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ." +
            ":generalDay\n" +
            "  rdf:type rdfs:Datatype ;\n" +
            "  rdfs:comment \"\"\"Day of month - formulated as a text string with a pattern constraint to reproduce the same lexical form as gDay, except that values up to 99 are permitted, in order to support calendars with more than 31 days in a month. \n" +
            "Note that the value-space is not defined, so a generic OWL2 processor cannot compute ordering relationships of values of this type.\"\"\"@en ;\n" +
            "  rdfs:label \"Generalized day\"@en ;\n" +
            "  owl:onDatatype xsd:string ;\n" +
            "  owl:withRestrictions (\n" +
            "      [\n" +
            "           xsd:minExclusive  2" +
            "      ]\n" +
            "      [\n" +
            "           xsd:maxExclusive  4" +
            "      ]\n" +
            "    ) ;\n" +
            "  skos:definition \"\"\"Day of month - formulated as a text string with a pattern constraint to reproduce the same lexical form as gDay, except that values up to 99 are permitted, in order to support calendars with more than 31 days in a month. \n" +
            "Note that the value-space is not defined, so a generic OWL2 processor cannot compute ordering relationships of values of this type.\"\"\"@en ;\n" +
            ".";
    public static final String OWL_FRAGMENT_MIN_MAX_INCLUSIVE = "" +
            "@prefix : <http://www.w3.org/2006/time#> .\n" +
            "@prefix dct: <http://purl.org/dc/terms/> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" +
            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ." +
            ":generalDay\n" +
            "  rdf:type rdfs:Datatype ;\n" +
            "  rdfs:comment \"\"\"Day of month - formulated as a text string with a pattern constraint to reproduce the same lexical form as gDay, except that values up to 99 are permitted, in order to support calendars with more than 31 days in a month. \n" +
            "Note that the value-space is not defined, so a generic OWL2 processor cannot compute ordering relationships of values of this type.\"\"\"@en ;\n" +
            "  rdfs:label \"Generalized day\"@en ;\n" +
            "  owl:onDatatype xsd:string ;\n" +
            "  owl:withRestrictions (\n" +
            "      [\n" +
            "           xsd:minInclusive  2" +
            "      ]\n" +
            "      [\n" +
            "           xsd:maxInclusive  4" +
            "      ]\n" +
            "    ) ;\n" +
            "  skos:definition \"\"\"Day of month - formulated as a text string with a pattern constraint to reproduce the same lexical form as gDay, except that values up to 99 are permitted, in order to support calendars with more than 31 days in a month. \n" +
            "Note that the value-space is not defined, so a generic OWL2 processor cannot compute ordering relationships of values of this type.\"\"\"@en ;\n" +
            ".";
    private static final String SH_PATTERN = "http://www.w3.org/ns/shacl#pattern";
    private static final String SH_MIN_LENGTH = "http://www.w3.org/ns/shacl#minLength";
    private static final String SH_MAX_LENGTH = "http://www.w3.org/ns/shacl#maxLength";
    private static final String SH_LENGTH = "http://www.w3.org/ns/shacl#length";
    private static final String SH_MAX_EXCLUSIVE = "http://www.w3.org/ns/shacl#maxExclusive";
    private static final String SH_MIN_EXCLUSIVE = "http://www.w3.org/ns/shacl#minExclusive";
    
    private static final String SH_MAX_INCLUSIVE = "http://www.w3.org/ns/shacl#maxInclusive";
    private static final String SH_MIN_INCLUSIVE = "http://www.w3.org/ns/shacl#minInclusive";
    


    @Test
    public void compliantWithShPatternShape() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_PATTERN, "TURTLE");
        String pattern = shapes.listObjectsOfProperty(ResourceFactory.createProperty(SH_PATTERN)).toList().get(0).toString();
        Boolean condition = pattern.equals("---(0[1-9]|[1-9][0-9])(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?");
        Assert.assertTrue(condition);
    }


    @Test
    public void compliantWithMinMaxLengthShape() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_MIN_MAX_LENGTH, "TURTLE");
        Boolean condition = shapes.contains(null,
                ResourceFactory.createProperty(SH_MIN_LENGTH), ResourceFactory.createTypedLiteral(2));
        
        Assert.assertTrue(condition);
    }

    @Test
    public void compliantWithShMaxLengthShape() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_MIN_MAX_LENGTH, "TURTLE");
        Boolean condition = shapes.contains(null,
                ResourceFactory.createProperty(SH_MAX_LENGTH), ResourceFactory.createTypedLiteral(4));
        Assert.assertTrue(condition);
    }
    
    @Test
    public void compliantWithShMinLengthShape() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_MIN_MAX_LENGTH, "TURTLE");
        Boolean condition = shapes.contains(null,
                ResourceFactory.createProperty(SH_MIN_LENGTH), ResourceFactory.createTypedLiteral(2));
        condition = shapes.contains(null,
                ResourceFactory.createProperty(SH_MAX_LENGTH), ResourceFactory.createTypedLiteral(4));
        Assert.assertTrue(condition);
    }
    
    @Test
    public void compliantWithLengthShape() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_LENGTH, "TURTLE");
        Boolean condition = shapes.contains(null,
                ResourceFactory.createProperty(SH_LENGTH), ResourceFactory.createTypedLiteral(2));
        
        Assert.assertTrue(condition);
    }

    @Test
    public void compliantWithMinMaxExclusiveShape() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_MIN_MAX_EXCLUSIVE, "TURTLE");
        Boolean condition = shapes.contains(null,
                ResourceFactory.createProperty(SH_MIN_EXCLUSIVE), ResourceFactory.createTypedLiteral(2));
        
        Assert.assertTrue(condition);
    }

    @Test
    public void compliantWithShMaxExclusiveShape() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_MIN_MAX_EXCLUSIVE, "TURTLE");
        Boolean condition = shapes.contains(null,
                ResourceFactory.createProperty(SH_MAX_EXCLUSIVE), ResourceFactory.createTypedLiteral(4));
        Assert.assertTrue(condition);
    }
    
    @Test
    public void compliantWithShMinExclusiveShape() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_MIN_MAX_EXCLUSIVE, "TURTLE");
        Boolean condition = shapes.contains(null,
                ResourceFactory.createProperty(SH_MIN_EXCLUSIVE), ResourceFactory.createTypedLiteral(2));
        condition = shapes.contains(null,
                ResourceFactory.createProperty(SH_MAX_EXCLUSIVE), ResourceFactory.createTypedLiteral(4));
        Assert.assertTrue(condition);
    }
    
    @Test
    public void compliantWithMinMaxInclusiveShape() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_MIN_MAX_INCLUSIVE, "TURTLE");
        Boolean condition = shapes.contains(null,
                ResourceFactory.createProperty(SH_MIN_INCLUSIVE), ResourceFactory.createTypedLiteral(2));
        
        Assert.assertTrue(condition);
    }

    @Test
    public void compliantWithShMaxInclusiveShape() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_MIN_MAX_INCLUSIVE, "TURTLE");
        Boolean condition = shapes.contains(null,
                ResourceFactory.createProperty(SH_MAX_INCLUSIVE), ResourceFactory.createTypedLiteral(4));
        Assert.assertTrue(condition);
    }
    
    @Test
    public void compliantWithShMinInclusiveShape() {
        ShaclFromOwl sharper = new OwlGenerator();
        Model shapes =  sharper.fromOwl(OWL_FRAGMENT_MIN_MAX_INCLUSIVE, "TURTLE");
        Boolean condition = shapes.contains(null,
                ResourceFactory.createProperty(SH_MIN_INCLUSIVE), ResourceFactory.createTypedLiteral(2));
        condition = shapes.contains(null,
                ResourceFactory.createProperty(SH_MAX_INCLUSIVE), ResourceFactory.createTypedLiteral(4));
        Assert.assertTrue(condition);
    }

}
