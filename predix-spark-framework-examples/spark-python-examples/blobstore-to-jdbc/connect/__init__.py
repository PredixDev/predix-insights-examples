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
        logger.info("Inside HeatRate Analytic")
        for key in context_dict:
            print(key)
            print(context_dict[key])



        timeseriesReadDS = "inputDS"
        tsDF = context_dict[timeseriesReadDS].sql("select * from " + context_dict[timeseriesReadDS].table_name)
        tsDF.registerTempTable("timeseries")
        tsDF.show(10000, False)
        tsDF.printSchema()


        print("TIME SERIES DF")
        tsDF = context_dict[timeseriesReadDS].sql("select * from timeseries")
        tsDF.show(1000, False)
        tsDF.printSchema()

        result_dict = {"outputDS":tsDF}
        return result_dict
