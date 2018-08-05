    public ArrayList loadIndexes() {
        JSONObject job = new JSONObject();
        ArrayList al = new ArrayList();
        try {
            String req = job.put("OperationId", "1").toString();
            InputStream is = null;
            String result = "";
            JSONObject jArray = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://192.168.1.4:8080/newgenlibctxt/CarbonServlet");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("OperationId", "1"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                JSONObject jobres = new JSONObject(result);
                JSONArray jarr = jobres.getJSONArray("MobileIndexes");
                for (int i = 0; i < jarr.length(); i++) {
                    String indexname = jarr.getString(i);
                    al.add(indexname);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return al;
    }
