package sharper.generators;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import shaper.model.ShaperModel;

public class OwlShaper implements ShaperModel{
	private static final String SH_PROPERTY = "http://www.w3.org/ns/shacl#property";
	// 0. Query to create initial NodeShapes
	private final String QUERY_FETCH_CLASSES = "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"+
											  "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"+
													 "PREFIX sh: <http://www.w3.org/ns/shacl#>" + 
													 "CONSTRUCT {  ?shapeUrl a sh:NodeShape ;  \n"
													 + "			   		sh:targetClass ?type ;\n"
													 + "			   		sh:deactivated \"false\";\n"
													 // Including data properties
													 + "					sh:property [ \n"
													 + "						sh:path ?dataProperty ;\n"
													 + "						sh:datatype ?datatype ;\n"
													 + "			   	 		sh:name ?dataPropertyName ;\n"
													 + "			   			sh:description ?dataPropertyComment ;\n"
													// + "			   			sh:message \"Error with property\""
													 + "]; "
													 // Including object properties
													 + "				sh:property [ \n"
													 + "						sh:path ?objectProperty ;\n"
													 + "						sh:class ?typeInRange ;"
													 + "			   	 		sh:name ?objectPropertyName ;\n"
													 + "			   			sh:description ?objectPropertyComment ;\n"
													 + "];"
													 
													 + " }"
													 + "WHERE { "
													 + "?type a owl:Class . \n"
													 // Data types extractor
													 + "OPTIONAL { ?dataProperty a owl:DatatypeProperty ;\n"
													 + "		 rdfs:domain ?type ;\n"
													 + "		 rdfs:range ?datatype ."
													 + "		 OPTIONAL {?dataProperty rdfs:label ?dataPropertyName } .\n"
													 + "		 OPTIONAL {?dataProperty rdfs:comment ?dataPropertyComment }.\n"
													 + "}"
													// Object properties extractor
													 + "OPTIONAL { ?objectProperty a owl:ObjectProperty ;\n"
													 + "		 rdfs:domain ?type ;\n"
													 + "		 rdfs:range ?typeInRange .\n"
													 + "		 OPTIONAL {?objectProperty rdfs:label ?objectPropertyName } .\n"
													 + "		 OPTIONAL {?objectProperty rdfs:comment ?objectPropertyComment }.\n"
													 + "		 FILTER (!isBlank(?objectProperty)) ."
													 + "}"
													 
													 + "FILTER (!isBlank(?type)) .\n"
													 + "BIND ( URI(CONCAT(STR(?type),\"Shape\")) AS ?shapeUrl) .\n"
													 + "}";
	
	// 0. Query to create initial NodeShapes
	private final String QUERY_FETCH_DATA_PROPERTIES = "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"+
												 "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"+
												 "PREFIX sh: <http://www.w3.org/ns/shacl#> \n" + 
													 "CONSTRUCT {  ?shapeUrl a sh:PropertyShape .  \n"
													 + "			   ?shapeUrl sh:name ?propertyName .\n"
													 + "			   ?shapeUrl sh:description ?propertyComment .\n"
													 + "			   ?shapeUrl sh:node ?shapeNodeUrl . \n" // triplet to reference NodeShape (domain) of this property
													 + "			   ?shapeUrl sh:path ?propertyDatatype ."
													 + "			   ?shapeUrl sh:datatype ?propertyDatatype ."
													 + " }\n"
													 + "WHERE { \n"
													 + "?dataProperty a owl:DatatypeProperty .\n"
													 + "?dataProperty rdfs:domain ?type .\n"
													// + "?dataProperty rdfs:domain ?type ."
													 + "OPTIONAL { ?dataProperty rdfs:label ?propertyName . }\n"
													 + "OPTIONAL { ?dataProperty rdfs:comment ?propertyComment . }\n"
													 + "FILTER (!isBlank(?type) && !isBlank(?dataProperty)) .\n"
													 + "BIND ( URI(CONCAT(STR(?type),\"Shape\")) AS ?shapeNodeUrl) .\n"
													 + "BIND ( URI(CONCAT(STR(?dataProperty),\"-Shape\")) AS ?shapeUrl) .\n"
													 + "}";

	private final String QUERY_FETCH_OBJECT_PROPERTIES = "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"+
														 "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"+
														 "PREFIX sh: <http://www.w3.org/ns/shacl#> \n" + 
															 "CONSTRUCT {  ?shapeUrl a sh:PropertyShape .  \n"
															 + "			   ?shapeUrl sh:name ?propertyName .\n"
															 + "			   ?shapeUrl sh:description ?propertyComment .\n"
															 + "			   ?shapeUrl sh:node ?shapeNodeUrl . \n" // triplet to reference NodeShape (domain) of this property
															 + "			   ?shapeUrl sh:path ?propertyDatatype .\n"
															 + "			   ?shapeUrl sh:class ?typeInRange .\n"
															 + " }\n"
															 + "WHERE { \n"
															 + "?dataProperty a owl:ObjectProperty .\n"
															 + "?dataProperty rdfs:domain ?type .\n"
															 + "?dataProperty rdfs:range ?typeInRange.\n"
															// + "?dataProperty rdfs:domain ?type ."
															 + "OPTIONAL { ?dataProperty rdfs:label ?propertyName . }\n"
															 + "OPTIONAL { ?dataProperty rdfs:comment ?propertyComment . }\n"
															 + "FILTER (!isBlank(?type) && !isBlank(?dataProperty)) .\n"
															 + "BIND ( URI(CONCAT(STR(?type),\"Shape\")) AS ?shapeNodeUrl) .\n"
															 + "BIND ( URI(CONCAT(STR(?dataProperty),\"-Shape\")) AS ?shapeUrl) .\n"
															 + "}";

	
	public Model fromModelToShapes(String owlUrl) {
		Model ontology = ModelFactory.createDefaultModel();
		ontology.read(owlUrl);
		Model shapes = ModelFactory.createDefaultModel();

		Query queryClasses = QueryFactory.create(QUERY_FETCH_CLASSES);
		QueryExecution qeClasses = QueryExecutionFactory.create(queryClasses, ontology);
		shapes = qeClasses.execConstruct();
		
	
		Query queryDataProperties = QueryFactory.create(QUERY_FETCH_DATA_PROPERTIES);
		QueryExecution qeDataProperties = QueryExecutionFactory.create(queryDataProperties, ontology);
		Model shapesDataProperties = qeDataProperties.execConstruct();
		shapes.add(shapesDataProperties);
		
		Query queryObjectProperties = QueryFactory.create(QUERY_FETCH_DATA_PROPERTIES);
		QueryExecution qeObjectProperties = QueryExecutionFactory.create(queryObjectProperties, ontology);
		Model shapesObjectProperties = qeObjectProperties.execConstruct();
		shapes.add(shapesObjectProperties);
		
		cleanEmptyProperties(shapes);
		
		shapes.write(System.out, "TURTLE");
		return shapes;
		
	}

	/**
	 * This method cleans from the shape the triplets encoding empty lists of properties, i.e., 'URI sh:property []'.
	 * @param shapes a {@link Model} that contains the RDF of the shape
	 */
	private void cleanEmptyProperties(Model shapes) {
		List<Statement> statementsToRemove = new ArrayList<>();
		StmtIterator iterator = shapes.listStatements(null, ResourceFactory.createProperty(SH_PROPERTY), (RDFNode) null);
		while(iterator.hasNext()) {
			Statement rootStatement = iterator.next();
			StmtIterator iteratorOfProperties = shapes.listStatements(rootStatement.getObject().asResource(), null, (RDFNode) null);
			if(!iteratorOfProperties.hasNext()) {
				statementsToRemove.add(rootStatement);
			}
		}
		shapes.remove(statementsToRemove);
		
	}

}
