using System.Collections.Generic;

namespace AnomalyDetection
{
    public class LResponse
    {
        public long SuggestedWindow { get; set; }
        public bool IsNegativeAnomaly { get; set; }
        public double LowerMargin { get; set; }
        public bool IsPositiveAnomaly { get; set; }
        public long Period { get; set; }
        public double ExpectedValue { get; set; }
        public bool IsAnomaly { get; set; }
        public double UpperMargin { get; set; }

    }

    public class EResponse
    {
        public List<bool> IsNegativeAnomaly { get; set; }
        public List<double> ExpectedValues { get; set; }
        public List<bool> IsPositiveAnomaly { get; set; }
        public List<double> LowerMargins { get; set; }
        public long Period { get; set; }
        public List<double> UpperMargins { get; set; }
        public List<bool> IsAnomaly { get; set; }

    }
}