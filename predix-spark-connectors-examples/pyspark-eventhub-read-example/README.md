## Predix Eventhub (predix.stream) PySpark Connector Example


## Execute example in Predix Insights

In Predix Insights, we can execute predix PySpark connectors in 2 different ways.

One way is to upload connectors as a library dependencies in Insights.
Another way is to bundle the required connectors to flow template zip file.

Once Spark connectors are built (Refer [Predix Spark Connectors Build](https://github.com/PredixDev/predix-spark-connectors#how-to-build-all-the-connectors) documentation),

1. To upload connectors as library dependency, upload built .jar files to [lib dependencies](https://docsstaging.predix.io/en-US/content/service/data_management/Andromeda/manage-dependencies#task_d912544c-d26e-471a-a0dc-78309c42b1fe) section of your Predix Insights instance

(Or)

1. To upload connectors bundled in flow template zip file, bundle built .jar files as a .zip file using the following structure, along with the example .py file

```
        <analytic-example>.zip/
        ├── lib
        │   └── connectors-common-<version>.jar
        │   └── spark-eventhub-connector-<version>.jar
        │   └── eventhubreadexample.py
        ├── conf
        │   └── predix-eventhub-read-example.properties
```

Refer [Predix Insights - Manage Dependencies](https://docsstaging.predix.io/en-US/content/service/data_management/Andromeda/manage-dependencies#task_d912544c-d26e-471a-a0dc-78309c42b1fe)

2. In your local directory, create a new zip file with an empty lib and conf folder.

3. In lib folder, add the `lib/eventhubreadexample.py`.

4. In conf folder, create a new `predix-eventhub-read-example.properties` file using [template file](./conf/predix-eventhub-write-example.properties.template) with predix timeseries url, uaa details, query string, etc. and add it

Refer [Predix Insights - Create FlowTemplate](https://docsstaging.predix.io/en-US/content/service/data_management/Andromeda/configure-flow-template#concept_03b3089b-2c0a-41f2-856d-4cd457a95896)

5. In the Insights UI, create a Flow Template using the .zip file created in step 2 - 4.

6. In the Insights UI, create a Flow for the Flow Template created in step 5. Add the following Spark Arguments.

```
{
    "fileName": "eventhubreadexample.py"
}
```

8. In the Insights UI, launch the Flow.

### Schema of Dataframe

```
 |-- tag: string (nullable = true)
 |-- timestamp: long (nullable = true)
 |-- value: double (nullable = true)
 |-- quality: integer (nullable = true)
 |-- attributes: struct (nullable = true)
 |    |-- AttributeKey: array (nullable = true)
 |    |    |-- element: string (containsNull = true)
 |    |-- AttributeKey2: array (nullable = true)
 |    |    |-- element: string (containsNull = true)
```

### Sample Dataframe

```
 +---------------+-------------+-----+-------+--------------------+
 |            tag|    timestamp|value|quality|          attributes|
 +---------------+-------------+-----+-------+--------------------+
 |X298312.DWATT11|1517442373576|  1.5|      3|[WrappedArray(Att...|
 |X298312.DWATT11|1517604424068|  1.5|      3|[WrappedArray(Att...|
 |X298312.DWATT12|1517442290123| 11.5|      3|[WrappedArray(Att...|
 |X298312.DWATT12|1517604525340|222.5|      3|[WrappedArray(Att...|
 +---------------+-------------+-----+-------+--------------------+
```

[![Analytics](https://ga-beacon.appspot.com/UA-82773213-1/predix-insights-examples/readme?pixel)](https://github.com/PredixDev)