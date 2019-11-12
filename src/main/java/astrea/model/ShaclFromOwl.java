package astrea.model;

import java.util.List;
import org.apache.jena.rdf.model.Model;
/**
 * <p>This interface represents an Astrea object that is able to produce SHACL shapes for a given ontology.</p>
 * <p>An ontology is a formal description of knowledge as a set of concepts within a domain and the relationships that hold between them.</p>
 * <p>This kind of objects exploits the restrictions explicit in such formal descriptions to produce a set of SHACL shapes.</p>
 */
public interface ShaclFromOwl {
	/**
	 * This method aims at generating the SHACL shapes from the url of an ontology
	 * @param owlUrl an ontology url
	 * @return a jena {@link Model} containing the SHACL shapes generated
	 */
	public Model fromURL(String owlUrl);
	/**
	 * This method aims at generating the SHACL shapes from a set of ontology urls
	 * @param owlUrls a list of onotlogies urls
	 * @return a jena {@link Model} containing the SHACL shapes generated
	 */
	public Model fromURLs(List<String> owlUrls);
	/**
	 * This method aims at generating the SHACL shapes from the content in memory of one or more ontologies
	 * @param owlContent the content of one or more ontologies
	 * @param format a valid jena format
	 * @return a jena {@link Model} containing the SHACL shapes generated
	 */
	public Model fromOwl(String owlContent, String format);
	/**
	 * This method aims at generating the SHACL shapes from a jena {@link Model} containing the content of one or more ontologies
	 * @param ontology a {@link Model} containing the content of one or more ontologies
	 * @return a jena {@link Model} containing the SHACL shapes generated
	 */
	public Model fromModel(Model ontology);
}
