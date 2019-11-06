package astrea.model;

import java.util.Map;

import org.apache.jena.rdf.model.Model;

public interface ShaclFromOwl {

	public Model fromURL(String owlUrl);
	public Model fromURL(Map<String,String> owlUrls);
	public Model fromOwl(String owlContent, String format);
	public Model fromModel(Model ontology);
}
