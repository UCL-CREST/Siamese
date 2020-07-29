    public LinkedList<NameValuePair> getScoreboard() {
        InputStream is = null;
        String result = "";
        LinkedList<NameValuePair> scores = new LinkedList<NameValuePair>();
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(domain);
            httppost.setEntity(new UrlEncodedFormEntity(library));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + ",");
            }
            is.close();
            result = sb.toString();
            if (result.equals("null,")) {
                return null;
            }
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }
        try {
            JSONObject json = new JSONObject(result);
            JSONArray data = json.getJSONArray("data");
            JSONArray me = json.getJSONArray("me");
            for (int i = 0; i < data.length(); i++) {
                JSONObject single = data.getJSONObject(i);
                String uid = single.getString("uid");
                String score = single.getString("score");
                scores.add(new BasicNameValuePair(uid, score));
            }
            for (int i = 0; i < me.length(); i++) {
                JSONObject single = me.getJSONObject(i);
                String uid = single.getString("uid");
                String score = single.getString("score");
                scores.add(new BasicNameValuePair(uid, score));
            }
            System.out.println(json);
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return scores;
    }
