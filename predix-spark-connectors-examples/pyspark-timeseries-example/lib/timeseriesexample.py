from pyspark import SparkContext, SparkConf
from pyspark.sql import SparkSession
import json
import sys

if __name__ == "__main__":
    print("*"*80)
    spark = SparkSession.builder.appName("PySpark Predix TS basic example").config("spark.some.config.option", "some-value").getOrCreate()

    properties = json.load(open('predixts-example.properties'))
    spark.read.format("predix.ts").options(**properties).load().show()

    print("*"*80)
    spark.stop()

