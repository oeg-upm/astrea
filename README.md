# Astrea

Astrea is a software capable of generate SHACL shapes from one or more given ontologies. It relies on a set of equivalences between the [OWL2](https://www.w3.org/TR/owl2-overview/) constructs and the [SHACL](https://www.w3.org/TR/shacl/) constructs, which are exploit by means of a set of [SPARQL queries](https://www.w3.org/TR/sparql11-query/). The idea behind Astrea is to rely on a set of mappings between such specifications, and a list of queries so by just applying the queries over one or more owl files the associated shapes can be generated.

In this repository the software provided imports all the ontologies associated to the construct owl:imports for an input ontology, in addition in its methods counts with the option of importing more than one ontology URL to generate their shapes. Consider that the shapes generated are associated only to the types and properties specified in the ontology, therefore providing more than one URL can be useful if one ontology references elements from another but it does not import it.

The [Astrea resources](https://github.com/oeg-upm/Astrea/tree/master/material), besides the java library which latest version [can be downloaded from the releases tab](https://github.com/oeg-upm/Astrea/releases), include the following elements:
* Mappings.xlsx: a set of mappings that hold the equivalences between OWL and SHACL constructs.
* OWL.csv, RDFS.csv, SHACL.csv, XSD.csv: the constructs from the OWL, RDFS, XSD, and SHACL that exists in these specifications
* Queries.csv: the queries to generate the SHACL shapes, this file contains also all the statements that are required from the OWL, RDFS, and XSD to generate a shapes, as well as, all the constructs belonging to SHACL that the output shape contains.
* astrea-dataset.zip: is a RDF dataset that contains all the Astrea resources modelled according to the [Astrea ontology](https://w3id.org/def/astrea#). This dataset is also available at [https://astrea.helio.linkeddata.es/](https://astrea.helio.linkeddata.es/) for live queries or to download.
* Java Documentation: is available at https://oeg-upm.github.io/Astrea/


**Astrea Demo:** Astrea has been integrated as a [web service and published online](https://astrea.linkeddata.es/). In addition, the source code of this demo can be found at [its GitHub repository](https://github.com/oeg-upm/astrea-web)

## 1 - Install Astrea
In order to use the Astrea as java library for third-party components there are two approaches: import the library as a *jar* or install Astrea as a local maven dependency and then use your *pom.xml* to import it. Following we provide a guide for both options.

#### 1.1 Import Astrea as a jar
Download the last release from our GitHub. Then, import the *jar* file in a project .
#### 1.2 Instaling as local maven dependency
Astrea can be installed as a local dependency. For this purpose download the code from this repository:
`````
git clone https://github.com/oeg-upm/Astrea.git
`````
Then, install the project as a local maven dependency, for which you can run the script that we provide
`````
bash mvn-install.sh
`````
Finally, import in a project the Astrea maven dependency using the following code in your *pom.xml*:
````
<dependency>
	<groupId>oeg.validation</groupId>
	<artifactId>astrea</artifactId>
	<version>1.1.0</version>
</dependency>
````
Alternatively, Astrea can be installed as a local dependency following these steps:
 1.  Compile the project:
````
mvn clean package -Dskiptests
````
 2.  Compile the project:
````
mvn install:install-file -Dfile=./target/astrea-1.1.0.jar -DgroupId=oeg.validation -DartifactId=astrea -Dversion=1.1.0 -Dpackaging=jar
````
 3.  Import the dependency in your project using the pom file relying on the previous snipped

## 2 - Quick start
Astrea can be used as a java library for third-party java projects, in you code create an instance of our Astrea object as follows:
````
ShaclFromOwl sharper = new OwlGenerator();
````
Having this object the shapes, which will be provided as [jena]([https://jena.apache.org/documentation/javadoc/jena/org/apache/jena/rdf/model/Model.html](https://jena.apache.org/documentation/javadoc/jena/org/apache/jena/rdf/model/Model.html)) Models, can be generated using different methods:

 - Using the URL of an ontology
 ````
 Model shapes = sharper.fromURL("http://iot.linkeddata.es/def/core/ontology.ttl");
````
 - Using a list containing the URLs of several ontologies
 ````
 List<String> ontologies = new ArrayList<>();
 ontologies.add("http://iot.linkeddata.es/def/core/ontology.ttl");
 ...
 Model shapes = sharper.fromURLs(ontologies);
````
- Using an ontology in memory, consider that the [formats supported are the ones specified in jena]([https://jena.apache.org/documentation/io/](https://jena.apache.org/documentation/io/))
`````
 Model shapes = sharper.fromOwl(String owlContent, String format);
`````
- Using an jena model
`````
Model ontologyModel = ModelFactory.createDefaultModel();
// insert content in the variable ontologyModel
Model shapes = sharper.fromModel(ontologyModel);
`````

**Keep in mind that Astrea will automatically include all the ontologies that are specified under the owl:imports statement, and therefore, it will generate their shapes as well.**

**To check other constructors of the OwlGenerator class read our [java doc](https://oeg-upm.github.io/Astrea/)**