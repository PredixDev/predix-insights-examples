package com.examples

import java.io.FileInputStream
import java.util.Properties
import org.apache.spark.sql.SparkSession

object PredixTsExample {

  def main(args: Array[String]) {

    val spark = SparkSession
      .builder()
      .appName("Spark Predix TS basic example")
      .getOrCreate()

    import scala.collection.JavaConverters._
    val parameters = new Properties
    parameters.load(new FileInputStream("predixts-example.properties"))
    spark.read.format("predix.ts").options(parameters.asScala.toMap).load().show()

  }
}
