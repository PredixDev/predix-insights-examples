package com.examples

import java.io.FileInputStream
import java.util.Properties
import org.apache.spark.sql.SparkSession

object PredixPostGresExample {
  def main(args: Array[String]) {

    val spark = SparkSession
      .builder()
      .appName("Spark Predix Postgres basic example")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    val sc = spark.sparkContext
    val sqlContext= new org.apache.spark.sql.SQLContext(sc)
    val msgs =Seq("[{\"messageId\":\"09fe6922-a610-4347-b1be-a244b2b9835a\",\"body\":\"content of tenant\"}]")
    val jsonRdd = sc.parallelize(msgs)
    val recordsDF =sqlContext.read.json(jsonRdd)

    val props = new Properties
    props.load(new FileInputStream("postgres.properties"))

    //write data to your_table and your_table will be created if it does not exist
    recordsDF.write.jdbc(props.getProperty("url"), "your_table", props)

    //read data from your_table
    val readDF = spark.read.jdbc(props.getProperty("url"), "your_table", props)
    readDF.show()
  }
}