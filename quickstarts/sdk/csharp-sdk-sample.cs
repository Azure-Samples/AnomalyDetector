/*
Copyright (c) Microsoft Corporation. All rights reserved.
Licensed under the MIT License.


This sample demonstrates the Anomaly Detection service's two detection methods:
    * Anomaly detection on an entire time-series dataset.
    * Anomaly detection on the latest data point in a dataset.

 * Prerequisites:
     * The Anomaly Detector client library for .NET
     * A .csv file containing a time-series data set with 
        UTC-timestamp and numerical values pairings. 
        Example data is included in this repo.
*/

namespace AnomalyDetectorSample
{
    // <usingStatements>
    using System;
    using System.IO;
    using System.Text;
    using System.Linq;
    using System.Collections.Generic;
    using System.Threading.Tasks;
    using Azure.AI.AnomalyDetector;
    using Azure;
    using Azure.AI.AnomalyDetector.Models;
    using System.Reflection;
    // </usingStatements>

    class Program
    {

        // <mainMethod>
        static void Main(string[] args)
        {
            //This sample assumes you have created an environment variable for your key and endpoint
            string endpoint = Environment.GetEnvironmentVariable("ANOMALY_DETECTOR_ENDPOINT");
            string key = Environment.GetEnvironmentVariable("ANOMALY_DETECTOR_KEY");
            string datapath = Path.Combine(Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location), "request-data.csv");

            var endpointUri = new Uri(endpoint);
            var credential = new AzureKeyCredential(key);

            AnomalyDetectorClient client = new AnomalyDetectorClient(endpointUri, credential);

            DetectRequest request = new DetectRequest(GetSeriesFromFile(datapath), TimeGranularity.Daily); // The request payload with points from the data file
            ChangePointDetectRequest changePointDetectRequest = new ChangePointDetectRequest(GetSeriesFromFile(datapath), TimeGranularity.Daily);

            EntireDetectSampleAsync(client, request).Wait(); // Async method for batch anomaly detection
            LastDetectSampleAsync(client, request).Wait(); // Async method for analyzing the latest data point in the set
            DetectChangePoint(client, changePointDetectRequest).Wait(); // Async method for change point detection

            Console.WriteLine("\nPress ENTER to exit.");
            Console.ReadLine();
        }
        // </mainMethod>

        // <runSamplesHelper>
        //Run the anomaly detection examples with extra error handling
        static void runSamples(AnomalyDetectorClient client, string dataPath)
        {

            try
            {
                DetectRequest request = new DetectRequest(GetSeriesFromFile(dataPath), TimeGranularity.Daily);

                EntireDetectSampleAsync(client, request).Wait();
                LastDetectSampleAsync(client, request).Wait();
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                if (e.InnerException != null && e.InnerException is RequestFailedException exception)
                {
                    Console.WriteLine("Error code: " + exception.ErrorCode);
                    Console.WriteLine("Error message: " + exception.Message);
                }

                else if (e.InnerException != null)
                {
                    Console.WriteLine(e.InnerException.Message);
                }
            }
        }
        // </runSamplesHelper>

        // <GetSeriesFromFile>
        static List<TimeSeriesPoint> GetSeriesFromFile(string path)
        {
            List<TimeSeriesPoint> list = File.ReadAllLines(path, Encoding.UTF8)
                .Where(e => e.Trim().Length != 0)
                .Select(e => e.Split(','))
                .Where(e => e.Length == 2)
                .Select(e => new TimeSeriesPoint(DateTime.Parse(e[0]), float.Parse(e[1]))).ToList();

            return list;
        }
        // </GetSeriesFromFile>

        // <entireDatasetExample>
        static async Task EntireDetectSampleAsync(AnomalyDetectorClient client, DetectRequest request)
        {
            Console.WriteLine("Detecting anomalies in the entire time series.");

            EntireDetectResponse result = await client.DetectEntireSeriesAsync(request).ConfigureAwait(false);

            if (result.IsAnomaly.Contains(true))
            {
                Console.WriteLine("An anomaly was detected at index:");
                for (int i = 0; i < request.Series.Count; ++i)
                {
                    if (result.IsAnomaly[i])
                    {
                        Console.Write(i);
                        Console.Write(" ");
                    }
                }
                Console.WriteLine();
            }
            else
            {
                Console.WriteLine(" No anomalies detected in the series.");
            }
        }
        // </entireDatasetExample>

        // <latestPointExample>
        static async Task LastDetectSampleAsync(AnomalyDetectorClient client, DetectRequest request)
        {

            Console.WriteLine("Detecting the anomaly status of the latest point in the series.");

            LastDetectResponse result = await client.DetectLastPointAsync(request).ConfigureAwait(false);

            if (result.IsAnomaly)
            {
                Console.WriteLine("The latest point was detected as an anomaly.");
            }
            else
            {
                Console.WriteLine("The latest point was not detected as an anomaly.");
            }
        }
        // </latestPointExample>

        // <changePointExample>
        public static async Task DetectChangePoint(AnomalyDetectorClient client, ChangePointDetectRequest request)
        {
            Console.WriteLine("Detecting the change points in the series.");

            ChangePointDetectResponse result = await client.DetectChangePointAsync(request).ConfigureAwait(false);

            if (result.IsChangePoint.Contains(true))
            {
                Console.WriteLine("A change point was detected at index:");
                for (int i = 0; i < request.Series.Count; ++i)
                {
                    if (result.IsChangePoint[i])
                    {
                        Console.Write(i);
                        Console.Write(" ");
                    }
                }
                Console.WriteLine();
            }
            else
            {
                Console.WriteLine("No change point detected in the series.");
            }
        }
        // </changePointExample>
    }
}
