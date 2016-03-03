#!/bin/bash
mvn clean install $* && cp -rp spiderapplet/target/jnlp/* . && cp -rp spiderapplet/src/main/resources/spider.html lib/
