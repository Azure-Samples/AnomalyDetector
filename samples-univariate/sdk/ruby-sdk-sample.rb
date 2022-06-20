require "azure_cognitiveservices_anomalydetector"
require "date"
require "csv"

CognitiveServicesCredentials = MsRestAzure::CognitiveServicesCredentials 
AnomalyDetectorClient = Azure::CognitiveServices::AnomalyDetector::V1_0::AnomalyDetectorClient
Request = Azure::CognitiveServices::AnomalyDetector::V1_0::Models::Request
Point = Azure::CognitiveServices::AnomalyDetector::V1_0::Models::Point
Granularity = Azure::CognitiveServices::AnomalyDetector::V1_0::Models::Granularity

def entire_detect_sample(endpoint, key, request)
    puts "Sample of detecting anomalies in the entire series."

    client = AnomalyDetectorClient.new(CognitiveServicesCredentials.new(key))
    client.endpoint = endpoint

    result = client.entire_detect(request) 

    if result.is_anomaly.include?(true) 
        puts "Anomaly was detected from the series at index:"
        for i in 0 .. request.series.length-1
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

def last_detect_sample(endpoint, key, request)
    puts "Sample of detecting whether the latest point in series is anomaly."

    client = AnomalyDetectorClient.new(CognitiveServicesCredentials.new(key))
    client.endpoint = endpoint

    result = client.last_detect(request) 

    if result.is_anomaly
        puts "The latest point is detected as anomaly."
    else
        puts "The latest point is not detected as anomaly."
    end  
end

def get_series_from_file(path)
    result = []
    csv = CSV.read(path, headers: false)
    csv.each do |item|
        p = Point.new()
        p.timestamp = DateTime.parse(item[0])
        p.value = item[1].to_f
        result.append p
    end
    return result
end


endpoint = "[YOUR_ENDPOINT_URL]"
key = "[YOUR_SUBSCRIPTION_KEY]"
path = "[PATH_TO_TIME_SERIES_DATA]"

# Anomaly detection samples.
begin
    series = get_series_from_file(path)
    request = Request.new()
    request.series = series
    request.granularity = Granularity::Daily

    entire_detect_sample(endpoint, key, request)
    last_detect_sample(endpoint, key, request)
rescue Exception => e
    if e.kind_of?(MsRest::HttpOperationError)
        puts "Error code: #{e.body["code"]}"
        puts "Error message: #{e.body["message"]}"
    else
        puts e.message
    end
end
