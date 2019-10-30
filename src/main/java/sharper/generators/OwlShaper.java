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
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;

import astrea.model.ShaclFromOwl;

public class OwlShaper implements ShaclFromOwl{
	private static final String SH_PROPERTY = "http://www.w3.org/ns/shacl#property";
	private static final String SH_DATATYPE = "http://www.w3.org/ns/shacl#datatype";
	private static final String SH_CLASS = "http://www.w3.org/ns/shacl#class";

	private static final String PREFIXES = 	"PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"+
									  		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"+
											"PREFIX sh: <http://www.w3.org/ns/shacl#>\n" + 
											"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"+
											"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"+
											"PREFIX apf: <http://jena.hpl.hp.com/ARQ/property#>\n";
	
	// 0. Query to create initial NodeShapes
	private static final String QUERY_CREATE_NODESHAPE = PREFIXES +
													 "CONSTRUCT {  ?shapeUrl a sh:NodeShape ; \n"
													 + "			   		sh:deactivated \"false\";\n"
													 + "			   		sh:closed \"true\";\n"
													 + "			   		sh:nodeKind sh:IRI;\n"
													 // 1. Including target owl:Class
 													 + "			   		sh:targetClass ?type ;\n"
													 // 2. Including meta-data
													 + "			   		sh:name  ?shapeNodeName; \n"
													 + "			   		rdfs:label  ?shapeNodeName; \n"
													 + "			   		rdfs:label  ?shapeNodeComment; \n"
													 + "			   		rdfs:seeAlso ?shapeNodeSeeAlso; \n"
													 + "			   		rdfs:isDefinedBy ?shapeNodeDefinedBy; \n"
													 // 3. Including data & object properties
													 + "					sh:property ?shapeUrlPropertyShape;\n"
													 + "					sh:property ?shapeUrlObjectProperty;\n"
													 // 4. Including intersected types
													 + "					sh:and ?intersectedTypesList . \n"
													 + "				?intersectedTypesListRest rdf:first ?intersectedTypesListHeadShape ; \n"
													 + "					rdf:rest ?intersectedTypesListTailShape . \n"
													// 5. Injecting inclusion types
													 + "				?shapeUrl sh:in ?inclusionTypesList . \n"
													 + "				?inclusionTypesListRest rdf:first ?inclusionTypesListHeadShape ; \n"
													 + "					rdf:rest ?inclusionTypesListTailShape . \n"
													 // 6. Including equivalent classes
													 + "				?shapeUrl sh:equals ?sameAsType . \n"
													 // 7. Including disjoint classes
													 + "				?shapeUrl sh:disjoint ?disjointType . \n"
													 // 8. Including complemented types
													 + "				?shapeUrl sh:not ?complementTypesList . \n"
													 + "				?complementTypesListRest rdf:first ?complementTypesListHeadShape ; \n"
													 + "					rdf:rest ?complementTypesListTailShape . \n"
													 + " } WHERE { "
													 // 1. Extracting the name of the owl:Class
													 + "?type a  ?typeClassUrl . \n"
													 + "VALUES ?typeClassUrl {owl:Class rdfs:Class} .\n"
													 // 2. Meta-data extractor
													 + "OPTIONAL { ?type rdfs:label ?shapeNodeName . } \n"
													 + "OPTIONAL { ?type rdfs:comment ?shapeNodeComment .} \n"
													 + "OPTIONAL { ?type rdfs:seeAlso ?shapeNodeSeeAlso .} \n"
													 + "OPTIONAL { ?type rdfs:isDefinedBy ?shapeNodeDefinedBy .} \n"
													 // 3. Data & Object properties types extractor
														// TODO: consider that we may have owl:unionOf, or any other, as range of the rdfs:domain
													 + "OPTIONAL { ?property a  ?propertyType;\n"
													 + "		 			rdfs:domain ?type ; \n"
													 + " 	 VALUES ?propertyType { owl:ObjectProperty owl:DatatypeProperty rdf:Property} .\n"
													 + "		 FILTER (!isBlank(?property)) ."
													 + "}"
													 // 4. Extracting intersecting types
													 + "OPTIONAL { ?type owl:intersectionOf ?intersectedTypesList .\n"
													 + "		 		?intersectedTypesList rdf:rest* ?intersectedTypesListRest .\n" 
													 + "      		?intersectedTypesListRest rdf:first ?intersectedTypesListHead .\n" 
													 + "       		?intersectedTypesListRest  rdf:rest ?intersectedTypesListTail .\n"
													 + "  			BIND ( IF ( ?intersectedTypesListHead != rdf:nil && !isBlank(?intersectedTypesListHead), URI(CONCAT(STR(?intersectedTypesListHead),\"Shape\")), ?intersectedTypesListHead ) AS ?intersectedTypesListHeadShape ) \n"
													 + "  			BIND ( IF ( ?intersectedTypesListTail != rdf:nil && !isBlank(?intersectedTypesListTail), URI(CONCAT(STR(?intersectedTypesListTail),\"Shape\")), ?intersectedTypesListTail ) AS ?intersectedTypesListTailShape )"
													 + "}"
													 // 5. Extracting inclusion types
													 + "OPTIONAL { ?type owl:oneOf ?inclusionTypesList .\n"
													 + "		 		?inclusionTypesList rdf:rest* ?inclusionTypesListRest .\n" 
													 + "      		?inclusionTypesListRest rdf:first ?inclusionTypesListHead .\n" 
													 + "       		?inclusionTypesListRest  rdf:rest ?inclusionTypesListTail .\n"
													 + "  			BIND ( IF ( ?inclusionTypesListHead != rdf:nil && !isBlank(?inclusionTypesListHead), URI(CONCAT(STR(?inclusionTypesListHead),\"Shape\")), ?inclusionTypesListHead ) AS ?inclusionTypesListHeadShape ) \n"
													 + "  			BIND ( IF ( ?inclusionTypesListTail != rdf:nil && !isBlank(?inclusionTypesListTail), URI(CONCAT(STR(?inclusionTypesListTail),\"Shape\")), ?inclusionTypesListTail ) AS ?inclusionTypesListTailShape )"
													 + "}"
													 // 6. Extracting equivalent classes
													 + "OPTIONAL { ?type owl:equivalentClass ?sameAsType . } \n"
													 // 7. Extracting disjoint classes
													 + "OPTIONAL { ?type owl:disjointWith ?disjointType . } \n"
													 // 8. Extracting complemented types
													 + "OPTIONAL { ?type owl:complementOf ?complementTypesList .\n"
													 + "		 		?complementTypesList rdf:rest* ?complementTypesListRest .\n" 
													 + "      		?complementTypesListRest rdf:first ?complementTypesListHead .\n" 
													 + "       		?complementTypesListRest  rdf:rest ?complementTypesListTail .\n"
													 + "  			BIND ( IF ( ?complementTypesListHead != rdf:nil && !isBlank(?complementTypesListHead), URI(CONCAT(STR(?complementTypesListHead),\"Shape\")), ?complementTypesListHead ) AS ?complementTypesListHeadShape ) \n"
													 + "  			BIND ( IF ( ?complementTypesListTail != rdf:nil && !isBlank(?complementTypesListTail), URI(CONCAT(STR(?complementTypesListTail),\"Shape\")), ?complementTypesListTail ) AS ?complementTypesListTailShape )"
													 + "}"
													 + "FILTER (!isBlank(?type)) .\n"
													 + "BIND ( URI(CONCAT(STR(?type),\"Shape\")) AS ?shapeUrl) .\n"
													 + "BIND ( URI(CONCAT(STR(?property),\"-Shape\")) AS ?shapeUrlPropertyShape) .\n"
													 + "}";
	
	private final String QUERY_INJECT_EMBEDDED_PROPERTIES_IN_NODESHAPES = PREFIXES +
															 "CONSTRUCT {  ?shapeUrl a sh:NodeShape ;  \n"
															 + "					sh:property [ \n"
															 + "					 	a sh:PropertyShape;\n"
															 + "					 	sh:path ?property;\n"
															 + "					 	sh:path ?propertyChain;\n"
															 + "					 	sh:class ?valuesInRange;\n"
															 + "					 	sh:datatype ?valuesInRange;\n"
															// -- Cardinality injector
															 + "			    			sh:maxCount ?maxCardinality ;\n" // maxCardinality
															 + "			   			sh:minCount ?minCardinality ;\n" // minCardinality
															 + "			   			sh:maxCount ?cardinality ;\n" // cardinality
															 + "			   			sh:minCount ?cardinality ;\n" // cardinality
															 + "			   			sh:hasValue ?hasValue ;\n" // cardinality
															 + "				]\n"
															 + " } WHERE { "
															 + "			?type a  ?typeClassUrl . \n"
															 + "			VALUES ?typeClassUrl {owl:Class rdfs:Class}\n"
															 + " 		?type rdfs:subClassOf ?owlPropertyRestriction .\n"
															 +" 			?owlPropertyRestriction a owl:Restriction"	
															 + " 		OPTIONAL{ ?owlPropertyRestriction owl:onProperty ?property .}\n"
															 + "			OPTIONAL{ ?owlPropertyRestriction owl:allValuesFrom ?valuesInRange}\n."
															
															// -- Cardinality extractor
															+ " 	OPTIONAL { ?owlPropertyRestriction owl:maxCardinality ?maxCardinality . }\n" 	// owl:maxCardinality
															+ " OPTIONAL { ?owlPropertyRestriction owl:minCardinality ?minCardinality .}\n"  	// owl:minCardinality
															+ " 	OPTIONAL	 {  ?owlPropertyRestriction owl:cardinality ?cardinality .}\n"		// owl:cardinality
															+ " 	OPTIONAL	 {  ?owlPropertyRestriction owl:hasValue ?hasValue .}\n"				// owl:hasValue
															// -- [Cardinality extractor]
															 + "FILTER (!isBlank(?type)) .\n"
															 + "BIND ( URI(CONCAT(STR(?type),\"Shape\")) AS ?shapeUrl) .\n"
															 + "}";
	

	
	private final String QUERY_CREATE_PROPERTYSHAPES = PREFIXES +
															 "CONSTRUCT {  ?shapeUrl a sh:PropertyShape .\n"
															 
															 + "			   ?shapeUrl sh:path ?property .\n"
															 // B. Including equivalences 
															 + "			   ?shapeUrl sh:equivalent ?equivalentProperty .\n"
															 // C. Including meta-data
															 + "			   ?shapeUrl sh:name  ?propertyName .\n"
															 + "			   ?shapeUrl rdfs:label  ?propertyName .\n"
															 + "			   ?shapeUrl sh:description  ?propertyComment .\n"
															 + "			   ?shapeUrl rdfs:label  ?propertyComment .\n"
															 + "			   ?shapeUrl rdfs:seeAlso ?shapePropertySeeAlso .\n"
															 + "			   ?shapeUrl rdfs:isDefinedBy ?shapePropertyDefinedBy .\n"
															 // A. Including NodeShapes related to this PropertyShape
															 + "			   ?shapeUrl	 sh:node ?shapeNodeUrl .\n" // triplet to reference NodeShape (domain) of this property, extracrted form domain
															 + "			   ?shapeUrl	 sh:node ?typesUnited .\n" // triplet to reference NodeShape (domain) of this property, extracted from unionOf
															 // D. Including inverse path
															 + "			   ?shapeUrl sh:inversePath ?inversePath .\n"
															 + " } WHERE { \n"
															 + "		 ?property a ?propertyType .\n"
															 + "		 VALUES ?propertyType {owl:ObjectProperty owl:DatatypeProperty rdf:Property rdfs:Datatype } .\n"
															 + "		 MINUS { ?propertyType a owl:InverseFunctionalProperty } .\n"
															 + "		 MINUS { ?propertyType a owl:FunctionalProperty } .\n"
															 // A. Extracting the domain to reference the NodeShape related to this PropertyShape
															 + "		OPTIONAL { ?property rdfs:domain ?typeInRange . "
															 + "					FILTER(!isBlank(?typeInRange))"
															 + "    }\n"
															 + "		OPTIONAL { ?property rdfs:domain ?blankType .\n"
															 + "				  ?blankType owl:unionOf ?typesUnited ."
															 + "				  ?typesUnited rdf:rest*/rdf:first ?typeInRange . }\n"
															 // B. Equivalences extractor
															 + "		OPTIONAL { ?property owl:equivalentProperty ?equivalentProperty . }\n"
															 // C. Meta-data extractor
															 + "		OPTIONAL { ?property rdfs:label ?propertyName . }\n"
															 + "		OPTIONAL { ?property rdfs:comment ?propertyComment . }\n"
															 + "		OPTIONAL { ?property rdfs:seeAlso ?shapePropertySeeAlso .} \n"
															 + "		OPTIONAL { ?property rdfs:isDefinedBy ?shapePropertyDefinedBy .} \n"
															 // D. Extracting inverse paths
															 + "		OPTIONAL { ?property owl:inverseOf ?inversePath .} \n"
															 // 
															 + "		FILTER (!isBlank(?property)) .\n"
															 + "		BIND ( URI(CONCAT(STR(?typeInRange),\"Shape\")) AS ?shapeNodeUrl) .\n"
															 + "		BIND ( URI(CONCAT(STR(?property),\"-Shape\")) AS ?shapeUrl) .\n"
															 + "}";

	
	private static final String QUERY_INCLUDE_UNIQUE_RESTRICTION_TO_PROPERTYSHAPE = PREFIXES +
				 "CONSTRUCT { \n"
				 + "			   ?shapeUrl sh:maxCount 1 .\n"
				 + "			   ?shapeUrl sh:path ?property .\n"
				 + " }\n"
				 + "WHERE { \n"
				 + "?property a owl:FunctionalProperty .\n"
				 + "BIND ( URI(CONCAT(STR(?property),\"-Shape\")) AS ?shapeUrl) .\n"
				 + "}";

	private static final String QUERY_INCLUDE_UNIQUE_RESTRICTION_TO_PROPERTYSHAPE_2 = PREFIXES 
			+ "CONSTRUCT { \n"
			 + "			   ?shapeUrl sh:maxCount 1 .\n"
			 + "			   ?shapeUrl sh:inversePath ?property .\n"
			 + " }\n"
			 + "WHERE { \n"
			 + "?property a owl:InverseFunctionalProperty .\n"
			 + "BIND ( URI(CONCAT(STR(?property),\"-Shape\")) AS ?shapeUrl) .\n"
			 + "}";

	private final String QUERY_FETCH_DATA_PROPERTIES = PREFIXES 
														 + "CONSTRUCT {  "
														 + "			   ?shapeUrl sh:datatype  ?typeInRange ; \n" 
														 + "			    			 sh:class  ?typeInRange ; \n"
														 + "						 sh:nodeKind sh:Literal ; \n" 
														 // 1. Including restrictions
														 // 		1.1 Including pattern
														 + "						 sh:pattern  ?restrictionPattern  ; \n" 
														 // 		1.2 Including lang
														 + "						 sh:languageIn  ?restrictionLang ; \n" 
														 // 		1.3 Including minlength
														 + "						 sh:minLength  ?restrictionMin  ; \n" 
														 // 		1.4 Including maxlength
														 + "						 sh:maxLength  ?restrictionMax  ; \n" 
														 // 		1.5 Including length
														 + "						 sh:maxLength  ?restrictionLength  ; \n" 
														 + "						 sh:minLength  ?restrictionLength  ; \n" 
														 //  	1.6 Including minExclusive
														 + "						 sh:minExclusive  ?restrictionMinExclusive  ; \n" 
														 //  	1.7 Including maxExclusive
														 + "						 sh:maxExclusive  ?restrictionMaxExclusive  ; \n" 
														  //  	1.8 Including maxExclusive
														 + "						 sh:minInclusive  ?restrictionMinInclusive  ; \n" 
														  //  	1.9 Including maxExclusive
														 + "						 sh:maxInclusive  ?restrictionMaxInclusive  ; \n" 
														 + " } WHERE { \n"
														 + "		?property a ?propertyType .\n"
														 + "		VALUES ?propertyType {owl:DatatypeProperty rdf:Property rdfs:Datatype}"
														 + "		OPTIONAL { ?property rdfs:range ?typeInRange. }\n"
														 // 1. Extracting the owl:withRestrictions
														 + "		OPTIONAL { ?property owl:withRestrictions ?restrictionsList . \n"
														 + "					?restrictionsList rdf:rest*/rdf:first ?restrictionElement . \n"
														 //					1.1 Extract pattern
														 + "					OPTIONAL { ?restrictionElement xsd:pattern ?restrictionPattern . } \n"
														 //					1.2 Extact lang
														 + "					OPTIONAL { ?restrictionElement rdf:langRange ?restrictionLang . } \n"
														 //					1.3 Extact min
														 + "					OPTIONAL { ?restrictionElement xsd:minLength ?restrictionMin . } \n"
														 //					1.4 Extact max
														 + "					OPTIONAL { ?restrictionElement xsd:maxLength ?restrictionMax . } \n"
														  //					1.5 Extact length
														 + "					OPTIONAL { ?restrictionElement xsd:length ?restrictionLength . } \n"
														 //					1.6 Extact minExclusive
														 + "					OPTIONAL { ?restrictionElement xsd:minExclusive ?restrictionMinExclusive . } \n"
														 //					1.7 Extact maxExclusive
														 + "					OPTIONAL { ?restrictionElement xsd:maxExclusive ?restrictionMaxExclusive . } \n"
														 //					1.8 Extact maxExclusive
														 + "					OPTIONAL { ?restrictionElement xsd:minInclusive ?restrictionMinInclusive . } \n"
														 //					1.9 Extact maxExclusive
														 + "					OPTIONAL { ?restrictionElement xsd:maxInclusive ?restrictionMaxInclusive . } \n"
														 
														 
														 + "}\n"
														 
														 + "		FILTER (!isBlank(?property)) .\n"
														// + "BIND ( URI(CONCAT(STR(?typeInRange),\"Shape\")) AS ?shapeNodeUrl) .\n"
														 + "		BIND ( URI(CONCAT(STR(?property),\"-Shape\")) AS ?shapeUrl) .\n"
														 + "}";

	private final String QUERY_FETCH_OBJECT_PROPERTIES = PREFIXES 
											 + "CONSTRUCT {  "
											 + "			   ?shapeUrl sh:class  ?typeInRange ; \n" 
											 + "						 sh:nodeKind sh:BlankNodeOrIRI . \n" 
											 + " } WHERE { \n"
											 + "		?property a ?propertyType .\n"
											 + "		VALUES ?propertyType {owl:ObjectProperty rdf:Property}"
											 + "		OPTIONAL { ?property rdfs:range ?typeInRange. }\n"
											 + "		OPTIONAL { ?property rdfs:range ?typeInRangeBlank . "
											 + "					?typeInRangeBlank owl:unionOf ?typeInRange ."
											 + "		}\n"
											 + "		FILTER (!isBlank(?property)) .\n"
											// + "BIND ( URI(CONCAT(STR(?typeInRange),\"Shape\")) AS ?shapeNodeUrl) .\n"
											 + "		FILTER (!isBlank(?typeInRange)) .\n"
											 + "		BIND ( URI(CONCAT(STR(?property),\"-Shape\")) AS ?shapeUrl) .\n"
											 + "}";
			
	// Post processing queries
	private static final String QUERY_REMOVE_PATH_PROPERTY_INCONSISTENCIES = PREFIXES 
			 + "DELETE { \n"
			 + "			   ?shapeUrl sh:path ?property .\n"
			 + " }\n"
			 + "WHERE { \n"
			 + "			   ?shapeUrl sh:maxCount 1 .\n"
			 + "			   ?shapeUrl sh:inversePath ?property .\n"
			 + "			   ?shapeUrl sh:path ?property .\n"
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
		// 0. Build NodeShapes
		Query queryNodeShapes = QueryFactory.create(QUERY_CREATE_NODESHAPE);
		Model nodeShapes = QueryExecutionFactory.create(queryNodeShapes, ontology).execConstruct();
		shapes.add(nodeShapes);
		
		// 1. Include property embedded restrictions into the NodeShapes
		Query queryNodeShapesEmbeddedProperties = QueryFactory.create(QUERY_INJECT_EMBEDDED_PROPERTIES_IN_NODESHAPES);
		Model shapesNodeShapesWithEmbeddedProperties = QueryExecutionFactory.create(queryNodeShapesEmbeddedProperties, ontology).execConstruct();
		shapes.add(shapesNodeShapesWithEmbeddedProperties);
		// 2. Create PropertyShapes, regardless if they refer data or object properties
		Query queryPropertyShapes = QueryFactory.create(QUERY_CREATE_PROPERTYSHAPES);
		Model shapesPropertyShapes = QueryExecutionFactory.create(queryPropertyShapes, ontology).execConstruct();
		shapes.add(shapesPropertyShapes);
		// 3. Enhance PropertyShapes that are always unique
		Query queryPropertiesShapesUnique = QueryFactory.create(QUERY_INCLUDE_UNIQUE_RESTRICTION_TO_PROPERTYSHAPE);
		Model shapesPropertiesShapesUnique = QueryExecutionFactory.create(queryPropertiesShapesUnique, ontology).execConstruct();
		shapes.add(shapesPropertiesShapesUnique);
		// 3.1
		Query queryPropertiesShapesUnique2 = QueryFactory.create(QUERY_INCLUDE_UNIQUE_RESTRICTION_TO_PROPERTYSHAPE_2);
		Model shapesPropertiesShapesUnique2 = QueryExecutionFactory.create(queryPropertiesShapesUnique2, ontology).execConstruct();
		shapes.add(shapesPropertiesShapesUnique2);
//		
//		
//		
		Query queryPropertyShapesOPEnhancement = QueryFactory.create(QUERY_FETCH_OBJECT_PROPERTIES);
		Model shapesPropertyShapesEnhancedWithOP = QueryExecutionFactory.create(queryPropertyShapesOPEnhancement, ontology).execConstruct();
		shapes.add(shapesPropertyShapesEnhancedWithOP);
//		
		Query queryPropertyShapesDPEnhancement = QueryFactory.create(QUERY_FETCH_DATA_PROPERTIES);
		Model shapesPropertyShapesEnhancedWithDP = QueryExecutionFactory.create(queryPropertyShapesDPEnhancement, ontology).execConstruct();
		shapes.add(shapesPropertyShapesEnhancedWithDP);
		
		
		// TODO: Link all NodeShapes with the PropertyShapes that belong to another NodeShape that describes a super class of the class referred by the former NodeShape 
		
		// Post processing:
		// 0. Clean empty URL sh:property [] patterns
		cleanEmptyProperties(shapes);
		// 1. Add xsd:string to any property that has no data type
		addXsdStringDatatype(shapes); // TODO
		// 2. Remove inconsistencies, if a subject has a sh:path and sh:inversePah, remove the former
		removePathInconsistencies(shapes);
		
		//shapes.write(System.out, "TURTLE");
		return shapes;
	}

	


	
	private void removePathInconsistencies(Model shapes) {
		UpdateRequest request = UpdateFactory.create() ;
		request.add(QUERY_REMOVE_PATH_PROPERTY_INCONSISTENCIES);
		UpdateAction.execute(request, shapes) ;
		
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
