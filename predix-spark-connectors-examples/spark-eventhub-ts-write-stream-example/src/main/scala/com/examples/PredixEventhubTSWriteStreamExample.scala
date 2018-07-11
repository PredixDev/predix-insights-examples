
package com.examples

import java.util.Properties
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.Row

import scala.collection.JavaConverters._

object PredixEventhubTSWriteStreamExample {
  
  private val defaultSchema = StructType(
        StructField("tag", StringType, true) ::
        StructField("timestamp", LongType, true) ::
        StructField("value", DoubleType, true) ::
        StructField("quality", IntegerType, true) :: Nil)
  
    def main(args: Array[String]) {
    
    //Create spark session to run spark commands in app Spark Predix Eventhub Write Streaming example
    val spark = SparkSession
      .builder()
      .appName("Spark Predix Eventhub Write Streaming example")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    //Load the properties file
    val props = new Properties();
    props.load(new java.io.FileInputStream("spark-eventhub-write-stream-example.properties"));

    //Create dataframe using predix.stream.ts schema structure
    val tsData = Seq(Row("tag1", 1518202903792L, 43.7, 1), Row("tag2", 1518202903792L, 43.7, 2), Row("tag3", 1518202903792L, 43.7, 2) )
    val df = spark.createDataFrame(spark.sparkContext.parallelize(tsData), StructType(defaultSchema))
    
    df.printSchema()
    df.show()
    
    /*
     * As an alternative to manually creating the dataframe, you can read it from timeseries.
     * Here is sample code of how to properly load and format data from timeseries:
     * Note: Reference the spark-predixts-example for more information
     * 
     * val props = new Properties();
     * props.load(new java.io.FileInputStream("eventhub.properties"));
     * df = spark.read.format("predix.ts").options(props.asScala.toMap).load()
     * df.show()
     * 
     */
    
    //Write dataframe to Eventhub Stream
    df.write.format("predix.stream.ts").options(props.asScala.toMap).mode(SaveMode.Overwrite).save()

  }
}
