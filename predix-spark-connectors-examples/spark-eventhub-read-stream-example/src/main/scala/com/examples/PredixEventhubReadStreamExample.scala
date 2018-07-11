package com.examples

import java.util.Properties
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Duration
import com.ge.predix.arf.connector.rtcommon.stream.JavaEventHubReceiver
import com.ge.predix.arf.connector.stream.StreamTSTransformer
import com.ge.predix.arf.connector.stream.StreamTransformer

object PredixEventhubReadStreamExample {
  def main(args: Array[String]) {
    val spark = SparkSession
      .builder()
      .appName("Spark Predix Eventhub Streaming example")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    // read event hub properties from configuration file
    val props = new Properties();
    props.load(new java.io.FileInputStream("eventhub.properties"));
    
    // configure custom eventhub reciever to spark's streaming context      
    val jb = new JavaEventHubReceiver(props)
    val ssc = new StreamingContext(spark.sparkContext, new Duration(1000))
    val receiver = ssc.receiverStream(jb)

    // create transformer object to parse through event hub messages
    val transformer = new StreamTransformer()
    // use StreamTSTransformer to transform messages into predix.timeseries format
    //val transformer = new StreamTSTransformer()
    receiver.foreachRDD(rdd => transformer.transform(rdd).show())
    ssc.start()
    println("eventhub connector example read section starts............")
   
    ssc.awaitTerminationOrTimeout(30000)

  }
}
