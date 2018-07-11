## Predix Insights Spark Quick Start Examples

The Quick Start Spark Examples provide a way to get started with Predix Insights features quickly. You can use them with the Predix Insights APIs or the Insights UI to get an understanding of how to use both to create and launch a flow. Instructions are provided using both the UI and API.

The high level steps are as follows. 
1. Use the prebuilt version of FlowPackage zip file supplied (or) build a FlowPackage zip using instructions given.
2. Upload the FlowPackage zip file to create a flow in Insights.
3. Launch the flow and check to see whether it successfully ran.
4. Create a DAG for the flow and check to see whether it successfully ran.

### Prepare FlowPackage ZIP file (Prebuilt or Build Using Script)

Use the prebuilt zip files from `dist/` folder and continue with the deploy & launch steps. 

(Or)

Run build.sh from the current quickstart examples folder. This will generate 3 zip files each with `<example folder name>.zip` under the ./dist folder.

`./build.sh`

### Using Predix Insights UI
#### 1. Create Flow 

> Note: You need a Predix Insights instance to deploy this example.

> Note: Following steps use Flows instead of FlowTemplate to best demonstrate the ease of use.

1. Log in to Predix Insights instance with credentials provided.
2. Navigate to `Develop -> Flows`.
3. Click `Upload` button on right top corner.
4. Enter Name, Description, and specify type (`SPARK_JAVA`/`SPARK_PYTHON`). Specify `SPARK_JAVA` for scala as well.
5. Click `Choose File` to select the FlowPackage zip file (scala, java or python example) from the dist folder.
6. Click `Upload`.

A Flow instance is created.


#### 2. Launch Flow

1. From Develop -> Flows page, open the newly created Flow instance by clicking the hyperlink with flow name.
2. In Flow instance page, click `Edit` on `Spark Arguments` section and select PASTE JSON.
3. In the text area, copy & paste the contents of the provided `<example folder name>/sparkargs` file and click Save.
4. Click Launch Flow.


#### 3. Check Flow Launch Success

1. Wait for the `Job Status` to be `FINISHED` & `Final application status` to be SUCCEEDED.
2. Click the container ending with 00001 (container of driver) and select `stdout` tab.
3. There should be a log which prints the Pi value, something like:
    `Pi is roughly 3.14288`

#### 4. Create DAG
1. Navigate to `Develop -> Orchestration`.
2. Click `Upload` button on right top corner.
3. Click on file-type drop-down menu and select 'AIRFLOW'
4. Click `Choose File` to select the DAG source(.py) file [quickstart-example-dag.py] from the `workflow-example` folder.
5. Click `Upload`.

#### 5. Check DAG Run Status

1. Navigate to `Monitor -> Orchestration`.
2. Navigate to `DAGS` tab to view the running DAG
3. Navigate to `Browse` tab and select any of the following menus (Task Instances, Logs, Jobs, DAG Runs) to see status of running tasks, dag runs scheduled etc.

### Using the PredixInsights API
#### 1. Create Flow

1. Get the UAA token using credentials provided.
```
curl -X GET \
  'https://<Predix UAA URL>/oauth/token?grant_type=client_credentials' \
  -H 'authorization: Basic <Base64 Encode ClientId:ClientSecret>'
```

2. Create a Flow using the FlowPackage zip file (scala, java or python example).
```
curl -X POST \
  https://<PredixInsights API HOST>/api/v1/flows \
  -H 'authorization: Bearer <UAA token from step1>' \
  -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \
  -H 'predix-zone-id: <Insights Zone-Id>' \
  -F 'metadata={"version": "1.0",   "user": "user",   "name": "quickstart-example-flow",   "description": "desc",   "type": "SPARK_JAVA" , "tags":["tag1:val1", "tag2:val2"]}' \
  -F file=@<FilePath>/<FlowPackage>.zip
```
Here type can be `SPARK_JAVA` for Java & Scala example and `SPARK_PYTHON` for Python example

#### 2. Launch Flow

Request

```
curl -X POST \
https://<PredixInsights API HOST>/api/v1/flows/quickstart-example-flow \
-H 'Authorization: <UAA token from step1> \
-H 'Predix-Zone-Id: <Insights Zone-Id>'
```

Response would be like below, grab the `<yarn-application-id>` from this. Its going to be used for checking results.

```
{
    "id": "<yarn-application-id>",
    "applicationType": "SPARK",
    "startTime": <startTime>,
    "finishTime": 0,
    "status": "ACCEPTED",
    "tags": [],
    "flow": {
        "id": "<uuid-of-flow>",
        "name": "quickstart-example-flow"
    },
    "submitDetails": ......
}
```
#### 3. Check Flow Launch Success

Request to get application status from yarn, look for `summary.status` &  `details.finalApplicationStatus` from the reponse. It has to be `FINISHED` & `SUCCEEDED` repectively.

```
curl -X GET \
  https://<PredixInsights API HOST>/api/v1/instances/<yarn-application-id> \
  -H 'Authorization: <UAA token from step1> \
  -H 'Predix-Zone-Id: <Insights Zone-Id>'
```
To see the results of the launched spark flow

1. Get the container id from the response.

Request
```
curl -X GET \
  https://<PredixInsights API HOST>/api/v1/instances/<yarn-application-id>/containers/ \
  -H 'Authorization: <UAA token from step1> \
  -H 'Predix-Zone-Id: <Insights Zone-Id>' \
  
```
Response
```
[
    {
        "containerId": "<container-id-2>",
        "startTime": 1520459003697,
        "finishTime": 1520459008652,
        "node": "http://...:8042",
        "memoryMB": 1408,
        "vcores": 1
    },
    {
        "containerId": "<container-id-1>",
        "startTime": 1520459008754,
        "finishTime": 1520459014104,
        "node": "http://...:8042",
        "memoryMB": 1408,
        "vcores": 1
    }
]
```

2. Get the stdout on that container id

```
curl -X GET \
  https://<PredixInsights API HOST>/api/v1/instances/<yarn-application-id>/containers/<container-id-1>/logs/stdout \
  -H 'Authorization: <UAA token from step1> \
  -H 'Predix-Zone-Id: <Insights Zone-Id>'
```

#### 4. Create DAG

1. Get the UAA token using credentials provided.
```
curl -X GET \
  'https://<Predix UAA URL>/oauth/token?grant_type=client_credentials' \
  -H 'authorization: Basic <Base64 Encode ClientId:ClientSecret>'
```

2. Create a DAG using the DAG source/zip file (python).
```
curl -X POST \
  https://<PredixInsights API HOST>/api/v1/dags \
  -H 'authorization: Bearer <UAA token from step1>' \
  -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \
  -H 'predix-zone-id: <Insights Zone-Id>' \
  -F 'metadata={"version": "1.0", "name": "quickstart-example-dag",   "user": "user",   "description": "desc"}' \
  -F file=@<FilePath>/<Dag>.py
```
zip files can uploaded as well.
```
curl -X POST \
  https://<PredixInsights API HOST>/api/v1/dags \
  -H 'authorization: Bearer <UAA token from step1>' \
  -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \
  -H 'predix-zone-id: <Insights Zone-Id>' \
  -F 'metadata={"version": "1.0", "name": "quickstart-example-dag",   "user": "user",   "description": "desc"}' \
  -F file=@<FilePath>/<Dag>.zip
```
3. Monitor status of running dags.

a. Query DAG status

```
curl -X GET \
  https://<PredixInsights API HOST>/api/v1/dags/status/quickstart-example-dag \
  -H 'authorization: Bearer <UAA token from step1>' \
  -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \
  -H 'predix-zone-id: <Insights Zone-Id>' \
``` 

b. Query DAG runs status

```
curl -X GET \
  https://<PredixInsights API HOST>/api/v1/dags/status/quickstart-example-dag/runs \
  -H 'authorization: Bearer <UAA token from step1>' \
  -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \
  -H 'predix-zone-id: <Insights Zone-Id>' \
```
For particular run, use <run-id> in query
```
curl -X GET \
  https://<PredixInsights API HOST>/api/v1/dags/status/quickstart-example-dag/run/<run-id> \
  -H 'authorization: Bearer <UAA token from step1>' \
  -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \
  -H 'predix-zone-id: <Insights Zone-Id>' \
```

c. Query DAG tasks status
```
curl -X GET \
  https://<PredixInsights API HOST>/api/v1/dags/status/quickstart-example-dag/tasks \
  -H 'authorization: Bearer <UAA token from step1>' \
  -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \
  -H 'predix-zone-id: <Insights Zone-Id>' \
```
For particular task, use <task-id> in query
```
curl -X GET \
  https://<PredixInsights API HOST>/api/v1/dags/status/quickstart-example-dag/run/<task-id> \
  -H 'authorization: Bearer <UAA token from step1>' \
  -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \
  -H 'predix-zone-id: <Insights Zone-Id>' \
```

[![Analytics](https://ga-beacon.appspot.com/UA-82773213-1/predix-insights-examples/readme?pixel)](https://github.com/PredixDev)