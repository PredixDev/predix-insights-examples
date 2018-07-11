from py4j.java_gateway import JavaClass
from pyspark import SparkContext, SparkConf
from pyspark.sql import SparkSession, SQLContext
import json
import sys
import os
import uuid

if __name__ == "__main__":
    print("*"*80)
    spark = SparkSession.builder.appName("PySpark Predix Eventhub basic read example").config("spark.some.config.option", "some-value").getOrCreate()
    sqlContext = SQLContext(spark)
    sparkContext = spark.sparkContext
    properties = json.load(open('predix-eventhub-read-example.properties'))
    jprop = JavaClass("java.util.Properties", sparkContext._gateway._gateway_client)()
    for k in properties:
        jprop.setProperty(k, properties[k])

    javaEventHubReceiver = JavaClass("com.ge.predix.arf.connector.rtcommon.stream.JavaEventHubReceiver", sparkContext._gateway._gateway_client)(jprop)
    durationObject = JavaClass("org.apache.spark.streaming.Duration", sparkContext._gateway._gateway_client)(1000)
    jssc = JavaClass("org.apache.spark.streaming.api.java.JavaStreamingContext", sparkContext._gateway._gateway_client)(sparkContext._jsc, durationObject)
    jreceiver = jssc.receiverStream(javaEventHubReceiver)
    transformerObject = JavaClass("com.ge.predix.arf.connector.stream.StreamTSTransformerObject", sparkContext._gateway._gateway_client)
    transformFunc = transformerObject.transformShowCallBackFunction()
    jreceiver.foreachRDD(transformFunc)
    jssc.start()
    jssc.awaitTerminationOrTimeout(30000)

    print("*"*80)
