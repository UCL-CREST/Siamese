    public List<Mosque> getAllMosquaisFromDataBase() {
        List<Mosque> mosquais = new ArrayList<Mosque>();
        InputStream is = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if (MyMapActivity.DEVELOPER_MODE) {
            nameValuePairs.add(new BasicNameValuePair(Param.LATITUDE, MyMapActivity.mLatitude + ""));
            nameValuePairs.add(new BasicNameValuePair(Param.LONGITUDE, MyMapActivity.mLongitude + ""));
        } else {
            nameValuePairs.add(new BasicNameValuePair(Param.LATITUDE, MyMapActivity.myLocation.getLatitude() + ""));
            nameValuePairs.add(new BasicNameValuePair(Param.LONGITUDE, MyMapActivity.myLocation.getLongitude() + ""));
        }
        nameValuePairs.add(new BasicNameValuePair(Param.RAYON, DataBaseQuery.rayon * Param.KM_MARGE + ""));
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Param.URI_SELECT_ALL_DATA_BASE);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                Mosque mosquai = new Mosque(json_data.getInt(Param.ID), json_data.getString(Param.NOM), json_data.getDouble(Param.LATITUDE), json_data.getDouble(Param.LONGITUDE), json_data.getString(Param.INFO), json_data.getInt(Param.HAVE_PICTURE) == 1 ? true : false, json_data.getString(Param.PICTURE));
                mosquais.add(mosquai);
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return mosquais;
    }
