
package com.examples

import java.util.Properties
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.SaveMode

import scala.collection.JavaConverters._
//below imports required for temporary data
import java.util.ArrayList
import org.apache.spark.api.java.JavaSparkContext

object PredixEventhubAlarmWriteStreamExample {
  def main(args: Array[String]) {

    //Create spark session to run spark commands in app Spark Predix Eventhub Alarm Write Streaming example
    val spark = SparkSession.builder()
      .appName("Spark Predix Eventhub Alarm Write Streaming example")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    //Load the properties file
    val props = new Properties();
    props.load(new java.io.FileInputStream("spark-eventhub-alarm-write-stream-example.properties"));

    //temporarily hard-coding data to write to alarm
    val msgs = "[{\"taskId\":\"smartSignalTask_13\",\"type\":\"updateAlarm\",\"name\":\"gateway_alert\",\"severity\":3,\"eventStart\":1490304685000,\"storageReceiveTime\":1490304785000,\"associatedMonitoredEntitySourceKey\":\"AVD-AUTOMATION-SEG3-ASSET-ID1\"},{\"taskId\":\"smartSignalTask_14\",\"type\":\"createAlarm\",\"name\":\"gateway_alert\",\"severity\":2,\"eventStart\":1490304885000,\"storageReceiveTime\":1490304985000,\"associatedMonitoredEntitySourceKey\":\"AVD-AUTOMATION-SEG3-ASSET-ID2\"}]"

    //Build schema to save the eventhub stream data with
    val defaultSchema = StructType(
        StructField("associatedMonitoredEntitySourceKey", StringType, true) ::
        StructField("eventStart", LongType, true) ::
        StructField("name", StringType, true) ::
        StructField("severity", IntegerType, true) ::
        StructField("storageReceiveTime", LongType, true) ::
        StructField("taskId", StringType, true) ::
        StructField("type", StringType, true) :: Nil)

    //Read data from an HDFS file and save in the spark dataframe df	            
    //val df = spark.read.schema(defaultSchema).json("hdfs:///arf-data/input/alarm-output.json")
    //Load the hard-coded data    
    var jsonStrList = new ArrayList[String]()
    jsonStrList.add(msgs)
    val df = spark.read.schema(defaultSchema).json(new JavaSparkContext(spark.sparkContext).parallelize(jsonStrList))

    df.printSchema()
    df.show()

    //Write the dataframe to Eventhub alarm stream
    df.write.format("predix.stream.alarm").options(props.asScala.toMap).mode(SaveMode.Overwrite).save()

  }
}
