from azure.cognitiveservices.anomalydetector import AnomalyDetectorClient
from azure.cognitiveservices.anomalydetector.models import Request, Point, Granularity, \
    APIErrorException
from datetime import datetime, timezone
from msrest.authentication import CognitiveServicesCredentials


def entire_dectect_sample(endpoint, key):
    print('Sample of detecting anomalies in the entire series.')

    client = AnomalyDetectorClient(endpoint, CognitiveServicesCredentials(key))

    series = [
        Point(timestamp=datetime(1962, 1, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1962, 2, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1962, 3, 1, tzinfo=timezone.utc), value=0),
        Point(timestamp=datetime(1962, 4, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1962, 5, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1962, 6, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1962, 7, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1962, 8, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1962, 9, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1962, 10, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1962, 11, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1962, 12, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1963, 1, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1963, 2, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1963, 3, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1963, 4, 1, tzinfo=timezone.utc), value=0),
        Point(timestamp=datetime(1963, 5, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1963, 6, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1963, 7, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1963, 8, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1963, 9, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1963, 10, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1963, 11, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1963, 12, 1, tzinfo=timezone.utc), value=1),
    ]

    data = Request(series=series, granularity=Granularity.monthly, sensitivity=95, max_anomaly_ratio=0.25)
    response = client.entire_detect(data)
    if True in response.is_anomaly:
        print('Anomaly was detected from the series at index:')
        for i in range(len(series)):
            if response.is_anomaly[i]:
                print(i)
    else:
        print('There is no anomaly detected from the series.')


def last_detect_sample(endpoint, key):
    print('Sample of detecting whether the latest point in series is anomaly.')

    client = AnomalyDetectorClient(endpoint, CognitiveServicesCredentials(key))

    series = [
        Point(timestamp=datetime(1962, 1, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1962, 2, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1962, 3, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1962, 4, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1962, 5, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1962, 6, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1962, 7, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1962, 8, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1962, 9, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1962, 10, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1962, 11, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1962, 12, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1963, 1, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1963, 2, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1963, 3, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1963, 4, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1963, 5, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1963, 6, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1963, 7, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1963, 8, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1963, 9, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1963, 10, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1963, 11, 1, tzinfo=timezone.utc), value=1),
        Point(timestamp=datetime(1963, 12, 1, tzinfo=timezone.utc), value=0),
    ]

    data = Request(series=series, granularity=Granularity.monthly, sensitivity=95, max_anomaly_ratio=0.25)
    response = client.last_detect(data)
    if response.is_anomaly:
        print('The latest point is detected as anomaly.')
    else:
        print('The latest point is not detected as anomaly.')


if __name__ == "__main__":
    endpoint = '[YOUR_ENDPOINT_URL]'
    key = '[YOUR_SUBSCRIPTION_KEY]'

    try:
        entire_dectect_sample(endpoint, key)
        last_detect_sample(endpoint, key)
    except Exception as e:
        if isinstance(e, APIErrorException):
            print('Error code: {}'.format(e.error.code))
            print('Error message: {}'.format(e.error.message))
        else:
            print(e)
