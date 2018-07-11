import airflow
from airflow import DAG
from airflow.operators.predix_insights_operator import PredixInsightsOperator
from datetime import timedelta
import os

default_args = {
'owner': 'EMR-SA-86d0-4c60-a462-70f27b9d01c6', # any string
'start_date': airflow.utils.dates.days_ago(1), # start date
'retries': 1,                                  # number of retries
'retry_delay': timedelta(minutes=5)            # interval between retries
}

dag = DAG(
    os.path.splitext(os.path.basename(__file__))[0],
    default_args=default_args,
    schedule_interval="*/15 * * * *"
)

macro_flow1 = PredixInsightsOperator(
    task_id='pio-123',
    predix_insights_flow_name='quickstart-example-flow',
    dag=dag
)

macro_flow2 = PredixInsightsOperator(
    task_id='pio-456',
    predix_insights_flow_name='quickstart-example-flow',
    dag=dag
)

macro_flow2.set_upstream(macro_flow1)
