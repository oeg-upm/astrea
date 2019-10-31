package astrea.model;

import java.util.List;

import org.apache.jena.rdf.model.Model;

public interface ShaclFromOwl {

	public Model fromURL(String owlUrl);
	public Model fromURL(List<String> owlUrls);
	public Model fromOwl(String owlContent, String format);
}
