package astrea.model;

import org.apache.jena.rdf.model.Model;

public interface ShaclFromOwl {

	public Model fromURL(String owlUrl);
	public Model fromOwl(String owlContent, String format);
}
