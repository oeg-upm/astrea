package astrea.generators;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import astrea.model.ShaclFromOwl;

/**
 * This class implements the {@link ShaclFromOwl} interface.<p> 
 * It retrieves the queries that allow the transformation from owl to SHACL. It can be provided with the public official Astrea <a href="https://astrea.linkeddata.es/">SPARQL</a> endpoints (by default), or any other as far as it follows the same structure. In addition, it could be provided with a local file containing the Astrea's RDF, like the published in our <a href="https://github.com/oeg-upm/Astrea">GitHub</a>.<p>
 * This class offers the functionality of Astrea considering always the latest version of the transformation queries
 * @author Andrea Cimmino
 *
 */
public class OwlGenerator implements ShaclFromOwl{

	// -- Attributes
	
	private List<String> queries;
	private String endpoint = "https://astrea.helio.linkeddata.es/sparql";
	private static final String QUERY_FETCH_SPARQL = "PREFIX ast: <https://w3id.org/def/astrea#>\nSELECT distinct ?query WHERE {\n  ?sub a ast:SPARQLQuery .\n  ?sub ast:body ?query .\n}";
	private Logger log = Logger.getLogger(OwlGenerator.class.getName());

	
	// -- Constructors
	
	/**
	 * This constructor relies on the public available <a href="https://astrea.linkeddata.es/">dataset</a> of Astrea.
	 */
	public OwlGenerator() {
		queries = new ArrayList<>();
		fetchQueries();
		
	}
	
	/**
	 * This constructor could receive any other version of the Astrea's dataset published in a SPARQL endpoint, as far as it follows the Astrea's dataset model.
	 * @param endpoint a SPARQL enpoint url
	 */

	public OwlGenerator(String endpoint) {
		queries = new ArrayList<>();
		fetchQueries();
		this.endpoint = endpoint;
	}
	
	
	
	/**
	 * @return the endpoint of the Knowledge Graph contaning the queries to translate from OWL to SHACL
	 */
	public String getEndpoint() {
		return endpoint;
	}

	/**
	 * @param endpoint the endpoint to set the Knowledge Graph contaning the queries to translate from OWL to SHACL
	 */
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	/**
	 * This constructor fetches the queries from the provided SPARQL endpoint
	 */
	private void fetchQueries() {
		
		Query query = QueryFactory.create(QUERY_FETCH_SPARQL);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query);

		ResultSet results = qexec.execSelect();
		while(results.hasNext()) {
			QuerySolution qSol = results.next();
			String queryFetched  = qSol.get("?query").asLiteral().getString();
			
			if(queryFetched.length()>1) {
				queries.add(queryFetched);					
			}
		}

		qexec.close();
	}
	
	/**
	 * This constructor fetches the queries from the provided RDF dataset
	 * @param rdfDataset a File containing a RDF dataset with the Astrea queriess
	 */
	public OwlGenerator(File rdfDataset) {
		Model model = ModelFactory.createDefaultModel() ;
		model.read(rdfDataset.getAbsolutePath()) ;
		
		Query query = QueryFactory.create(QUERY_FETCH_SPARQL);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);

		ResultSet results = qexec.execSelect();
		while(results.hasNext()) {
			QuerySolution qSol = results.next();
			String queryFetched  = qSol.get("?query").asLiteral().getString();
			
			if(queryFetched.length()>1) {
				queries.add(queryFetched);					
			}
		}
	
		qexec.close();
	}
	
	/**
	 * This method returns the list of SPARQL queries that are used to generate the SHACL shapes
	 * @return the queries used to generate the SHACL shapes
	 */
	public List<String> getQueries() {
		return queries;
	}

	/**
	 * This method allows to set the list of SPARQL queries used to generate the SHACL shapes
	 * @param queries used to generate the SHACL shapes
	 */
	public void setQueries(List<String> queries) {
		this.queries = queries;
	}
	
	// -- Methods
	
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



	@Override
	public Model fromModel(Model ontology) {
		ExecutorService executorService = Executors.newFixedThreadPool(this.queries.size());
		List<Callable<Model>> taskList = new ArrayList<>();
		Model shapes = ModelFactory.createDefaultModel();
		
		for(String query:queries) {
			Callable<Model> task = () -> {
	            return retrievePartialShapes(query, ontology);
	        };
			taskList.add(task);
		}
		try {
			List<Future<Model>> futures = executorService.invokeAll(taskList);
	        for(int index=0; index < futures.size(); index++){
	        		Future<Model> future = futures.get(index);
	            try {
					shapes.add(future.get());
				} catch (InterruptedException | ExecutionException e) {
					log.severe(e.toString());
				}
	        }
		}catch(Exception e) {
			log.severe(e.toString());
	    }
	    executorService.shutdown();
		
		return shapes;
	}

	private Model retrievePartialShapes(String query, Model ontology) {
		Model partialShape = ModelFactory.createDefaultModel();
		QueryExecution qExec = null;
		try {
			Query queryNodeShapes = QueryFactory.create(query);
			qExec = QueryExecutionFactory.create(queryNodeShapes, ontology);
			partialShape.add(qExec.execConstruct());
			
		} catch(Exception e) {
			String errorMsg = (new StringBuilder()).append("The following query produced the error: ").append(e.toString()).append("\n").append(query).toString();
			log.severe(errorMsg);
			
		} finally {
			if(qExec!=null)
				qExec.close();
		}
		return partialShape;
	}
	
	/*
	 * Deprecated block of code 
	
	@Override
	public Model fromModel(Model ontology) {
		Model shapes = ModelFactory.createDefaultModel();
		queries.stream().forEach(query -> parallelPopulation(query, ontology, shapes));
		return shapes;
	}

	private void parallelPopulation(String query, Model ontology, Model shapes) {
		QueryExecution qExec = null;
		try {
			Query queryNodeShapes = QueryFactory.create(query);
			qExec = QueryExecutionFactory.create(queryNodeShapes, ontology);
			shapes.add(qExec.execConstruct());
			
		} catch(Exception e) {
			String errorMsg = (new StringBuilder()).append("The following query produced the error: ").append(e.toString()).append("\n").append(query).toString();
			log.severe(errorMsg);
			
		} finally {
			if(qExec!=null)
				qExec.close();
		}
	}*/
	
}
