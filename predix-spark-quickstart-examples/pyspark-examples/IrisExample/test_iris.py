from __future__ import print_function
from pyspark.ml.classification import LogisticRegression, OneVsRest
from pyspark.ml.evaluation import MulticlassClassificationEvaluator
from pyspark.sql import SparkSession
import os


if __name__ == "__main__":
    spark = SparkSession \
        .builder \
        .appName("OneVsRestExample") \
        .getOrCreate()

    # load data file.
    cwd = os.getcwd()
    inputData = spark.read.format("libsvm").load('file://' + cwd + '/iris_data.txt')

    # generate the train/test split.
    (train, test) = inputData.randomSplit([0.8, 0.2])

    # instantiate the base classifier.
    lr = LogisticRegression(maxIter=3, tol=1E-6, fitIntercept=True)

    # instantiate the One Vs Rest Classifier.
    ovr = OneVsRest(classifier=lr)

    # train the multiclass model.
    ovrModel = ovr.fit(train)

    # score the model on test data.
    predictions = ovrModel.transform(test)

    # obtain evaluator.
    evaluator = MulticlassClassificationEvaluator(metricName="accuracy")

    # compute the classification error on test data.
    accuracy = evaluator.evaluate(predictions)
    print("Test Error = %g" % (1.0 - accuracy))

    spark.stop()
