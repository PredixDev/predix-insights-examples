
package com.examples


import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.SaveMode

import java.util.Properties



object PredixEventhubGenericWriteStreamExample {
  def main(args: Array[String]) {
    
    val spark = SparkSession
      .builder()
      .appName("Spark Predix Eventhub Write Streaming example")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()


      // read event hub properties from configuration file
      val props = new Properties();
      props.load(new java.io.FileInputStream("spark-eventhub-write-stream-example.properties"));
      
      // create dataframe which contains data to be published to eventhub
      
      // create schema used for the predix.stream data provider
      val defaultSchema = StructType(
        StructField("body", StringType, true) ::
        StructField("tags", MapType(StringType, StringType, true), true):: Nil)

      // create a sequence of json strings that have body and tags
      val jsonSeq = Seq( ("{\"body\":\"null\",\"tags\":{\"k1\":\"v1\"}}"), ("{\"body\":\"@345$%^\",\"tags\":{\"k2\":\"v2\"}}"))
      var outputDf = spark.sqlContext.read
        .schema(defaultSchema)
        .json(spark.sparkContext.parallelize(jsonSeq))
      
      outputDf.show      
      import scala.collection.JavaConverters._

      println("eventhub connector example write section starts............")
      outputDf.write
        .format("predix.stream")
        .options(props.asScala.toMap)
        .mode(SaveMode.Overwrite)
        .save()
    
  }
  
} 