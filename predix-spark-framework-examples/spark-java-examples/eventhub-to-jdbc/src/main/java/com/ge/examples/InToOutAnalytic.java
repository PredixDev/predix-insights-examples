package com.ge.examples;

import com.ge.arf.rt.IAnalytics;
import com.ge.arf.rt.IRuntimeDataset;
import com.ge.arf.rt.config.HasConfig;

import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

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

        Dataset<Row> outputDf = ((SQLContext)inputDS.getContext()).sql("select body from `" + inputDS.getName() + "`");
        outputDf.show();

        outputs.put("writeTo", outputDf);
        logger.info("################# End of Input to Output Analytic  ######################");
        return outputs;
    }
}
