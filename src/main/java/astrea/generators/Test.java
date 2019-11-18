package astrea.generators;

import astrea.model.ShaclFromOwl;

public class Test {

	public static String query = "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" + 
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" + 
			"PREFIX sh: <http://www.w3.org/ns/shacl#>\n" + 
			"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
			"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" + 
			"\n" + 
			"CONSTRUCT {\n" + 
			"  ?shapeUrl a sh:PropertyShape .\n" + 
			"  ?shapeUrl sh:pattern '(\\\\+|-)?([0-9]+(\\\\.[0-9]*)?|\\\\.[0-9]+)([Ee](\\\\+|-)?[0-9]+)?|(\\\\+|-)?INF|NaN'^^xsd:string. \n" +
			" } WHERE { \n" + 
			" 	?property a ?propertyType .\n" + 
			"	VALUES ?propertyType {owl:DatatypeProperty rdfs:Datatype}\n" + 
			" 	OPTIONAL { \n" + 
			" 		?property owl:withRestrictions ?restrictionsList .\n" + 
			" 		?restrictionsList rdf:rest*/rdf:first ?restrictionElement .\n" + 
			" 		FILTER NOT EXISTS { ?restrictionElement xsd:pattern ?restrictionPattern . }\n" + 
			" 	}\n" +
			"	?property rdfs:range xsd:decimal . \n" +
			" 	FILTER (!isBlank(?property)) .\n" + 
			" 	BIND ( URI(CONCAT(STR(?property),'-Shape')) AS ?shapeUrl) .\n" +
			"}";
	// patterns https://www.w3.org/TR/xmlschema11-2/#decimal
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long startTime = System.currentTimeMillis();
		System.out.println(query);
		
		OwlGenerator generator = new OwlGenerator();
	
		generator.fromURL("https://www.w3.org/2006/time").write(System.out,"TURTLE");
		 long stopTime = System.currentTimeMillis();
	      long elapsedTime = stopTime - startTime;
	      System.out.println(elapsedTime);
	}

}
