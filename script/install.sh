#!/bin/bash
. ~/.bash_profile
mvn clean install -Dmaven.test.skip=true
