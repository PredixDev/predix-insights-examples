## Predix Spark Framework

Apache Spark based framework for reading/writing data from/to Predix data services like Event Hub, Time Series & APM services like Asset, Time Series.

Main advantages of using this over vanilla spark is that this framework takes care of both deploy and runtime property loading. It provides a config based approach for inputs and outputs, easier way to retrieve data from multiple sources. This framework leverages Predix Conenctors for retrieving data from all the Predix datasources.
Also this framework works with any open source connectors like blobstore(s3), postgres(jdbc), etc.

## How to build spark-java-examples and spark-python-examples

To build any example from spark-java-examples or spark-python-examples, use the following command to generate a .zip file in each examples' target folder.

`./build.sh <example-folderName>`


## Want to develop a Spark app using this framework

Refer to the any examples src section, it has several sample Spark apps that uses a structure which can facilitate data retrieval.

## What is the usage of job.json

Job json is an easy representation of inputs & outputs for any given analytic. It defines what type of datasource to connect to and put placeholder connection details to be substituted during runtime.
Also it specifies what language of Spark Job and what would be className or egg file name that has the logic.

```
{
  "job_definition": {
    "inputs": [
      {
        "name": "readFromDatasource1",
        "provider": "predix.ts",
        ...
      }
    ],
    "language": "JAVA",
    "outputs": [
      {
         "name": "writeToDatasource2",
         "provider": "jdbc",
        ...
      }
    ]
  }
}
```

## What is usage of runtime.properties

It's a property file that has connection details and also framework configuration parameters that are to be substituted during runtime.
For example, it can have details about a jdbc datasource that the analytic needs to retrieve data from.

```
...

predix.postgres.url=jdbc:postgresql://<postgres-instanceName>:5432/<postgres-schemaName>
predix.postgres.user=<postgres-userName>

...

```

## How to use these examples

### In Predix Insights

These examples can be used

[See documentation](https://docsstaging.predix.io/en-US/content/service/data_management/Andromeda/overview#concept_af7c2dd8-440c-4390-a708-7ea87e65e1ed) to learn more about Predix Insights and what it offers.

[See documentation](https://docsstaging.predix.io/en-US/content/service/data_management/Andromeda/overview#concept_62d19937-12f5-4e70-8173-357b844504e2) to learn more about Predix Spark Framework.


#### Setup Predix Spark Framework

1. Stable version of PredixSparkFramework libraries are preinstalled in any Insights instance.

2. When running any spark job, just specify the frameworkName option and that will include all the framework libraries in classpath.

```
{
    ...
    "frameworkName": "predix-spark-framework"
}
```

#### Run an example with Predix Insights Flows

1. Pick an example you want to test and modify the `<example-folderName>/conf/runtime.properties` to fill in the placeholder values required.

2. Build that example using `cd spark-<language>-examples; ./build.sh <example-folderName>` script to generate `<example-folderName>/target/<example-folderName>.zip` for Java or `<example-folderName>/dist/<example-folderName>.zip` for Python

```
        <example-folderName>.zip/
        ├── lib
        │   └── <example-folderName>-<version>.(jar/egg)
        ├── conf
        │   └── job.json
        │   └── runtime.properties

```

3. Create a Flow in Predix Insights using the `<example-folderName>.zip` file.

[See documentation](https://docsstaging.predix.io/en-US/content/service/data_management/Andromeda/configure-flows) to learn how to create and manage flows in Predix Insights.

4. Copy the sparkargs from `<example-folderName>/conf/sparkargs` and update the newly created flow with it.

5. Launch the flow and wait for to see the logs.

6. In logs you will see the dataframes being printed that are read from the datasources.

#### Things to note
1. If you ran into any connectivity issues, make sure to check whether the datasource instances are available to be connected from the Predix Insights instance.
2. If its an Eventhub example, make sure to Stop Instance after testing as it will continue running as its a streaming job.

## Advice on creating analytics
It is possbile that the data providers may provide corrupted data records in incorrect format. In this case, the predix connectors would feed empty dataframes into the analytics. Accordingly, the analytic writers should check if the dataframe from specific data provider is empty or not. If it is empty, then please return an empty map as follows:

 ** Java Analytic **
 ````
     public Map<String, Dataset<Row>> runAnalysis(Map<String, IRuntimeDataset<SQLContext>> inputDatasets) {
        logger.info("################# Run Java Analytic ######################");
        Map outputs = new HashMap();
        IRuntimeDataset inputDS = (IRuntimeDataset)inputDatasets.get("inputDS");
        if (inputDS == null) {
          logger.warn("Input Datasource is null, won't be able to run the analytic");
          return Collections.emptyMap();
        }

        Dataset<Row> outputDf = ((SQLContext)inputDS.getContext()).sql("select * from `" + inputDS.getName() + "`");
        if (outputDf.head(1).isEmpty()) {
          logger.warn("Corrupted streaming message...... pls check......");
          return Collections.emptyMap();
        }
        outputDf.show();
        //put your analytic logic here //

        outputs.put("outputDS", outputDf);
        logger.info("################# End of Java Analytic  ######################");
        return outputs;
    }
 ````
 
 ** Python Analytic **
 ````
     def run_job(self, spark_session, runtime_config, job_json, context_dict, logger):
        logger.info("################# run Python Analytic ######################")

        timeseriesReadDS = "timeseriesReadDS"
        tsDF = context_dict[timeseriesReadDS].sql("select * from " + context_dict[timeseriesReadDS].table_name)
        if(len(tsDF.head(1)) == 0)
                logger.warn("Corrupted streaming message...... pls check......")
                return {"tsWriteDs":None}

        ## put your analytic logic here ##

        result_dict = {"tsWriteDs":tsDF}
        logger.info("################# End of Python Analytic ######################")
        return result_dict
 ````
 
 [![Analytics](https://ga-beacon.appspot.com/UA-82773213-1/predix-insights-examples/readme?pixel)](https://github.com/PredixDev)
