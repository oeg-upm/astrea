package test.queries.datatypes;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Test;

import astrea.generators.OwlGenerator;
import astrea.model.ShaclFromOwl;

public class InjectedDatatypesTest {


	public static final String OWL_FRAGMENT_DECIMAL_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:decimal ;\n" + 
			".";
	public static final String OWL_FRAGMENT_FLOAT_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:float ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_DOUBLE_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:double ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_DURATION_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:duration ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_YEAR_MONTH_DURATION_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:yearMonthDuration ;\n" + 
			".";
	
	
	public static final String OWL_FRAGMENT_DAY_TIME_DURATION_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:dayTimeDuration ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_NON_NEGATIVE_INTEGER_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:nonNegativeInteger ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_NON_POSITIVE_INTEGER_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:nonPositiveInteger ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_NEGATIVE_INTEGER_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:negativeInteger ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_POSITIVE_INTEGER_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:positiveInteger ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_TIME_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:time ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_G_MONTH_DAY_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:gMonthDay ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_BOOLEAN_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:boolean ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_G_DAY_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:gDay ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_G_MONTH_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:gMonth ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_HEX_BINARY_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:hexBinary ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_B64_BINARY_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:base64Binary ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_LANGUAGE_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:language ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_NMTOKEN_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:NMTOKEN ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_NMTOKENS_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:NMTOKENS ;\n" + 
			".";
	
	
	public static final String OWL_FRAGMENT_NAME_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:Name ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_NCNAME_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:NCName ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_INTEGER_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:integer ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_LONG_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:long ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_UNSIGNED_LONG_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:unsignedLong ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_UNSIGNED_INT_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:unsignedInt ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_UNSIGNED_SHORT_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:unsignedShort ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_UNSIGNED_BYTE_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:unsignedByte ;\n" + 
			".";
	
	
	public static final String OWL_FRAGMENT_INT_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:int ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_SHORT_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:short ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_BYTE_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:byte ;\n" + 
			".";

	
	public static final String OWL_FRAGMENT_STRING_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:string ;\n" + 
			".";

	
	public static final String OWL_FRAGMENT_DATE_TIME_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:dateTime ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_DATE_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:date ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_G_YEAR_MONTH_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:gYearMonth ;\n" + 
			".";
	public static final String OWL_FRAGMENT_G_YEAR_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:gYear ;\n" + 
			".";
	
	public static final String OWL_FRAGMENT_ANY_URI_DATA_PROPERTY = "@prefix : <http://www.w3.org/2006/time#> .\n" + 
			"@prefix dct: <http://purl.org/dc/terms/> .\n" + 
			"@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" + 
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
			"@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\n" + 
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
			":fakeProperty\n" + 
			"  rdf:type owl:DatatypeProperty ;\n" + 
			"  rdfs:domain :FakeClass ;\n" + 
			"  rdfs:range xsd:anyURI ;\n" + 
			".";
	
	private static final String SH_PATTERN_SHAPE = "http://www.w3.org/ns/shacl#pattern";
	private static final String SH_MAX_SHAPE = "http://www.w3.org/ns/shacl#maxInclusive";
	private static final String SH_MIN_SHAPE = "http://www.w3.org/ns/shacl#minInclusive";
	private static final String SH_MIN_LENGTH_SHAPE = "http://www.w3.org/ns/shacl#minLength";

	
	@Test
	public void decimalTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_DECIMAL_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "(\\+|-)?([0-9]+(\\.[0-9]*)?|\\.[0-9]+)");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void floatTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_FLOAT_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "(\\+|-)?([0-9]+(\\.[0-9]*)?|\\.[0-9]+)([Ee](\\+|-)?[0-9]+)?|(\\+|-)?INF|NaN");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void doubleTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_DOUBLE_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "(\\+|-)?([0-9]+(\\.[0-9]*)?|\\.[0-9]+)([Ee](\\+|-)?[0-9]+)? |(\\+|-)?INF|NaN");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void durationTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_DURATION_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "-?P((([0-9]+Y([0-9]+M)?([0-9]+D)?|([0-9]+M)([0-9]+D)?|([0-9]+D))(T(([0-9]+H)([0-9]+M)?([0-9]+(\\.[0-9]+)?S)?|([0-9]+M)([0-9]+(\\.[0-9]+)?S)?|([0-9]+(\\.[0-9]+)?S)))?)|(T(([0-9]+H)([0-9]+M)?([0-9]+(\\.[0-9]+)?S)?|([0-9]+M)([0-9]+(\\.[0-9]+)?S)?|([0-9]+(\\.[0-9]+)?S))))");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void yearMonthDurationTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_YEAR_MONTH_DURATION_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "[^DT]*");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void dayTimeDurationTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_DAY_TIME_DURATION_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "[^YM]*(T.*)?");
		Assert.assertTrue(condition);
	}
	

	@Test
	public void timeTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_TIME_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "(([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9](\\.[0-9]+)?|(24:00:00(\\.0+)?))(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void booleanTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_BOOLEAN_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "true|false|0|1");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void gMonthDayTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_G_MONTH_DAY_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "--(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void gDayTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_G_DAY_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "---(0[1-9]|[12][0-9]|3[01])(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void gMonthTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_G_MONTH_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "--(0[1-9]|1[0-2])(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void hexBinaryTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_HEX_BINARY_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "([0-9a-fA-F]{2})*");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void base64BinaryTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_B64_BINARY_DATA_PROPERTY, "TURTLE"); 
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "((([A-Za-z0-9+/] ?){4})*(([A-Za-z0-9+/] ?){3}[A-Za-z0-9+/]|([A-Za-z0-9+/] ?){2}[AEIMQUYcgkosw048] ?=|[A-Za-z0-9+/] ?[AQgw] ?= ?=))?");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void languageTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_LANGUAGE_DATA_PROPERTY, "TURTLE");		
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "[a-zA-Z]{1,8}(-[a-zA-Z0-9]{1,8})*");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void nMTOKENTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_NMTOKEN_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "\\c+");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void nMTOKENSTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_NMTOKENS_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "\\c+");
		String min = shapes.listObjectsOfProperty(ResourceFactory.createProperty(SH_MIN_LENGTH_SHAPE)).toList().get(0).asLiteral().toString();
		condition &= min.equals("1^^http://www.w3.org/2001/XMLSchema#integer");
		
		Assert.assertTrue(condition);
	}
	
	@Test
	public void nameTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_NAME_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "\\i\\c*");
		Assert.assertTrue(condition);
	}
	

	
	@Test
	public void integerTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_INTEGER_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "[\\-+]?[0-9]+");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void longTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_LONG_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "[\\-+]?[0-9]+");
		String max = shapes.listObjectsOfProperty(ResourceFactory.createProperty(SH_MAX_SHAPE)).toList().get(0).asLiteral().toString();
		String min = shapes.listObjectsOfProperty(ResourceFactory.createProperty(SH_MIN_SHAPE)).toList().get(0).asLiteral().toString();
		condition &= min.equals("-9223372036854775808^^http://www.w3.org/2001/XMLSchema#integer") && max.equals("9223372036854775807^^http://www.w3.org/2001/XMLSchema#integer");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void unsignedLongTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_UNSIGNED_LONG_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "[\\-+]?[0-9]+");
		String max = shapes.listObjectsOfProperty(ResourceFactory.createProperty(SH_MAX_SHAPE)).toList().get(0).asLiteral().toString();
		String min = shapes.listObjectsOfProperty(ResourceFactory.createProperty(SH_MIN_SHAPE)).toList().get(0).asLiteral().toString();
		condition &= min.equals("0^^http://www.w3.org/2001/XMLSchema#integer") && max.equals("18446744073709551615^^http://www.w3.org/2001/XMLSchema#integer");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void unsignedIntTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_UNSIGNED_INT_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "[\\-+]?[0-9]+");
		String max = shapes.listObjectsOfProperty(ResourceFactory.createProperty(SH_MAX_SHAPE)).toList().get(0).asLiteral().toString();
		String min = shapes.listObjectsOfProperty(ResourceFactory.createProperty(SH_MIN_SHAPE)).toList().get(0).asLiteral().toString();
		condition &= min.equals("0^^http://www.w3.org/2001/XMLSchema#integer") && max.equals("4294967295^^http://www.w3.org/2001/XMLSchema#integer");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void unsignedShortTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_UNSIGNED_SHORT_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "[\\-+]?[0-9]+");
		String max = shapes.listObjectsOfProperty(ResourceFactory.createProperty(SH_MAX_SHAPE)).toList().get(0).asLiteral().toString();
		String min = shapes.listObjectsOfProperty(ResourceFactory.createProperty(SH_MIN_SHAPE)).toList().get(0).asLiteral().toString();
		condition &= min.equals("0^^http://www.w3.org/2001/XMLSchema#integer") && max.equals("65535^^http://www.w3.org/2001/XMLSchema#integer");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void unsignedByteTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_UNSIGNED_BYTE_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "[\\-+]?[0-9]+");
		String max = shapes.listObjectsOfProperty(ResourceFactory.createProperty(SH_MAX_SHAPE)).toList().get(0).asLiteral().toString();
		String min = shapes.listObjectsOfProperty(ResourceFactory.createProperty(SH_MIN_SHAPE)).toList().get(0).asLiteral().toString();
		condition &= min.equals("0^^http://www.w3.org/2001/XMLSchema#integer") && max.equals("255^^http://www.w3.org/2001/XMLSchema#integer");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void intTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_INT_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "[\\-+]?[0-9]+");
		String max = shapes.listObjectsOfProperty(ResourceFactory.createProperty(SH_MAX_SHAPE)).toList().get(0).asLiteral().toString();
		String min = shapes.listObjectsOfProperty(ResourceFactory.createProperty(SH_MIN_SHAPE)).toList().get(0).asLiteral().toString();
		condition &= min.equals("-2147483648^^http://www.w3.org/2001/XMLSchema#integer") && max.equals("2147483647^^http://www.w3.org/2001/XMLSchema#integer");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void shortTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_SHORT_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "[\\-+]?[0-9]+");
		String max = shapes.listObjectsOfProperty(ResourceFactory.createProperty(SH_MAX_SHAPE)).toList().get(0).asLiteral().toString();
		String min = shapes.listObjectsOfProperty(ResourceFactory.createProperty(SH_MIN_SHAPE)).toList().get(0).asLiteral().toString();
		condition &= min.equals("-32768^^http://www.w3.org/2001/XMLSchema#integer") && max.equals("32767^^http://www.w3.org/2001/XMLSchema#integer");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void byteTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_BYTE_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "[\\-+]?[0-9]+");
		String max = shapes.listObjectsOfProperty(ResourceFactory.createProperty(SH_MAX_SHAPE)).toList().get(0).asLiteral().toString();
		String min = shapes.listObjectsOfProperty(ResourceFactory.createProperty(SH_MIN_SHAPE)).toList().get(0).asLiteral().toString();
		condition &= min.equals("-128^^http://www.w3.org/2001/XMLSchema#integer") && max.equals("127^^http://www.w3.org/2001/XMLSchema#integer");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void nonNegativeIntegerTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_NON_NEGATIVE_INTEGER_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "[\\-+]?[0-9]+");
		String min = shapes.listObjectsOfProperty(ResourceFactory.createProperty(SH_MIN_SHAPE)).toList().get(0).asLiteral().toString();
		condition &= min.equals("0^^http://www.w3.org/2001/XMLSchema#integer");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void nonPositiveIntegerTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_NON_POSITIVE_INTEGER_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "[\\-+]?[0-9]+");
		String min = shapes.listObjectsOfProperty(ResourceFactory.createProperty(SH_MAX_SHAPE)).toList().get(0).asLiteral().toString();
		condition &= min.equals("0^^http://www.w3.org/2001/XMLSchema#integer");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void negativeIntegerTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_NEGATIVE_INTEGER_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "[\\-+]?[0-9]+");
		String min = shapes.listObjectsOfProperty(ResourceFactory.createProperty(SH_MAX_SHAPE)).toList().get(0).asLiteral().toString();
		condition &= min.equals("-1^^http://www.w3.org/2001/XMLSchema#integer");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void positiveIntegerTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_POSITIVE_INTEGER_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "[\\-+]?[0-9]+");
		String min = shapes.listObjectsOfProperty(ResourceFactory.createProperty(SH_MIN_SHAPE)).toList().get(0).asLiteral().toString();
		condition &= min.equals("1^^http://www.w3.org/2001/XMLSchema#integer");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void stringTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_STRING_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), ".*");
		Assert.assertTrue(condition);
	}

	@Test
	public void dateTimeTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_DATE_TIME_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "-?([1-9][0-9]{3,}|0[0-9]{3})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])T(([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9](\\.[0-9]+)?|(24:00:00(\\.0+)?))(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?");
		Assert.assertTrue(condition);
	}


	
	@Test
	public void dateTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_DATE_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "-?([1-9][0-9]{3,}|0[0-9]{3})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void gYearMonthTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_G_YEAR_MONTH_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "-?([1-9][0-9]{3,}|0[0-9]{3})-(0[1-9]|1[0-2])(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void gYearTest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_G_YEAR_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "-?([1-9][0-9]{3,}|0[0-9]{3})(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?");
		Assert.assertTrue(condition);
	}
	
	@Test
	public void anyURITest() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromOwl(OWL_FRAGMENT_ANY_URI_DATA_PROPERTY, "TURTLE");
		Boolean condition = shapes.contains(null, ResourceFactory.createProperty(SH_PATTERN_SHAPE), "^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?");
		Assert.assertTrue(condition);
	}
}
