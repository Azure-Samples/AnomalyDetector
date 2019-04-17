import * as msRest from "@azure/ms-rest-js";
import { AnomalyDetectorClient, AnomalyDetectorModels, AnomalyDetectorMappers } from "@azure/cognitiveservices-anomalydetector";

function entire_detect_sample(endpoint: string, key: string){
  const options: msRest.ApiKeyCredentialOptions = {
    inHeader: {
      "Ocp-Apim-Subscription-Key": key
    }
  };

  const client = new AnomalyDetectorClient(new msRest.ApiKeyCredentials(options), endpoint);
  const body: AnomalyDetectorModels.Request = {
    series: [
      { timestamp: new Date("1962-01-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1962-02-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1962-03-01T00:00:00Z"), value: 0 },
      { timestamp: new Date("1962-04-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1962-05-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1962-06-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1962-07-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1962-08-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1962-09-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1962-10-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1962-11-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1962-12-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1963-01-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1963-02-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1963-03-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1963-04-01T00:00:00Z"), value: 0 },
      { timestamp: new Date("1963-05-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1963-06-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1963-07-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1963-08-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1963-09-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1963-10-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1963-11-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1963-12-01T00:00:00Z"), value: 1 },
    ],
    granularity: "monthly",
    customInterval: undefined,
    period: 0,
    maxAnomalyRatio: 0.25,
    sensitivity: 95
  };
  client.entireDetect(body).then((result) => {
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

function last_detect_sample(endpoint: string, key: string){
  const options: msRest.ApiKeyCredentialOptions = {
    inHeader: {
      "Ocp-Apim-Subscription-Key": key
    }
  };

  const client = new AnomalyDetectorClient(new msRest.ApiKeyCredentials(options), endpoint);
  const body: AnomalyDetectorModels.Request = {
    series: [
      { timestamp: new Date("1962-01-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1962-02-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1962-03-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1962-04-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1962-05-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1962-06-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1962-07-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1962-08-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1962-09-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1962-10-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1962-11-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1962-12-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1963-01-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1963-02-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1963-03-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1963-04-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1963-05-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1963-06-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1963-07-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1963-08-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1963-09-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1963-10-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1963-11-01T00:00:00Z"), value: 1 },
      { timestamp: new Date("1963-12-01T00:00:00Z"), value: 0 },
    ],
    granularity: "monthly",
    customInterval: undefined,
    period: 0,
    maxAnomalyRatio: 0.25,
    sensitivity: 95
  };
  client.lastDetect(body).then((result) => {
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


const endpoint = "[YOUR_ENDPOINT_URL]";
const key = "[YOUR_SUBSCRIPTION_KEY]";

entire_detect_sample(endpoint, key);
last_detect_sample(endpoint, key);

