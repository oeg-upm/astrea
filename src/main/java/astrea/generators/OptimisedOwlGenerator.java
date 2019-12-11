package astrea.generators;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;

import astrea.model.ShaclFromOwl;

public class OptimisedOwlGenerator implements ShaclFromOwl{


	private static final String PREFIXES = 	"PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"+
									  		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"+
											"PREFIX sh: <http://www.w3.org/ns/shacl#>\n" + 
											"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"+
											"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n";
	
	// 0. Query to create initial NodeShapes
	private static final String QUERY_CREATE_NODESHAPE = PREFIXES +
													 "CONSTRUCT {  ?shapeUrl a sh:NodeShape ; \n"
													 + "			   		sh:deactivated \"false\";\n"
													 + "			   		sh:closed \"true\";\n"
													 + "			   		sh:nodeKind sh:IRI;\n"
													 // 1. Including target owl:Class
 													 + "			   		sh:targetClass ?type ;\n"
													 // 2. Including meta-data
													 + "			   		sh:name ?shapeNodeName; \n"
													 + "			   		rdfs:label  ?shapeNodeName; \n"
													 + "			   		sh:description ?shapeNodeComment; \n"
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
													 + "				?shapeUrl sh:not ?complementTypeShape . \n"
													 // 9. Including unionOf types
													 + "				?shapeUrl sh:or ?unionTypesList . \n"
													 + "				?unionTypesListRest rdf:first ?unionTypesListHeadShape ; \n"
													 + "					rdf:rest ?unionTypesListTailShape . \n"
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
													 + "OPTIONAL { ?type owl:complementOf ?complementType . \n"
													 + "				BIND ( URI(CONCAT(STR(?complementType),\"Shape\")) AS ?complementTypeShape) .\n"
													 + "				FILTER (!isBlank(?complementType))      \n"
													 + "}\n"
													 // 9. Extracting unionOf types
													 + "OPTIONAL {  ?type owl:unionOf ?unionTypesList .\n"
													 + "		 		?unionTypesList rdf:rest* ?unionTypesListRest .\n" 
													 + "      		?vTypesListRest rdf:first ?unionTypesListHead .\n" 
													 + "       		?unionTypesListRest  rdf:rest ?unionTypesListTail .\n"
													 + "  			BIND ( IF ( ?unionTypesListHead != rdf:nil && !isBlank(?unionTypesListHead), URI(CONCAT(STR(?unionTypesListHead),\"Shape\")), ?unionTypesListHead ) AS ?unionTypesListHeadShape ) \n"
													 + "  			BIND ( IF ( ?unionTypesListTail != rdf:nil && !isBlank(?unionTypesListTail), URI(CONCAT(STR(?unionTypesListTail),\"Shape\")), ?unionTypesListTail ) AS ?unionTypesListTailShape )"
													 + "}"
													 + "FILTER (!isBlank(?type)) .\n"
													 + "BIND ( URI(CONCAT(STR(?type),\"Shape\")) AS ?shapeUrl) .\n"
													 + "BIND ( URI(CONCAT(STR(?property),\"-Shape\")) AS ?shapeUrlPropertyShape) .\n"
													 + "}";
	
	private final String QUERY_INJECT_EMBEDDED_PROPERTIES_IN_NODESHAPES = PREFIXES +
															 "CONSTRUCT {  ?shapeUrl a sh:NodeShape ;  \n"
															 + "					sh:property [ \n"
															// + "					 	a sh:PropertyShape;\n"
															 + "					 	sh:path ?property;\n"
														//UNCOMMENT + "					 	sh:path ?propertyChain;\n"
															 + "					 	?variableRange ?valuesInRange;\n"
															// -- 1. Cardinality injector
															 + "			    			sh:maxCount ?maxCardinality ;\n" // maxCardinality
															 + "			   			sh:minCount ?minCardinality ;\n" // minCardinality
															 + "			   			sh:maxCount ?cardinality ;\n" // cardinality
															 + "			   			sh:minCount ?cardinality ;\n" // cardinality
															 + "			   			sh:hasValue ?hasValue ;\n" // cardinality
															 + "				];\n"
															 // -- 2. Inserting  complementary properties
															  + "				sh:not [ \n"
														//	 + "					 	a sh:PropertyShape;\n"
															 + "					 	sh:path ?complementaryProperty;\n"
															 + "					 	sh:hasValue ?complementaryPropertyRangeShape;\n"
															 + "				];\n"
															// -- 3. Cardinality injector
															 + "					sh:property [ \n"
														//	 + "					 	a sh:PropertyShape;\n"
															 + "			    			sh:qualifiedMaxCount ?maxQualifiedCardinality ;\n" 
															 + "			   			sh:qualifiedMinCount ?minQualifiedCardinality ;\n"
															 + "			   			sh:qualifiedValueShape ?qualifiedCardinality ;\n" // cardinality
															 + "			   			sh:path ?hasTypeOfCardinality ;\n" // cardinality
															 + "]"
															//
															 + " } WHERE { "
															 + "			?type a  ?typeClassUrl . \n"
															 + "			VALUES ?typeClassUrl {owl:Class rdfs:Class}\n"
															 + " 		OPTIONAL {?type rdfs:subClassOf ?owlPropertyRestriction .\n"
															 + " 			 ?owlPropertyRestriction a owl:Restriction ."	
															 + " 			 ?owlPropertyRestriction owl:onProperty ?property . \n"
															 + "				 OPTIONAL{ ?owlPropertyRestriction owl:allValuesFrom ?valuesInRange ."
															 + "  			 	BIND ( IF ( STRSTARTS(str(?valuesInRange),\"http://www.w3.org/2001/XMLSchema#\")"/* || ... */+ ", sh:datatype, sh:class ) AS ?variableRange ) \n"
															 + "				 }\n."
															 +" 		   }\n"		
															// -- 1. Cardinality extractor
															+ " 	OPTIONAL { ?owlPropertyRestriction owl:maxCardinality ?maxCardinality . }\n" 	// owl:maxCardinality
															+ " OPTIONAL { ?owlPropertyRestriction owl:minCardinality ?minCardinality .}\n"  	// owl:minCardinality
															+ " 	OPTIONAL	 {  ?owlPropertyRestriction owl:cardinality ?cardinality .}\n"		// owl:cardinality
															+ " 	OPTIONAL	 {  ?owlPropertyRestriction owl:hasValue ?hasValue .}\n"				// owl:hasValue
															 // 2. Extracting complementary properties
															 + "OPTIONAL { ?type owl:complementOf ?owlPropertyRestriction . \n"
															 + "			   ?owlPropertyRestriction owl:onProperty ?complementaryProperty ."
															 + "			   ?owlPropertyRestriction owl:hasValue ?complementaryPropertyRange"
															 + "				BIND ( URI(CONCAT(STR(?complementaryPropertyRange),\"Shape\")) AS ?complementaryPropertyRangeShape) .\n"
															 + "}\n"
															// 3. Cualified cardinalities
															 + " OPTIONAL { ?owlPropertyRestriction owl:maxQualifiedCardinality ?maxQualifiedCardinality . }\n" 	
															 + " OPTIONAL { ?owlPropertyRestriction owl:minQualifiedCardinality ?minQualifiedCardinality .}\n"  
															 + " OPTIONAL {  ?owlPropertyRestriction owl:qualifiedCardinality ?qualifiedCardinality .}\n"		
															 + " OPTIONAL {  ?owlPropertyRestriction owl:onClass ?hasTypeOfCardinality .}\n"			
															 //
															 
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
														 + "		VALUES ?propertyType {owl:DatatypeProperty rdfs:Datatype} " // TODO: BEFORE THIS TYPE WAS IN THE LIST, rdf:Property 
														
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
											 + "		VALUES ?propertyType {owl:ObjectProperty }" // todo: before rdf:Property was in this list
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
			
	// Post processing queries
		private static final String QUERY_REMOVE_EMPTY_NODES = PREFIXES 
				 + "DELETE { \n"
				 + "			   ?subject ?predicate ?blankNode .\n"
				 + " }\n"
				 + "WHERE { \n"
				 + "	    ?subject ?predicate ?blankNode .\n"
				 +" 	 	FILTER NOT EXISTS { ?blankNode ?property ?range } .\n"
				 +"	 	FILTER (isBlank(?blankNode)) .\n"
				 + "}";
		// Post processing queries
				private static final String QUERY_INSERT_EMBEDDED_PROPERTYSHAPE_TYPE = PREFIXES 
						 + "INSERT { \n"
						 + "			  ?blankNode a sh:PropertyShape.\n"
						 + " }\n"
						 + "WHERE { \n"
						 + "	    ?subject ?predicate ?blankNode .\n"
						 + "	    ?blankNode ?property ?range .\n"
						 +"	 	FILTER ( STRSTARTS(str(?predicate), \"http://www.w3.org/ns/shacl#\") ) .\n"
						 + "}";
						
	
	@Override
	public Model fromURLs(List<String> owlUrls) {
		Model ontology = ModelFactory.createDefaultModel();
		if(owlUrls!=null && !owlUrls.isEmpty()) {
			for(int index=0; index < owlUrls.size(); index++) {
				String owlUrl = owlUrls.get(index);
				Model ontologyTemporal = ModelFactory.createDefaultModel();
				ontologyTemporal.read(owlUrl);
				ontology.add(ontologyTemporal);
			}
		}
		return fromModel(ontology);
	}

			
	@Override
	public Model fromURL(String owlUrl) {
		Model ontology = ModelFactory.createDefaultModel();
		ontology.read(owlUrl);
		return fromModel(ontology);
	}
	
	
	@Override
	public Model fromOwl(String owlContent, String format) {
		Model ontology = ModelFactory.createDefaultModel();
		InputStream is = new ByteArrayInputStream(owlContent.getBytes() );
		ontology.read(is, null, format); 
		return fromModel(ontology);
	}
	
	
	public Model fromModel(Model ontology) {
		importOntologies(ontology);
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
		cleanEmptyGraphs(shapes);
		
		// 2. Remove inconsistencies, if a subject has a sh:path and sh:inversePah, remove the former
		removePathInconsistencies(shapes);
		// 3. Embedded the sh:PropertyShape types 
		embeddedPropertyTypes(shapes);
		//shapes.write(System.out, "TURTLE");
		return shapes;
	}

	


	



	private void embeddedPropertyTypes(Model shapes) {
		UpdateRequest request = UpdateFactory.create() ;
		request.add(QUERY_INSERT_EMBEDDED_PROPERTYSHAPE_TYPE);
		UpdateAction.execute(request, shapes) ;
		
	}


	private void importOntologies(Model ontology) {
		NodeIterator iterator = ontology.listObjectsOfProperty(ResourceFactory.createProperty("http://www.w3.org/2002/07/owl#imports"));
		while(iterator.hasNext()) {
			RDFNode ontologyUrl = iterator.next();
			ontology.read(ontologyUrl.toString());
		}
	}

	private void cleanEmptyGraphs(Model shapes) {
		UpdateRequest request = UpdateFactory.create() ;
		request.add(QUERY_REMOVE_EMPTY_NODES);
		UpdateAction.execute(request, shapes) ;
	}
	
	private void removePathInconsistencies(Model shapes) {
		UpdateRequest request = UpdateFactory.create() ;
		request.add(QUERY_REMOVE_PATH_PROPERTY_INCONSISTENCIES);
		UpdateAction.execute(request, shapes) ;
		
	}






}
