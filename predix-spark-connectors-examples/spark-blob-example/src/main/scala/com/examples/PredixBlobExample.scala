package com.examples

import java.io.FileInputStream
import java.util.Properties
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.SaveMode
import org.apache.spark.SparkConf

import scala.collection.JavaConverters._

object PredixBlobExample {

 def main(args: Array[String]) {


    val prop = new Properties
    prop.load(new FileInputStream("blob-example.properties"))
    val props = prop.asScala.toMap

    val conf = new SparkConf
    conf.setAll(props)

    val spark = SparkSession
      .builder()
      .appName("Spark Predix S3 basic example")
      .config(conf)
      .getOrCreate()

// Read data from S3 file
    val s3Data = spark.read.json(prop.getProperty("url"))

    s3Data.show()

  }
}
