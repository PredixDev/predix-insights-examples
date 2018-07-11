## Predix Insights Examples

Briefly this examples section covers the following.

1. How to execute a simple spark example in Predix Insights
2. How to implement and execute a Spark App with Predix Connectors
3. How to implement and execute a Spark App with Predix Spark Framework
4. How to implement and execute an orchestration with Predix Workflow

Look at each specific sections README to learn more on whats the advantage of using one over other. 

### When to use Predix Spark Framework
1. If you need a quicker way to federate data from multiple sources and also write data to multiple sources.
2. If you dont want to worry about encrypting your credentials
3. If you want to apply dynamic/runtime property configurations 
4. Underlying this framework uses connectors to federate data so performance wise both should be same

### When to use Predix Connectors
1. If you query data from 1 or 2 data sources
2. If you have large data in Predix Datasources that you wanted to run some analytic on and dont have to worry about performance tuning of it

## When to use Predix Workflow
1. If you have multiple Predix Insights flows and want to impose a schedule interval and dependency among the flows
2. If you want to pass meta data between multiple Predix Insights flows without using an external data source

[![Analytics](https://ga-beacon.appspot.com/UA-82773213-1/predix-insights-examples/readme?pixel)](https://github.com/PredixDev)