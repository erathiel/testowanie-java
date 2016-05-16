#!/bin/sh

java -cp ./hsqldb-2.3.3/lib/hsqldb.jar org.hsqldb.util.DatabaseManagerSwing --url jdbc:hsqldb:hsql://localhost/workdb
