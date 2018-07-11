predix-insights-postman
=================

Swagger UI: https://insights-api.data-services.predix.io/api/v1/swagger-ui.html

## To Use

1. Make sure you're on the latest version of Postman
1. Import the postman collection
  + Open Postman and click 'import' in the top left
  + Select the file `predix-insights.postman_collection.json` found in this directory
1. Import the environment
  + Click the gear symbol in the top right area of Postman
  + Select 'manage environments'
  + click 'import' in the modal that appears
  + Select the file `predix-insights.postman_environment.json` found in this directory
  + Exit from the modal, and then in the environment dropdown in the top-right of Postman, select 'Predix Insights'
1. Customize the environment
  + currently, the environment contains:
    + `host` - specify hostname - by default, targets production API url
    + `Predix-Zone-Id` - also known as the Tenant ID. For API request header
    + `Authorization` - for API request header. Use in the format of `Bearer <token>`
    + `uaaIssuerId` - UAA instance URI + `/oauth/token`
    + `uaaClient` - UAA credentials
    + `uaaClientSecret` - UAA credentials
    + `current_<object>`
  + request headers:
    + `Predix-Zone-Id` - enter once in the environment and all requests will include it
    + `Authorization` - Generate a UAA token first and then and enter it in the environment in the format of `Bearer <token>`
  + the `current_` environment variables help when you're working with specific items multiple times
    + for example, if you wanted to upload a flow-template, create a flow attached to that template, and then launch the flow, you should:
      + upload the template, and then edit `current_flow-template` to reflect the UUID
      + upload the flow, and then edit `current_flow` to reflect that UUID
      + now you can submit all flow requests without having to edit the request itself
  + if you see something like this: `{{ environment_variable }}`, an environment variable is expected. Click the eye in the top right of Postman to check the current environment variables. You can also find a link to edit from there.

[![Analytics](https://ga-beacon.appspot.com/UA-82773213-1/predix-insights-examples/tree/master/predix-spark-connectors-examples/readme?pixel)](https://github.com/PredixDev)
