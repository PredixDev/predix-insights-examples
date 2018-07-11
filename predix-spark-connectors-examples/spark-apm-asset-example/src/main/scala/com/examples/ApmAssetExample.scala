package com.examples

import java.util.Properties
import java.io.FileInputStream
import org.apache.spark.sql.SparkSession

object ApmAssetExample {

  def main(args: Array[String]) {

    val spark = SparkSession
      .builder()
      .appName("Spark Apm Asset basic example")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    val props = new Properties();
    props.load(new FileInputStream("apmasset-example.properties"));
    
    import scala.collection.JavaConverters._
    val assetdf = spark.read
      .format("predix.apm.asset")
      .options(props.asScala.toMap)
      .load()

    assetdf.show()
    assetdf.printSchema()
  }
}
