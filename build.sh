#!/bin/bash
mvn clean install && cp -rp spiderapplet/target/jnlp/* . && cp -rp spiderapplet/src/main/resources/spider.html lib/

# run app via webstart:
# javaws spider.jnlp

# run app via java:
# java -jar lib/spiderapplet-1.0-SNAPSHOT.jar

# run app via appletviewer:
# appletviewer lib/spider.html

