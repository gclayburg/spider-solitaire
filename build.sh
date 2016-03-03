#!/bin/bash
mvn clean install && cp -rp spiderapplet/target/jnlp/* .
