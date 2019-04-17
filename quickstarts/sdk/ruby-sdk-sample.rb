require 'azure_cognitiveservices_anomalydetector'
require 'date'

CognitiveServicesCredentials = MsRestAzure::CognitiveServicesCredentials 
AnomalyDetectorClient = Azure::CognitiveServices::AnomalyDetector::V1_0::AnomalyDetectorClient
Request = Azure::CognitiveServices::AnomalyDetector::V1_0::Models::Request
Point = Azure::CognitiveServices::AnomalyDetector::V1_0::Models::Point
Granularity = Azure::CognitiveServices::AnomalyDetector::V1_0::Models::Granularity

def create_point(timestamp, value)
    p = Point.new()
    p.timestamp = timestamp
    p.value = value
    return p 
end


class EntireDetectSample
    def EntireDetectSample.run(endpoint, key)
        puts "Sample of detecting anomalies in the entire series."

        client = AnomalyDetectorClient.new(CognitiveServicesCredentials.new(key))
        client.endpoint = endpoint

        # Create time series
        series = [
            create_point(DateTime.parse("1962-01-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1962-02-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1962-03-01T00:00:00Z"), 0),
            create_point(DateTime.parse("1962-04-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1962-05-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1962-06-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1962-07-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1962-08-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1962-09-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1962-10-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1962-11-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1962-12-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1963-01-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1963-02-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1963-03-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1963-04-01T00:00:00Z"), 0),
            create_point(DateTime.parse("1963-05-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1963-06-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1963-07-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1963-08-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1963-09-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1963-10-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1963-11-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1963-12-01T00:00:00Z"), 1)
        ]

        # Detection
        request = Request.new()
        request.series = series
        request.granularity = Granularity::Monthly
        request.max_anomaly_ratio = 0.25
        request.sensitivity = 95
        result = client.entire_detect(request) 

        if result.is_anomaly.include?(true) 
            puts "Anomaly was detected from the series at index:"
            for i in 0 .. series.length-1
                if result.is_anomaly[i]
                    print i
                    print " "
                end
            end
            puts 
        else
            puts "There is no anomaly detected from the series."
        end       
    end
end


class LastDetectSample
    def LastDetectSample.run(endpoint, key)
        puts "Sample of detecting whether the latest point in series is anomaly."

        client = AnomalyDetectorClient.new(CognitiveServicesCredentials.new(key))
        client.endpoint = endpoint

        # Create time series
        series = [
            create_point(DateTime.parse("1962-01-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1962-02-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1962-03-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1962-04-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1962-05-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1962-06-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1962-07-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1962-08-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1962-09-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1962-10-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1962-11-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1962-12-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1963-01-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1963-02-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1963-03-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1963-04-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1963-05-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1963-06-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1963-07-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1963-08-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1963-09-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1963-10-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1963-11-01T00:00:00Z"), 1),
            create_point(DateTime.parse("1963-12-01T00:00:00Z"), 0)
        ]

        # Detection
        request = Request.new()
        request.series = series
        request.granularity = Granularity::Daily
        request.max_anomaly_ratio = 0.25
        request.sensitivity = 95
        result = client.last_detect(request) 

        if result.is_anomaly
            puts "The latest point is detected as anomaly."
        else
            puts "The latest point is not detected as anomaly."
        end  
    end
end

endpoint = '[YOUR_ENDPOINT_URL]'
key = '[YOUR_SUBSCRIPTION_KEY]'

# Anomaly detection samples.
begin
    EntireDetectSample.run(endpoint, key)
    LastDetectSample.run(endpoint, key)
rescue Exception => e
    if e.kind_of?(MsRest::HttpOperationError)
        puts "Error code: #{e.body["code"]}"
        puts "Error message: #{e.body["message"]}"
    else
        puts e.message
    end
end
