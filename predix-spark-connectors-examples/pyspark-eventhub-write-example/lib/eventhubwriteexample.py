from pyspark import SparkContext, SparkConf
from pyspark.sql import SparkSession, SQLContext
import json
import sys

if __name__ == "__main__":
    print("*"*80)
    spark = SparkSession.builder.appName("PySpark Predix Eventhub basic write example").config("spark.some.config.option", "some-value").getOrCreate()
    sqlContext = SQLContext(spark)
    json_str = [("{\"body\":\"null\",\"tags\":{\"k1\":\"v1\"}}"), ("{\"body\":\"@345$%^\",\"tags\":{\"k2\":\"v2\"}}")]
    json_rdd = spark.sparkContext.parallelize(json_str)
    df = sqlContext.read.json(json_rdd)
    properties = json.load(open('predix-eventhub-write-example.properties.template'))
    df.write.format("predix.stream").options(**properties).save().show()

    print("*"*80)
    spark.stop()
