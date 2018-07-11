package com.examples

import java.io.FileInputStream
import java.util.Properties

import org.apache.spark.sql.SparkSession

object PredixTsIngestExample {

  def main(args: Array[String]) {

    val spark = SparkSession
      .builder()
      .appName("Spark Predix TS basic ingestion example")
      .getOrCreate()

    import scala.collection.JavaConverters._
    val parameters = new Properties
    parameters.load(new FileInputStream("predixts-ingest-example.properties"))

    val tsdf = spark.read.option("header", "true").csv("sample-timeseries-data.csv")
    tsdf.show()
    tsdf.write.format("predix.ts").options(parameters.asScala.toMap).save()

  }
}
