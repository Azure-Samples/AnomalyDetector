using System;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;

namespace AnomalyDetection
{
    public class AnomalyDetectionClient
    {

        public async Task<string> Request(string baseAddress, string endpoint, string subscriptionKey, string requestData)
        {
            using (HttpClient client = new HttpClient { BaseAddress = new Uri(baseAddress) })
            {
                System.Net.ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls12 | SecurityProtocolType.Tls11 | SecurityProtocolType.Tls;
                client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
                client.DefaultRequestHeaders.Add("Ocp-Apim-Subscription-Key", subscriptionKey);

                var content = new StringContent(requestData, Encoding.UTF8, "application/json");
                var res = await client.PostAsync(endpoint, content);
                var responseText = await res.Content.ReadAsStringAsync();
                if (res.IsSuccessStatusCode)
                {
                    return responseText;
                }
                else
                {
                    throw new HttpRequestException($"The server returned {res.StatusCode} - {res.StatusCode.ToString()}\n: { responseText }");
                }
            }
        }
    }
}
