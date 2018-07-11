#!/bin/bash

if [ -z "$1" ]
then
    echo "WARN: Supply the folder under spark-java-examples that you want to build."
    exit
fi

pushd $1

echo "Building example..."
mvn clean install -s ../mvnsettings.xml

echo "Removing existing lib/*.jar files..."
rm lib/*.jar || echo "No file to delete"

echo "Copying jar file to lib..."
cp target/*.jar lib/

echo "Creating zip file..."
zip -r target/$1.zip conf/
zip -r target/$1.zip lib/
#zip -rj target/$1.zip target/*.jar

popd
