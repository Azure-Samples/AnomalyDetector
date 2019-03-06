using System;
using System.Collections.Generic;

namespace AnomalyDetection
{
    public class Point
    {
        public DateTime Timestamp { get; set; }
        public double Value { get; set; }
    }

    public class Request
    {
        public List<Point> Series { get; set; }
        public float? MaxAnomalyRatio { get; set; }
        public int? Sensitivity { get; set; }
        public string Granularity { get; set; }

    }
}
