package com.examples

import java.io.FileInputStream
import java.util.Properties
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.SaveMode

object ApmTsExample {

  def main(args: Array[String]) {

    val spark = SparkSession
      .builder()
      .appName("Spark Apm TS basic example")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()
    
    
    val props = new Properties();
    props.load(new FileInputStream("apmts-example.properties"));

    
    import scala.collection.JavaConverters._
    val tsdf=spark.read.format("predix.apm.ts").options(props.asScala.toMap).load()
    tsdf.show()
    tsdf.createOrReplaceTempView("temptable")
    val outputTag=props.getProperty("outputTag")
    val finalDF = spark.sql("select '"+outputTag+"' as tag, timestamp, value+10 as value, quality from temptable")

    finalDF.show(1000)
    finalDF.printSchema()

    val props2 = new Properties();
    props2.load(new FileInputStream("apmts-write-example.properties"));
    finalDF.write.format("predix.apm.ts").options(props2.asScala.toMap).mode(SaveMode.Overwrite).save()




    println("Finished writing to apm ts")
    spark.close()
  }
}
