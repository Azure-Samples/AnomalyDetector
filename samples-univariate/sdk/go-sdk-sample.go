package main

import (
	"bufio"
	"context"
	"encoding/csv"
	"fmt"
	"github.com/Azure/azure-sdk-for-go/services/preview/cognitiveservices/v1.0/anomalydetector"
	"github.com/Azure/go-autorest/autorest"
	"github.com/Azure/go-autorest/autorest/date"
	"io"
	"log"
	"os"
	"strconv"
	"time"
)

func getAnomalyDetectorClient(endpoint string, key string) anomalydetector.BaseClient {
	client := anomalydetector.New(endpoint)
	client.Authorizer = autorest.NewCognitiveServicesAuthorizer(key)
	return client
}

func getSeriesFromFile(path string) []anomalydetector.Point {
	var series []anomalydetector.Point

	csvFile, _ := os.Open(path)
	reader := csv.NewReader(bufio.NewReader(csvFile))
	for {
		line, err := reader.Read()
		if err == io.EOF {
			break
		} else if err != nil {
			log.Fatal(err)
		}
		timestamp, _ := time.Parse(time.RFC3339, line[0])
		value, _ := strconv.ParseFloat(line[1], 64)

		series = append(series, anomalydetector.Point{Timestamp: &date.Time{timestamp}, Value: &value})
	}
	return series
}

func entireDetectSample(endpoint string, key string, request anomalydetector.Request) {
	fmt.Println("Sample of detecting anomalies in the entire series.")
	client := getAnomalyDetectorClient(endpoint, key)
	response, err := client.EntireDetect(context.Background(), request)
	if err != nil {
		log.Fatal("ERROR:", err)
	}

	var anomalies []int
	for idx, isAnomaly := range *response.IsAnomaly {
		if isAnomaly {
			anomalies = append(anomalies, idx)
		}
	}
	if len(anomalies) > 0 {
		fmt.Println("Anomaly was detected from the series at index:")
		for _, idx := range anomalies {
			fmt.Println(idx)
		}
	} else {
		fmt.Println("There is no anomaly detected from the series.")
	}
}

func lastDetectSample(endpoint string, key string, request anomalydetector.Request) {
	fmt.Println("Sample of detecting whether the latest point in series is anomaly.")
	client := getAnomalyDetectorClient(endpoint, key)
	response, err := client.LastDetect(context.Background(), request)
	if err != nil {
		log.Fatal("ERROR:", err)
	}

	if *response.IsAnomaly {
		fmt.Println("The latest point is detected as anomaly.")
	} else {
		fmt.Println("The latest point is not detected as anomaly.")
	}
}


func main() {
	var endpoint = "[YOUR_ENDPOINT_URL]"
	var key  = "[YOUR_SUBSCRIPTION_KEY]"
	var path = "[PATH_TO_TIME_SERIES_DATA]"

	var series = getSeriesFromFile(path)
	var request = anomalydetector.Request{Series: &series, Granularity: anomalydetector.Daily}

	entireDetectSample(endpoint, key, request)
	lastDetectSample(endpoint, key, request)
}
