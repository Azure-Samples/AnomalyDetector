// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
namespace ConsoleApplication1
{
    using System;
    using System.IO;
    using System.Linq;
    using System.Collections.Generic;
    using System.Threading.Tasks;
    using Microsoft.Azure.CognitiveServices.AnomalyDetector;
    using Microsoft.Azure.CognitiveServices.AnomalyDetector.Models;

    class Program
    {
        static void Main(string[] args)
        {
            string endpoint = "[YOUR_ENDPOINT_URL]";
            string key = "[YOUR_SUBSCRIPTION_KEY]";
            string path = "[PATH_TO_TIME_SERIES_DATA]";

            // Anomaly detection samples.
            try
            {
                EntireDetectSampleAsync(endpoint, key, path).Wait();
                LastDetectSampleAsync(endpoint, key, path).Wait();
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                if (e.InnerException != null && e.InnerException is APIErrorException)
                {
                    APIError error = ((APIErrorException)e.InnerException).Body;
                    Console.WriteLine("Error code: " + error.Code);
                    Console.WriteLine("Error message: " + error.Message);
                }
                else if (e.InnerException != null)
                {
                    Console.WriteLine(e.InnerException.Message);
                }
            }

            Console.WriteLine("\nPress ENTER to exit.");
            Console.ReadLine();
        }

        static List<Point> GetSeriesFromFile(string path)
        {
            return File.ReadAllLines(path)
                .Where(e => e.Trim().Length != 0)
                .Select(e => e.Split(','))
                .Where(e => e.Length == 2)
                .Select(e => new Point(DateTime.Parse(e[0]), Double.Parse(e[1]))).ToList();
        }

        static async Task EntireDetectSampleAsync(string endpoint, string key, string path)
        {
            Console.WriteLine("Sample of detecting anomalies in the entire series.");

            IAnomalyDetectorClient client = new AnomalyDetectorClient(new ApiKeyServiceClientCredentials(key))
            {
                Endpoint = endpoint
            };

            List<Point> series = GetSeriesFromFile(path);
            Request request = new Request(series, Granularity.Daily);
            EntireDetectResponse result = await client.EntireDetectAsync(request).ConfigureAwait(false);

            if (result.IsAnomaly.Contains(true))
            {
                Console.WriteLine("Anomaly was detected from the series at index:");
                for (int i = 0; i < series.Count; ++i)
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
                Console.WriteLine("There is no anomaly detected from the series.");
            }
        }

        static async Task LastDetectSampleAsync(string endpoint, string key, string path)
        {
            Console.WriteLine("Sample of detecting whether the latest point in series is anomaly");

            IAnomalyDetectorClient client = new AnomalyDetectorClient(new ApiKeyServiceClientCredentials(key))
            {
                Endpoint = endpoint
            };

            List<Point> series = GetSeriesFromFile(path);
            Request request = new Request(series, Granularity.Daily);
            LastDetectResponse result = await client.LastDetectAsync(request).ConfigureAwait(false);

            if (result.IsAnomaly)
            {
                Console.WriteLine("The latest point is detected as anomaly.");
            }
            else
            {
                Console.WriteLine("The latest point is not detected as anomaly.");
            }
        }
    }
}