import * as msRest from "@azure/ms-rest-js";
import { AnomalyDetectorClient, AnomalyDetectorModels, AnomalyDetectorMappers } from "@azure/cognitiveservices-anomalydetector";
import * as fs from 'fs';
import parse from 'csv-parse/lib/sync';

function entire_detect_sample(endpoint: string, key: string, request: AnomalyDetectorModels.Request){
  console.log("Sample of detecting anomalies in the entire series.");
  const options: msRest.ApiKeyCredentialOptions = {
    inHeader: {
      "Ocp-Apim-Subscription-Key": key
    }
  };

  const client = new AnomalyDetectorClient(new msRest.ApiKeyCredentials(options), endpoint);
  client.entireDetect(request).then((result) => {
    if(result.isAnomaly.some(function(e){return e === true;})){
      console.log("Anomaly was detected from the series at index:");
      result.isAnomaly.forEach(function(e, i){
        if(e === true) console.log(i);
      });
    }else{
      console.log("There is no anomaly detected from the series.");
    }
  }).catch((err) => {
    if(err.body !== undefined){
      console.error("Error code: " + err.body.code);
      console.error("Error message: " + err.body.message);
    }else{
      console.error(err);
    }
  });
}

function last_detect_sample(endpoint: string, key: string, request: AnomalyDetectorModels.Request){
  console.log("Sample of detecting whether the latest point in series is anomaly.");
  const options: msRest.ApiKeyCredentialOptions = {
    inHeader: {
      "Ocp-Apim-Subscription-Key": key
    }
  };

  const client = new AnomalyDetectorClient(new msRest.ApiKeyCredentials(options), endpoint);
  client.lastDetect(request).then((result) => {
    if(result.isAnomaly){
      console.log("The latest point is detected as anomaly.");
    }else{
      console.log("The latest point is not detected as anomaly.");
    }
  }).catch((err) => {
    if(err.body !== undefined){
      console.error("Error code: " + err.body.code);
      console.error("Error message: " + err.body.message);
    }else{
      console.error(err);
    }
  });
}

function read_series_from_file(path: string): Array<AnomalyDetectorModels.Point>{
  let result = Array<AnomalyDetectorModels.Point>();
  let input = fs.readFileSync(path).toString();
  let parsed = parse(input, {skip_empty_lines:true});
  parsed.forEach(function(e: Array<string>){
    result.push({timestamp:new Date(e[0]), value:Number(e[1])});
  });
  return result;
}

const endpoint = "[YOUR_ENDPOINT_URL]";
const key = "[YOUR_SUBSCRIPTION_KEY]";
const path = "[PATH_TO_TIME_SERIES_DATA]";

const request: AnomalyDetectorModels.Request = {
  series: read_series_from_file(path),
  granularity: "daily",
};
entire_detect_sample(endpoint, key, request);
last_detect_sample(endpoint, key, request);

