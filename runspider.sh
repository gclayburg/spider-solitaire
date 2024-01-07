#!/bin/bash

#The version of Java is important here.  Java 8 works.  Newer versions probably will not work with the event system.  
#It seems Java 9 finally dropped support of the ancient Applet 1.0 event model that this game uses.  So, make sure you use Java 8.
/home/gclaybur/.sdkman/candidates/java/current/bin/java -jar /home/gclaybur/dev/spider-solitaire/lib/spiderapplet-1.0-SNAPSHOT.jar > /dev/null

