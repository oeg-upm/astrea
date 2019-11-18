package test.queries.catalogue;

import org.apache.jena.rdf.model.Model;
import org.junit.Assert;
import org.junit.Test;

import astrea.generators.OwlGenerator;
import astrea.model.ShaclFromOwl;

public class TestCatalogue {
	
	
	@Test
	public void testTime() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromURL("https://www.w3.org/2006/time");
		Assert.assertFalse(shapes.isEmpty());
	}
	
	
	@Test
	public void testSAREF() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromURL("https://w3id.org/saref");
		Assert.assertFalse(shapes.isEmpty());
	}
	
	@Test
	public void testSAREF4ENV() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromURL("https://mariapoveda.github.io/saref-ext/OnToology/SAREF4ENV/ontology/saref4envi.ttl/documentation/ontology.ttl");
		Assert.assertFalse(shapes.isEmpty());
	}
	
	@Test
	public void testVICINITYcore() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromURL("http://iot.linkeddata.es/def/core/ontology.ttl");
		Assert.assertFalse(shapes.isEmpty());
	}
	
	
	@Test
	public void testWoTMappings() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromURL("http://iot.linkeddata.es/def/wot-mappings/ontology.xml");
		Assert.assertFalse(shapes.isEmpty());
	}
	
	
	@Test
	public void testDELTAcore() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromURL("http://delta.linkeddata.es/def/core/ontology.json");
		Assert.assertFalse(shapes.isEmpty());
	}
	
	
	@Test
	public void testSAREF4BLDG() {
		ShaclFromOwl sharper = new OwlGenerator();
		Model shapes =  sharper.fromURL("https://mariapoveda.github.io/saref-ext/OnToology/SAREF4BLD/ontology/saref4bldg.ttl/documentation/ontology.ttl");
		Assert.assertFalse(shapes.isEmpty());
	}

}
