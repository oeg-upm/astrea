package sharper.generators;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import astrea.model.ShaclFromOwl;

public class OwlGenerator implements ShaclFromOwl{

	private List<String> queries;
	
	public OwlGenerator() {
		queries = new ArrayList<>();
		fetchQueries();
	}
	
	
	private void fetchQueries() {
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("/Users/cimmino/Desktop/queries.csv"));
			String line = reader.readLine();
			Boolean open = false;
			StringBuilder query = new StringBuilder();
			while (line != null) {
				if(line!="-" && line!= "QUERY") {
					
					if(!line.endsWith("\"")) {
						query.append(line).append("\n");
					}else {
						queries.add(query.toString().replaceAll("^\\s*\"", "").replaceAll("\"\\s*$", ""));
						System.out.println(query);
						query = new StringBuilder();
						break;
					}					
				}
				// read next line
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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
		Model shapes = ModelFactory.createDefaultModel();
		for(String query:queries) {
			Query queryNodeShapes = QueryFactory.create(query);
			Model nodeShapes = QueryExecutionFactory.create(queryNodeShapes, ontology).execConstruct();
			shapes.add(nodeShapes);
		}
		
		return shapes;
	}

}
