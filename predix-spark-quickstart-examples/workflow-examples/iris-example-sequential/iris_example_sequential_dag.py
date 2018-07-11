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
#then specify the arguments that we created above. The catchup variable can be set to true or omited if the user
#desires the dags to try and run the missing dag run based on the presented schedule interval and the start date
#of the dag, running up to the current time.
dag = DAG('example_iris_dag1', schedule_interval = "*/5 * * * *", catchup=False, default_args=default_args)

#Here we define a Predix Insights Operator. This schedules a flow that we have created to run. In this case,
#we try to schedule a flow with the name 'iris_example_sequential_flow'. For this to succed, this flow needs
#to already be created in Predix Insights. The code and the zip file with the code that can be used with this
#example can be found in this repository following the path: 'predix-spark-quickstart-examples/pyspark-examples/IrisExample'.
#When uploading this zip, simply name the flow the same as the string used in the predix_insights_flow_name parameter below.
#Don't forget to configure the spark aguments for the flow as well after you create it.
task_one = PredixInsightsOperator(
    task_id='First_Iris_Run',
    predix_insights_flow_name='iris_example_sequential_flow',
    dag=dag)

#The second task we will run uses a python operator. We define two functions, one that runs some desired
#code, and another that is a callback for when the first function is succesfully ran.
def print_message (*args):
    print "Analytic Pipeline Completed"

def on_print_success(context):
    print "Final Print Completed"

#After the functions we want to use are defined, the operator is created and references the previous functions.
#Note that you are not required to included a callback function.
task_two = PythonOperator(
    task_id='example_print_operation',
    python_callable=print_message,
    on_success_callback=on_print_success,
    dag=dag)


#Again we create another Insights Operator. This runs a different execution of the flow that is specified,
#but still uses the same code.
task_three = PredixInsightsOperator(
    task_id='Second_Iris_Run',
    predix_insights_flow_name='iris_example_sequential_flow',
    dag=dag)

#Finally, we specify the order that we want these tasks to run. This is done by specifying per operator
#what is parent should be. I.E. task_one runs first and does not have an upstream. Task_two runs after
#task one, and so we need to specify that is is dependent on the previous upstream task running first.
task_two.set_upstream(task_one)
task_three.set_upstream(task_two)
