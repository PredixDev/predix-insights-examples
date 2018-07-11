package com.ge.examples;

import com.ge.arf.rt.IAnalytics;
import com.ge.arf.rt.IRuntimeDataset;
import com.ge.arf.rt.config.HasConfig;

import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;



public class InToOutAnalytic extends HasConfig implements IAnalytics<SQLContext, Dataset<Row>> {

    private static Logger logger = Logger.getLogger(InToOutAnalytic.class);

    public Map<String, Dataset<Row>> runAnalysis(Map<String, IRuntimeDataset<SQLContext>> inputDatasets) {
        logger.info("################# Start of Input to Output Analytic  ######################");
        Map outputs = new HashMap();
        IRuntimeDataset inputDS = (IRuntimeDataset)inputDatasets.get("readFrom");
        if (inputDS == null) {
          logger.warn("Input Datasource is null, won't be able to run the analytic");
          return Collections.emptyMap();
        }

        StructType customSchema = new StructType(
            new StructField[] { new StructField("associatedMonitoredEntitySourceKey", DataTypes.StringType, true, Metadata.empty()),
                    new StructField("eventStart", DataTypes.LongType, true, Metadata.empty()),
                    new StructField("name", DataTypes.StringType, true, Metadata.empty()),
                    new StructField("severity", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("storageReceiveTime", DataTypes.LongType, true, Metadata.empty()),
            new StructField("taskId", DataTypes.StringType, true, Metadata.empty()),
        new StructField("type", DataTypes.StringType, true, Metadata.empty())
     });

        Dataset<Row> outputDf = ((SQLContext)inputDS.getContext()).read().schema(customSchema).json("s3://bucket-a6e56f98-6db8-4463-bf91-fa2188f8af32/alarm_output.json");

        //Dataset<Row> outputDf = ((SQLContext)inputDS.getContext()).sql("select * from `" + inputDS.getName() + "`");
        outputDf.show();

        outputs.put("writeTo", outputDf);
        logger.info("################# End of Input to Output Analytic  ######################");
        return outputs;
    }
}
