# ASTREA

## 1. Quick start
Astrea can be used as a java library for third-party java projects. First install Astrea as a dependency following the steps detailed in the section **1.1.1 Install Astrea**. Then, in you code create an instance of our Astrea object as follows:
````
ShaclFromOwl sharper = new OptimisedOwlGenerator();
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

### 1.1 Install Astrea
In order to use the Astrea as java library for third-party components there are two approaches: import the library as a *jar* or install Astrea as a local maven dependency and then use your *pom.xml* to import it. Following we provide a guide for both options.

#### 1.1.1 Import Astrea as a jar
Download the last release from our GitHub or our [web page](https://astrea.linkeddata.es). Then, import the *jar* file in a project .
#### 1.1.2 Instaling as local maven dependency
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
	<version>1.0.0</version>
</dependency>
````
Alternatively, Astrea can be installed as a local dependency following these steps:
 1.  Compile the project:
````
mvn clean package -Dskiptests
````
 2.  Compile the project:
````
mvn install:install-file -Dfile=./target/astrea-1.0.0.jar -DgroupId=oeg.validation -DartifactId=astrea -Dversion=1.0.0 -Dpackaging=jar
````
 2.  Import the dependency in your project using the pom file relying on the previous snipped