import airflow
from airflow import DAG
from airflow.operators.predix_insights_operator import PredixInsightsOperator
from airflow.operators.python_operator import PythonOperator
from datetime import timedelta, datetime

#Sets the default arguments the dag needs. The owner can be any desired user defined string.
default_args = {
    'owner': 'example_owner',
    'start_date': datetime(2018, 5, 10, 0, 0),
    'retries': 2,
    'retry_delay': timedelta(minutes=2),
}

#Define the dag object to be used. First specify the name, the schedule using chron syntac (currently every 5 minutes),
#then specify the arguments that we created above.The catchup variable can be set to true or omited if the user
#desires the dags to try and run the missing dag run based on the presented schedule interval and the start date
#of the dag, running up to the current time.
dag = DAG('new_dag_name_parallel1', schedule_interval = "*/5 * * * *", catchup=False, default_args=default_args)


#This operator creates a Predix Insights Operator. It refers to and requires a flow to be uploaded in
#Predix Insights. The name specified in the 'predix_insights_flow_name' need to match the name the
#was created. An example zip and the code associated with it can be found in this repo at
#'predix-spark-quickstart-examples/pyspark-examples/IrisExample/'.
task_one = PredixInsightsOperator(
    task_id='First_PI_Operator',
    predix_insights_flow_name='iris_example_flow',
    dag=dag)


#Here is an example python Operator. First we define two functions. The first contains the code we want
#to be ran by the operator. The second is a calback function that is called when the first functions
#is ran successfully.
def print_message (*args):
    print "Hello from Pyop B"

def on_print_success(context):
    print "Print A Completed"

#Both functions are then referenced by name in the creation of the operator. Note that an on success callback
#is not required for the python operator.
task_two_a = PythonOperator(
    task_id='print_operation_a',
    python_callable=print_message,
    on_success_callback=on_print_success,
    dag=dag)


#We define another python operator to frun in parallel with the first.
def print_other_message (*args):
    print "Hello from Pyop B"

def on_print_other_success(context):
    print "Print B Completed"

task_two_b = PythonOperator(
    task_id='print_operation_b',
    python_callable=print_other_message,
    on_success_callback=on_print_other_success,
    dag=dag)


#We then define another insights flow, using the same flow we used in the first task. This create a
#new execution of the flow using the same code. This will wait for both the parallel python operators
#to finish before executing
task_three = PredixInsightsOperator(
    task_id='Second_PI_Operator',
    predix_insights_flow_name='iris_example_flow',
    dag=dag)

#Finally we set the order the operators run in by specifying the parent of each operator.
task_two_a.set_upstream(task_one)
task_two_b.set_upstream(task_one)
task_three.set_upstream(task_two_a)
task_three.set_upstream(task_two_b)
