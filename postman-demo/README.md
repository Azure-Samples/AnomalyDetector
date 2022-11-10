# How to run MVAD(v1.1) in postman

### API Overview
There are 7 APIs provided in Multivariate Anomaly Deteciton:
* **Training**: Use `Train Model API` to create and train a model, then use `Get Model Status API` to get the status and model metadata.
* **Inference**: 
    * Use `Async Inference API` to trigger an asynchronous inference process and use `Get Inference results API` to get detection results on a batch of data.
    * You could also use `Sync Inference API` to trigger a detection on one timestamp every time.
* **Other operations**: `List Model API` and `Delete Model API` are supported in MVAD for model management.
![](https://i.imgur.com/PQ7h6Ix.png)



|API Name| Method | Path  | Description |
| ------ | ---- | ----------- | ------ | 
|**Train Model**| POST       |  `{endpoint}`/anomalydetector/v1.1/multivariate/models    |   Create and train a model          |
|**Get Model Status**|     GET   |   `{endpoint}`anomalydetector/v1.1/multivariate/models/`{modelId}`   |     Get model status and model metadata with `modelId`   |
|**Async Inference**|    POST    |  `{endpoint}`/anomalydetector/v1.1/multivariate/models/`{modelId}`:detect-batch    |          Trigger an asynchronous inference with `modelId`   |
|**Get Inference Results**|     GET   |  `{endpoint}`/anomalydetector/v1.1/multivariate/detect-batch/`{resultId}`    |       Get asynchronous inference resulsts with `resultId`      |
|**Sync Inference**|   POST     |   `{endpoint}`/anomalydetector/v1.1/multivariate/models/`{modelId}`:detect-last   |      Trigger a synchronous inference with `modelId`       |
|**List Model**|     GET   |  `{endpoint}`/anomalydetector/v1.1/multivariate/models    |      List all models       |
|**Delete Model**|     DELET   |  `{endpoint}`/anomalydetector/v1.1/multivariate/models/`{modelId}`    |     Delete model with `modelId`       |

Please click this button to fork the API collection:
[![Fork in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/18763802-b90da6d8-0f98-4200-976f-546342abcade?action=collection%2Ffork&collection-url=entityId%3D18763802-b90da6d8-0f98-4200-976f-546342abcade%26entityType%3Dcollection%26workspaceId%3De1370b45-5076-4885-884f-e9a97136ddbc#?env%5BMVAD%5D=W3sia2V5IjoibW9kZWxJZCIsInZhbHVlIjoiIiwiZW5hYmxlZCI6dHJ1ZSwidHlwZSI6ImRlZmF1bHQiLCJzZXNzaW9uVmFsdWUiOiJlNjQxZTJlYy01Mzg5LTExZWQtYTkyMC01MjcyNGM4YTZkZmEiLCJzZXNzaW9uSW5kZXgiOjB9LHsia2V5IjoicmVzdWx0SWQiLCJ2YWx1ZSI6IiIsImVuYWJsZWQiOnRydWUsInR5cGUiOiJkZWZhdWx0Iiwic2Vzc2lvblZhbHVlIjoiOGZkZTAwNDItNTM4YS0xMWVkLTlhNDEtMGUxMGNkOTEwZmZhIiwic2Vzc2lvbkluZGV4IjoxfSx7ImtleSI6Ik9jcC1BcGltLVN1YnNjcmlwdGlvbi1LZXkiLCJ2YWx1ZSI6IiIsImVuYWJsZWQiOnRydWUsInR5cGUiOiJzZWNyZXQiLCJzZXNzaW9uVmFsdWUiOiJjNzNjMGRhMzlhOTA0MjgzODA4ZjBmY2E0Zjc3MTFkOCIsInNlc3Npb25JbmRleCI6Mn0seyJrZXkiOiJlbmRwb2ludCIsInZhbHVlIjoiIiwiZW5hYmxlZCI6dHJ1ZSwidHlwZSI6ImRlZmF1bHQiLCJzZXNzaW9uVmFsdWUiOiJodHRwczovL211bHRpLWFkLXRlc3QtdXNjeC5jb2duaXRpdmVzZXJ2aWNlcy5henVyZS5jb20vIiwic2Vzc2lvbkluZGV4IjozfSx7ImtleSI6ImRhdGFTb3VyY2UiLCJ2YWx1ZSI6IiIsImVuYWJsZWQiOnRydWUsInR5cGUiOiJkZWZhdWx0Iiwic2Vzc2lvblZhbHVlIjoiaHR0cHM6Ly9tdmFkZGF0YXNldC5ibG9iLmNvcmUud2luZG93cy5uZXQvc2FtcGxlLW9uZXRhYmxlL3NhbXBsZV9kYXRhXzVfMzAwMC5jc3YiLCJzZXNzaW9uSW5kZXgiOjR9XQ==)

1. Select environment as **MVAD**.
![](https://i.imgur.com/s4art6M.png)


1. Select **Environment**, paste your Anomaly Detector `endpoint`, `key` and dataSource `url` in to the **CURRENT VALUE** column, click **Save** to let the variables take effect.
![](https://i.imgur.com/BU00OGj.png)

2. Select **Collections**, and click on the first API - **Create and train a model**, then click **Send**. 

    ***Note:** If your data is one CSV file, please set the dataSchema as **OneTable**, if your data is multiple CSV files in a folder, please set the dataSchema as **MultiTable.***

    ![](https://i.imgur.com/vCNsvIg.png)

3. In the response of the first API, copy the modelId and paste it in the `modelId` in **Environments**, click **Save**. Then go to **Collections**, click on the second API - **Get model status**, and click **Send**.
![](https://i.imgur.com/tqUznIu.gif)

4. Select the third API - **Batch Detection**, and click **Send**. This API will trigger an asynchronous inference task, and you should use the Get batch detection results API several times to get the status and the final results.
![](https://i.imgur.com/OjaE6XK.png)

5. In the response of the third API, copy the resultId and paste it in the `resultId` in **Environments**, click **Save**. Then go to **Collections**, click on the fourth API - Get batch detection results, and click **Send**.
![](https://i.imgur.com/HKuJhSK.gif)

6. For the rest of the APIs, click on each and click Send to test on their request and response.
![](https://i.imgur.com/KeqnaL7.png)