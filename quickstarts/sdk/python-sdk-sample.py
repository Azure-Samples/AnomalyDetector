from azure.cognitiveservices.anomalydetector import AnomalyDetectorClient
from azure.cognitiveservices.anomalydetector.models import Request, Point, Granularity, \
    APIErrorException
from msrest.authentication import CognitiveServicesCredentials
import pandas as pd


def entire_detect_sample(endpoint, key, request):
    print('Sample of detecting anomalies in the entire series.')

    client = AnomalyDetectorClient(endpoint, CognitiveServicesCredentials(key))
    response = client.entire_detect(request)
    if True in response.is_anomaly:
        print('Anomaly was detected from the series at index:')
        for i in range(len(series)):
            if response.is_anomaly[i]:
                print(i)
    else:
        print('There is no anomaly detected from the series.')


def last_detect_sample(endpoint, key, request):
    print('Sample of detecting whether the latest point in series is anomaly.')

    client = AnomalyDetectorClient(endpoint, CognitiveServicesCredentials(key))
    response = client.last_detect(request)
    if response.is_anomaly:
        print('The latest point is detected as anomaly.')
    else:
        print('The latest point is not detected as anomaly.')


def get_series_from_file(path):
    df = pd.read_csv(path, header=None, encoding='utf-8', parse_dates=[0])
    series = []
    for index, row in df.iterrows():
        series.append(Point(timestamp=row[0], value=row[1]))
    return series


if __name__ == "__main__":
    endpoint = '[YOUR_ENDPOINT_URL]'
    key = '[YOUR_SUBSCRIPTION_KEY]'
    path = "[PATH_TO_TIME_SERIES_DATA]"

    try:
        series = get_series_from_file(path)
        request = Request(series=series, granularity=Granularity.daily)

        entire_detect_sample(endpoint, key, request)
        last_detect_sample(endpoint, key, request)
    except Exception as e:
        if isinstance(e, APIErrorException):
            print('Error code: {}'.format(e.error.code))
            print('Error message: {}'.format(e.error.message))
        else:
            print(e)
