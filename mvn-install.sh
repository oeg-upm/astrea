# 1. Install the helio-core dependency in your local repository using this script
mvn clean install -DskipTests
mvn install:install-file -Dfile=./target/astrea-1.2.1.jar -DgroupId=oeg.validation -DartifactId=astrea -Dversion=1.2.1 -Dpackaging=jar

# 2. You are ready to go and use the framework 