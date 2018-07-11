#!/bin/bash

if [ -z "$1" ]
then
    echo "WARN: Supply the folder under spark-python-examples that you want to build."
    exit
fi

pushd $1

echo "Building example..."
python setup.py bdist_egg

mkdir lib

echo "Removing existing lib/*.egg files..."
rm lib/*.egg || echo "No file to delete"

echo "Copying egg file to lib..."
cp dist/*.egg lib/

rm -rf build || echo "No build folder to delete"
rm -rf *.egg-info || echo "No egg-info folder to delete"

echo "Creating zip file..."
zip -r dist/$1.zip conf/
zip -r dist/$1.zip lib/

echo "Created zip file $1/dist/$1.zip"

popd
