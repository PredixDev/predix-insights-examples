## APM Timeseries (predix.apm.ts)

This data connector supports read/write functionality to APM Timeseries. User authentication will be automatically handled.

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
        │   └── spark-apm-ts-connector-<version>.jar
        ├── conf
        ├──target/spark-apmts-example-0.0.1.jar
```

Refer [Predix Insights - Manage Dependencies](https://docsstaging.predix.io/en-US/content/service/data_management/Andromeda/manage-dependencies#task_d912544c-d26e-471a-a0dc-78309c42b1fe)

2. In your local directory, create a new zip file with an empty lib and conf folder.

3. In lib folder, add the `target/spark-apmts-example-0.0.1.jar`.

4. In conf folder, create a new `apmts-example.properties` file using [template file](./conf/apmts-example.properties.template) with predix timeseries url, uaa details, query string, etc. and add it

Refer [Predix Insights - Create FlowTemplate](https://docsstaging.predix.io/en-US/content/service/data_management/Andromeda/configure-flow-template#concept_03b3089b-2c0a-41f2-856d-4cd457a95896)

5. In the Insights UI, create a Flow Template using the .zip file created in step 2 - 4.

6. In the Insights UI, create a Flow for the Flow Template created in step 5. Add the following Spark Arguments.

```
{
    "className": "com.examples.ApmTsExample"
}
```

8. In the Insights UI, launch the Flow.

### Schema of Dataframe

```
 |-- tag: string (nullable = true)
 |-- timestamp: long (nullable = true)
 |-- value: double (nullable = true)
 |-- quality: integer (nullable = true)

```

### Sample Dataframe

```
+-----------------------+-------------+-----+-------+
|tag                    |timestamp    |value|quality|
+-----------------------+-------------+-----+-------+
|OO_Tag_Temperature_ID21|1495063800000|0.9  |3      |
|OO_Tag_Temperature_ID21|1495064400000|0.76 |3      |
+-----------------------+-------------+-----+-------+
```
[![Analytics](https://ga-beacon.appspot.com/UA-82773213-1/predix-insights-examples/readme?pixel)](https://github.com/PredixDev)