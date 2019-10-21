package sharper.generators;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import astrea.model.ShaclFromOwl;

public class OwlShaper implements ShaclFromOwl{
	private static final String SH_PROPERTY = "http://www.w3.org/ns/shacl#property";
	private static final String SH_DATATYPE = "http://www.w3.org/ns/shacl#datatype";
	private static final String SH_CLASS = "http://www.w3.org/ns/shacl#class";

	// 0. Query to create initial NodeShapes
	private final String QUERY_FETCH_CLASSES = "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"+
											  "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"+
													 "PREFIX sh: <http://www.w3.org/ns/shacl#>" + 
													 "CONSTRUCT {  ?shapeUrl a sh:NodeShape ;  \n"
													 + "			   		sh:deactivated \"false\";\n"
													 + "			   		sh:closed \"true\";\n"
													 // Including target owl:Class
 													 + "			   		sh:targetClass ?type ;\n"
													 // Including meta-data
													 + "			   		sh:name  ?shapeNodeName; \n"
													 + "			   		rdfs:label  ?shapeNodeName; \n"
													 + "			   		rdfs:label  ?shapeNodeComment; \n"
													 + "			   		rdfs:seeAlso ?shapeNodeSeeAlso; \n"
													 + "			   		rdfs:isDefinedBy ?shapeNodeDefinedBy; \n"
													 // Including data & object properties
													 + "					sh:property ?shapeUrlDataProperty ;"
													 + "					sh:property ?shapeUrlObjectProperty."
													 + " }"
													 + "WHERE { "
													 + "?type a owl:Class . \n"
													 // Meta-data extractor
													 + "OPTIONAL { ?type rdfs:label ?shapeNodeName . } \n"
													 + "OPTIONAL { ?type rdfs:comment ?shapeNodeComment .} \n"
													 + "OPTIONAL { ?type rdfs:seeAlso ?shapeNodeSeeAlso .} \n"
													 + "OPTIONAL { ?type rdfs:isDefinedBy ?shapeNodeDefinedBy .} \n"
													 
													 // Data types extractor
													 + "OPTIONAL { ?dataProperty a owl:DatatypeProperty ;\n"
													 + "		 			rdfs:domain ?type ; }\n"
													 // TODO: consider that we may have owl:unionOf, or any other, as range of the rdfs:domain
												
													// Object properties extractor
													 + " OPTIONAL{ ?objectProperty a owl:ObjectProperty .\n"
													 + "		 ?objectProperty rdfs:domain ?type .\n"
													 + "		 FILTER (!isBlank(?objectProperty)) ."
													 + "}"
													
													  
													 + "FILTER (!isBlank(?type)) .\n"
													 + "BIND ( URI(CONCAT(STR(?type),\"Shape\")) AS ?shapeUrl) .\n"
													 + "BIND ( URI(CONCAT(STR(?dataProperty),\"-Shape\")) AS ?shapeUrlDataProperty) .\n"
													 + "BIND ( URI(CONCAT(STR(?objectProperty),\"-Shape\")) AS ?shapeUrlObjectProperty) .\n"
													 + "}";
	
	private final String QUERY_RESTRICTIONS_FROM_CLASSES = "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"+
			  "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"+
					 "PREFIX sh: <http://www.w3.org/ns/shacl#>" + 
					 "CONSTRUCT {  ?shapeUrl a sh:NodeShape ;  \n"
					 + "					sh:property [ \n"
					 + "					 	a sh:PropertyShape;\n"
					 + "					 	sh:path ?property;\n"
					 + "			    			sh:maxCount ?maxCardinality ;\n" // maxCardinality
					 + "			   			sh:minCount ?minCardinality ;\n" // minCardinality
					 + "			   			sh:maxCount ?cardinality ;\n" // cardinality
					 + "			   			sh:minCount ?cardinality ;\n" // cardinality
					 + "			   			sh:hasValue ?hasValue ;\n" // cardinality
					 + "				]\n"
					 + " }"
					 + "WHERE { "
					 + "?type a owl:Class . \n"
					
					+ " ?type rdfs:subClassOf ?owlPropertyRestriction .\n"
					+ " ?owlPropertyRestriction owl:onProperty ?property .\n"
					+ " 	OPTIONAL { ?owlPropertyRestriction owl:maxCardinality ?maxCardinality . }\n" 	// owl:maxCardinality
					+ " OPTIONAL { ?owlPropertyRestriction owl:minCardinality ?minCardinality .}\n"  	// owl:minCardinality
					+ " 	OPTIONAL	 {  ?owlPropertyRestriction owl:cardinality ?cardinality .}\n"		// owl:cardinality
					+ " 	OPTIONAL	 {  ?owlPropertyRestriction owl:hasValue ?hasValue .}\n"				// owl:hasValue
				
					
					// -- [Cardinality extractor]
					 + "FILTER (!isBlank(?type)) .\n"
					 + "BIND ( URI(CONCAT(STR(?type),\"Shape\")) AS ?shapeUrl) .\n"
					 + "BIND ( URI(CONCAT(CONCAT(STR(?dataProperty),\"-Shape\"),str(?type))) AS ?shapePropertyUrl) .\n"
					 + "}";
	
	// 0. Query to create initial PropertyShapes
	private final String QUERY_FETCH_DATA_PROPERTIES = "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"+
												 "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"+
												 "PREFIX sh: <http://www.w3.org/ns/shacl#> \n" + 
													 "CONSTRUCT {  ?shapeUrl a sh:PropertyShape .  \n"
													 + "			   ?shapeUrl sh:name ?propertyName .\n"
													 + "			   ?shapeUrl sh:description ?propertyComment .\n"
													 + "			   ?shapeUrl sh:node ?shapeNodeUrl . \n" // triplet to reference NodeShape (domain) of this property
													 + "			   ?shapeUrl sh:path ?dataProperty .\n"
													 + "			   ?shapeUrl sh:datatype ?datatype .\n"
													 + " }\n"
													 + "WHERE { \n"
													 + "?dataProperty a owl:DatatypeProperty .\n"
													 + "?dataProperty rdfs:domain ?type .\n"
													 + "	OPTIONAL {?dataProperty rdfs:range ?datatype . }"
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
															 + "			   ?shapeUrl sh:path ?dataProperty .\n"
															 + "			   ?shapeUrl sh:class ?typeInRange .\n"
															// Cardinalities
															 + "			   ?shapeUrl sh:maxCount ?maxCardinality .\n" // maxCardinality
															 + "			   ?shapeUrl sh:minCount ?minCardinality .\n" // minCardinality
															 + "			   ?shapeUrl sh:maxCount ?cardinality .\n" // cardinality
															 + "			   ?shapeUrl sh:minCount ?cardinality .\n" // cardinality
															 + " }\n"
															 + "WHERE { \n"
															 + "?dataProperty a owl:ObjectProperty .\n"
															 + "?dataProperty rdfs:domain ?type .\n"
															 + "?dataProperty rdfs:range ?typeInRange.\n"
															 + "OPTIONAL { ?dataProperty rdfs:label ?propertyName . }\n"
															 + "OPTIONAL { ?dataProperty rdfs:comment ?propertyComment . }\n"
															 + "FILTER (!isBlank(?type) && !isBlank(?dataProperty)) .\n"
															 + "BIND ( URI(CONCAT(STR(?typeInRange),\"Shape\")) AS ?shapeNodeUrl) .\n"
															 + "BIND ( URI(CONCAT(STR(?dataProperty),\"-Shape\")) AS ?shapeUrl) .\n"
															 + "}";

	

	
	public Model fromURL(String owlUrl) {
		Model ontology = ModelFactory.createDefaultModel();
		ontology.read(owlUrl);
		return createShapeFromOntology(ontology);
	}
	
	
	public Model fromOwl(String owlContent, String format) {
		Model ontology = ModelFactory.createDefaultModel();
		InputStream is = new ByteArrayInputStream(owlContent.getBytes() );
		ontology.read(is, null, format); 
		return createShapeFromOntology(ontology);
	}
	
	
	private Model createShapeFromOntology(Model ontology) {
		Model shapes = ModelFactory.createDefaultModel();
		Query queryClasses = QueryFactory.create(QUERY_FETCH_CLASSES);
		QueryExecution qeClasses = QueryExecutionFactory.create(queryClasses, ontology);
		shapes = qeClasses.execConstruct();
		

		Query queryDataProperties = QueryFactory.create(QUERY_FETCH_DATA_PROPERTIES);
		QueryExecution qeDataProperties = QueryExecutionFactory.create(queryDataProperties, ontology);
		Model shapesDataProperties = qeDataProperties.execConstruct();
		shapes.add(shapesDataProperties);
		
		Query queryObjectProperties = QueryFactory.create(QUERY_FETCH_OBJECT_PROPERTIES);
		QueryExecution qeObjectProperties = QueryExecutionFactory.create(queryObjectProperties, ontology);
		Model shapesObjectProperties = qeObjectProperties.execConstruct();
		shapes.add(shapesObjectProperties);
	
		
		Query queryPropertiesFurtherRestrictions = QueryFactory.create(QUERY_RESTRICTIONS_FROM_CLASSES);
		QueryExecution qeFurtherRestrictions = QueryExecutionFactory.create(queryPropertiesFurtherRestrictions, ontology);
		Model shapesFurtherRestrictions = qeFurtherRestrictions.execConstruct();
		shapes.add(shapesFurtherRestrictions);
		
		// Post processing:
		// 0. Clean empty URL sh:property [] patterns
		cleanEmptyProperties(shapes);
		// 1. Add xsd:string to any property that has no data type
		addXsdStringDatatype(shapes);
		// 2. Include bespoke restriction specified in the classes
		
		
		//shapes.write(System.out, "TURTLE");
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
	
	private void addXsdStringDatatype(Model shapes) {
		// TODO: consider there to include this in the NodeShape and the PropertyShape as well
		// 0. For the NodeShape:
		List<Statement> statementsToAdd = new ArrayList<>();
		StmtIterator iterator = shapes.listStatements(null, ResourceFactory.createProperty(SH_PROPERTY), (RDFNode) null);
		while(iterator.hasNext()) {
			Statement rootStatement = iterator.next();
			Resource propertyURI = rootStatement.getObject().asResource();
			// If property does not have xsd datatype nor is a property for an object property
			Boolean conditionDatatypes = shapes.contains(propertyURI, ResourceFactory.createProperty(SH_DATATYPE), (RDFNode) null);
			Boolean conditionIsObjectProperty = shapes.contains(propertyURI, ResourceFactory.createProperty(SH_CLASS), (RDFNode) null);
			if(!conditionDatatypes && !conditionIsObjectProperty) {
				System.out.println();
			}
			
		}
	}



}
