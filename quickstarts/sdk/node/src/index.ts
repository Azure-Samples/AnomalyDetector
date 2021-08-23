import { AzureKeyCredential } from "@azure/core-auth";
import { AnomalyDetectorClient, DetectRequest, DetectEntireResponse, TimeSeriesPoint, TimeGranularity } from "@azure/ai-anomaly-detector";
import * as fs from "fs";
import parse from "csv-parse/lib/sync";

function entire_detect_sample(endpoint: string, key: string, request: DetectRequest){
  console.log("Sample of detecting anomalies in the entire series.");


  const client = new AnomalyDetectorClient(endpoint ,new AzureKeyCredential(key));
  client.detectEntireSeries(request).then((result) => {
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

function last_detect_sample(endpoint: string, key: string, request: DetectRequest){
  console.log("Sample of detecting whether the latest point in series is anomaly.");

  const client = new AnomalyDetectorClient(endpoint, new AzureKeyCredential(key));
  client.detectLastPoint(request).then((result) => {
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

function read_series_from_file(path: string): Array<TimeSeriesPoint>{
  let result = Array<TimeSeriesPoint>();
  let input = fs.readFileSync(path).toString();
  let parsed = parse(input, {skip_empty_lines:true});
  parsed.forEach(function(e: Array<string>){
    result.push({timestamp:new Date(e[0]), value:Number(e[1])});
  });
  return result;
}

const endpoint = "[YOUR_ANOMALY_DETECTOR_ENDPOINT_URL]";
const key = "[YOUR_ANOMALY_DETECTOR_KEY]";
const path = "[PATH_TO_TIME_SERIES_DATA]";

const request: DetectRequest = {
  series: read_series_from_file(path),
  granularity: TimeGranularity.daily,
};
entire_detect_sample(endpoint, key, request);
last_detect_sample(endpoint, key, request);

