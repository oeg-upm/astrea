package shaper.model;

import org.apache.jena.rdf.model.Model;

public interface ShaperModel {

	public Model fromModelToShapes(String owlUrl);
	
}
