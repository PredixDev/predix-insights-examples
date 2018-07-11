#!/bin/bash
BUILD_FOLDER=build
DIST_FOLDER=dist

for d in *-example ; do

    echo "INFO: Building $d..."

    if [[ ! -d "$d" ]]; then
        echo "WARN: Build example $d is not a directory, skipping..."
        continue
    fi

    rm -r $BUILD_FOLDER/*
    rm $DIST_FOLDER/$d.zip

    mkdir -p $BUILD_FOLDER/conf
    mkdir -p $BUILD_FOLDER/lib
    mkdir -p $DIST_FOLDER/

    pushd $d

    echo "INFO: Building example..."
    if [ -f ./pom.xml ]; then
        echo "INFO: Pom file exists so running mvn clean install..."
        mvn clean install || error_exit "ERROR: Mvn Build Failed for $d."
        cp target/*.jar ../$BUILD_FOLDER/lib/ || error_exit "ERROR: Copy target jar failed for $d."
    else
        cp *.py ../$BUILD_FOLDER/lib/ || error_exit "ERROR: Copy py file failed for $d."
    fi

    popd

    echo "INFO: Creating zip file under $DIST_FOLDER..."
    pushd $BUILD_FOLDER
    zip -r ../$DIST_FOLDER/$d.zip ./conf || error_exit "ERROR: Creating zip/conf failed for $d."
    zip -r ../$DIST_FOLDER/$d.zip ./lib || error_exit "ERROR: Creating zip/lib failed for $d."
    popd
    echo "INFO: Finished Building $d..."
done

echo "INFO: Examples bundled..."
ls -l $DIST_FOLDER
function error_exit {
    echo "$1" >&2   ## Send message to stderr. Exclude >&2 if you don't want it that way.
    exit "${2:-1}"  ## Return a code specified by $2 or 1 by default.
}
