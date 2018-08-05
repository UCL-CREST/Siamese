    public LinkedList<NameValuePair> getQuestion() {
        InputStream is = null;
        String result = "";
        LinkedList<NameValuePair> question = new LinkedList<NameValuePair>();
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
                sb.append(line);
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
            JSONObject quest = data.getJSONObject(0);
            question.add(new BasicNameValuePair("q", quest.getString("q")));
            question.add(new BasicNameValuePair("a", quest.getString("a")));
            question.add(new BasicNameValuePair("b", quest.getString("b")));
            question.add(new BasicNameValuePair("c", quest.getString("c")));
            question.add(new BasicNameValuePair("d", quest.getString("d")));
            question.add(new BasicNameValuePair("correct", quest.getString("correct")));
            return question;
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return null;
    }
