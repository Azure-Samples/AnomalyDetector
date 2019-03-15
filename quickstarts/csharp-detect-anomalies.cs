// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
using System;
using System.IO;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;

namespace Console
{
    class Program
    {

        // Replace the subscriptionKey string with your valid subscription key.
        const string subscriptionKey = "[YOUR_SUBSCRIPTION_KEY]";

        //replace the endpoint URL with the correct one for your subscription. Your endpoint can be found in the Azure portal. For example: https://westus2.api.cognitive.microsoft.com
        const string endpoint = "[YOUR_ENDPOINT_URL]";

        // Replace the dataPath string with a path to the JSON formatted time series data.
        const string dataPath = "[PATH_TO_TIME_SERIES_DATA]";

        // Urls for anomaly detection on:
        // A batch of data points, or
        // The latest data point in the time series
        const string latestPointDetectionUrl = "/anomalydetector/v1.0/timeseries/last/detect";
        const string batchDetectionUrl = "/anomalydetector/v1.0/timeseries/entire/detect";


        static void Main(string[] args)
        {
            //read in the JSON time series data for the API request
            var requestData = File.ReadAllText(dataPath);

            detectAnomaliesBatch(requestData);
            detectAnomaliesLatest(requestData);
            System.Console.WriteLine("\nPress any key to exit ");
            System.Console.ReadKey();
        }

        static void detectAnomaliesBatch(string requestData)
        {
            System.Console.WriteLine("Detecting anomalies as a batch");

            //construct the request
            var result = Request(
                endpoint,
                batchDetectionUrl,
                subscriptionKey,
                requestData).Result;

            //deserialize the JSON object, and display it
            dynamic jsonObj = Newtonsoft.Json.JsonConvert.DeserializeObject(result);
            System.Console.WriteLine(jsonObj);

            //Find and display the positions of anomalies in the data set
            bool[] anomalies = jsonObj["isAnomaly"].ToObject<bool[]>();
            System.Console.WriteLine("\n Anomalies detected in the following data positions:");
            for (var i = 0; i < anomalies.Length; i++)
            {
                if (anomalies[i])
                {
                    System.Console.Write(i + ", ");
                }
            }
        }

        static void detectAnomaliesLatest(string requestData)
        {
            System.Console.WriteLine("\n\n Determining if latest data point is an anomaly");
            //construct the request
            var result = Request(
                endpoint,
                latestPointDetectionUrl,
                subscriptionKey,
                requestData).Result;

            //deserialize the JSON object, and display it
            dynamic jsonObj = Newtonsoft.Json.JsonConvert.DeserializeObject(result);
            System.Console.WriteLine(jsonObj);
        }

        /// <summary>
        /// Sends a request to the Anomaly Detection API to detect anomaly points
        /// </summary>
        /// <param name="apiAddress">Address of the API.</param>
        /// <param name="endpoint">The endpoint of the API</param>
        /// <param name="subscriptionKey">The subscription key applied  </param>
        /// <param name="requestData">The JSON string for requet data points</param>
        /// <returns>The JSON string for anomaly points and expected values.</returns>
        static async Task<string> Request(string apiAddress, string endpoint, string subscriptionKey, string requestData)
        {
            using (HttpClient client = new HttpClient { apiAddress = new Uri(apiAddress) })
            {
                System.Net.ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls12 | SecurityProtocolType.Tls11 | SecurityProtocolType.Tls;
                client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
                client.DefaultRequestHeaders.Add("Ocp-Apim-Subscription-Key", subscriptionKey);

                var content = new StringContent(requestData, Encoding.UTF8, "application/json");
                var res = await client.PostAsync(endpoint, content);
                if (res.IsSuccessStatusCode)
                {
                    return await res.Content.ReadAsStringAsync();
                }
                else
                {
                    return $"ErrorCode: {res.StatusCode}";
                }
            }
        }
    }
}