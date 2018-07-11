from pyspark import SparkContext
from pyspark.sql import *
from pyspark.sql import functions
from pyspark.sql.types import StructType, StructField, StringType, IntegerType, LongType
from pyspark.sql.dataframe import *
from pyspark.sql.functions import *
from pyspark.sql import Row
import sys

class TestJob():
    def run_job(self, spark_session, runtime_config, job_json, context_dict, logger):
        print("*"*80)
        # generate python dataframe
        logger.info("Inside dummy Analytic")
        for key in context_dict:
            print(key)
            print(context_dict[key])

        timeseriesReadDS = "timeseriesReadDS"
        tsDF = context_dict[timeseriesReadDS].sql("select * from " + context_dict[timeseriesReadDS].table_name)
        tsDF.registerTempTable("timeseries")
        #tsDF.show()
        tsDF.printSchema()
        print("input tsDF.count()")
        print(tsDF.count())

        print("TIME SERIES DF")
        outDF = context_dict[timeseriesReadDS].sql("select tag, timestamp, value + 10 as value, quality from timeseries")
        print("TIME SERIES outDF.count()")
        print(outDF.count())
        #tsDF.show()
        outDF.printSchema()

        result_dict = {"tsWriteDS":outDF}
        return result_dict
