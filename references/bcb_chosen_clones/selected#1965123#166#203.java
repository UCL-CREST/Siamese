            @Override
            public void onClick(View v) {
                Log.d(Config.SS_TAG, "Sending POST request to server...");
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Config.RPC_SERVLET_URL);
                JSONObject requestJson = new JSONObject();
                JSONArray callsJson = new JSONArray();
                try {
                    JSONObject callJson = new JSONObject();
                    callJson.put("method", "ping");
                    callJson.put("void", "null");
                    callsJson.put(0, callJson);
                    requestJson.put("calls", callsJson);
                    httpPost.setEntity(new StringEntity(requestJson.toString(), "UTF-8"));
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    final int responseStatusCode = httpResponse.getStatusLine().getStatusCode();
                    if (200 <= responseStatusCode && responseStatusCode < 300) {
                        Log.d(Config.SS_TAG, "Successful ping - status code: " + responseStatusCode);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"), 8 * 1024);
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line).append("\n");
                        }
                        JSONTokener tokener = new JSONTokener(sb.toString());
                        JSONObject responseJson = new JSONObject(tokener);
                        JSONArray resultsJson = responseJson.getJSONArray("results");
                        JSONObject result = resultsJson.getJSONObject(0);
                        String returnValue = result.getJSONObject("data").getString("return");
                        Log.d(Config.SS_TAG, "Response message: " + returnValue);
                    } else {
                        Log.e(Config.SS_TAG, "Unsuccessful ping...");
                    }
                } catch (Exception e) {
                    Log.e(Config.SS_TAG, "Error while trying to ping rpc servlet");
                    e.printStackTrace();
                }
            }
