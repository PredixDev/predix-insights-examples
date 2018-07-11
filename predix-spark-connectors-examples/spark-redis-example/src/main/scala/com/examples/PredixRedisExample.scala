package com.examples

import com.redislabs.provider.redis._
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType
import org.apache.spark.rdd.RDD


import org.apache.spark.sql.DataFrameWriter
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.functions._
import scala.io.Source.fromInputStream

object PredixRedisExample {
  def main(args: Array[String]) {

    val spark = SparkSession
      .builder()
      .appName("Spark Predix Redis basic example")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    val content = fromInputStream(getClass.getClassLoader.getResourceAsStream("blog")).getLines.toArray.mkString("\n")
    val sc = spark.sparkContext
    val wcnts = sc.parallelize(content.split("\\W+").filter(!_.isEmpty)).map((_, 1)).reduceByKey(_ + _).map(x => (x._1, x._2.toString))
    val wds = sc.parallelize(content.split("\\W+").filter(!_.isEmpty))

    val redisConfig = new RedisConfig(new RedisEndpoint("YOUR_URL", 1111, "YOUR_PASSWORD"))

    // Flush all the hosts
    redisConfig.hosts.foreach( node => {
      val conn = node.connect
      conn.flushAll
      conn.close
    })

    sc.toRedisZSET(wcnts, "all:words:cnt:sortedset")(redisConfig)
    sc.toRedisHASH(wcnts, "all:words:cnt:hash" )(redisConfig)
    sc.toRedisLIST(wds, "all:words:list" )(redisConfig)
    sc.toRedisSET(wds, "all:words:set")(redisConfig)
    sc.toRedisKV(wcnts)(redisConfig)

    val sqlContext= new org.apache.spark.sql.SQLContext(sc)
    import sqlContext.implicits._

    val redisZSetRDD = sc.fromRedisZSet("all:words:cnt:sortedset")(redisConfig)
    val redisZSetDF = redisZSetRDD.toDF()
    redisZSetDF.show()

    val redisHashRDD = sc.fromRedisHash("all:words:cnt:hash")(redisConfig)
    val redisHashDF = redisHashRDD.toDF()
    redisHashDF.show()

    val redisListRDD = sc.fromRedisList("all:words:list")(redisConfig)
    val redisListDF = redisListRDD.toDF()
    redisListDF.show()

    val redisSetRDD = sc.fromRedisSet("all:words:set")(redisConfig)
    val redisSetDF = redisSetRDD.toDF()
    redisSetDF.show()

    val redisKVRDD = sc.fromRedisKV("*")(redisConfig)
    val redisKVDF = redisKVRDD.toDF()
    redisKVDF.show()
  }
}
