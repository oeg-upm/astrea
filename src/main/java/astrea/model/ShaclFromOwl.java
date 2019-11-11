package astrea.model;

import java.util.List;
import org.apache.jena.rdf.model.Model;
/**
 * This interface represents an Astrea object that is able to produce SHACL shapes for a given ontology.<p>
 * An ontology is a formal description of knowledge as a set of concepts within a domain and the relationships that hold between them.<p>
 * This kind of objects exploits the restrictions explicit in such formal descriptions to produce a set of SHACL shapes.<p>
 */
public interface ShaclFromOwl {

	public Model fromURL(String owlUrl);
	public Model fromURLs(List<String> owlUrls);
	public Model fromOwl(String owlContent, String format);
	public Model fromModel(Model ontology);
}
