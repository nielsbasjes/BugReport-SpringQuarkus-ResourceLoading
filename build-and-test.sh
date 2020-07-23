#!/usr/bin/env bash

( cd resourcelib && mvn clean install ) && ( cd java-test && mvn clean package ) && ( cd quarkus-test && mvn clean package )
