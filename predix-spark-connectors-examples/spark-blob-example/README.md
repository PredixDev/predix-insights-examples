## Blobstore Spark Connector Example
This example reads data from the S3 file where the data is in Predix Time Series format.

## Build

`mvn clean install -s ../../mvnsettings.xml`

## Run example in Predix Insights

In Predix Insights, we can execute predix spark connectors in 2 different ways.

One way is to upload connectors as a library dependencies in Insights.
Another way is to bundle the required connectors to flow template zip file.

Once Connectors are built and required example is built,

1. To upload connectors as library dependency, upload built .jar files to [lib dependencies](https://docsstaging.predix.io/en-US/content/service/data_management/Andromeda/manage-dependencies#task_d912544c-d26e-471a-a0dc-78309c42b1fe) section of your Predix Insights instance

(Or)

1. To upload connectors bundled in flow template zip file, bundle built .jar files as a .zip file using the following structure, along with the example .jar file

```
        <analytic-example>.zip/
        ├── lib
        │   └── connectors-common-<version>.jar
        ├── conf
        ├──target/spark-blob-example-0.0.1.jar
```

Refer [Predix Insights - Manage Dependencies](https://docsstaging.predix.io/en-US/content/service/data_management/Andromeda/manage-dependencies#task_d912544c-d26e-471a-a0dc-78309c42b1fe)

2. In your local directory, create a new .zip file with an empty lib and conf folder.

3. Edit the `PredixBlobExample.scala` file to include the S3 file path you want to read from. Build this file again using the build command above. 

4. In the lib folder, add the `target/spark-blob-example-0.0.1.jar`.

5. In the conf folder, create a new `blob-example.properties` file using [template file](./conf/blob-example.properties.template). Add the AWS key, secret key, etc.

Refer [Predix Insights - Create FlowTemplate](https://docsstaging.predix.io/en-US/content/service/data_management/Andromeda/configure-flow-template#concept_03b3089b-2c0a-41f2-856d-4cd457a95896)

6. In the Insights UI, create a Flow Template using the .zip file created in step 2 - 5.

7. In the Insights UI, create a Flow for the Flow Template created in step 6. Add the following Spark Arguments.

```
{
    "className": "com.examples.PredixBlobExample"
}
```

8. In the Insights UI, launch the Flow.

[![Analytics](https://ga-beacon.appspot.com/UA-82773213-1/predix-insights-examples/readme?pixel)](https://github.com/PredixDev)
