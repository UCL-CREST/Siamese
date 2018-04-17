    private void calculateCoverageAndSpecificity(String mainCat) throws IOException, JSONException {
        for (String cat : Rules.categoryTree.get(mainCat)) {
            for (String queryString : Rules.queries.get(cat)) {
                String urlEncodedQueryString = URLEncoder.encode(queryString, "UTF-8");
                URL url = new URL("http://boss.yahooapis.com/ysearch/web/v1/" + urlEncodedQueryString + "?appid=" + yahoo_ap_id + "&count=4&format=json&sites=" + site);
                URLConnection con = url.openConnection();
                String line;
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                String response = builder.toString();
                JSONObject json = new JSONObject(response);
                JSONObject jsonObject = json.getJSONObject("ysearchresponse");
                String totalhits = jsonObject.getString("totalhits");
                long totalhitsLong = Long.parseLong(totalhits);
                QueryInfo qinfo = new QueryInfo(queryString, totalhitsLong);
                queryInfoMap.put(queryString, qinfo);
                cov.put(cat, cov.get(cat) + totalhitsLong);
                if (totalhitsLong == 0) {
                    continue;
                }
                ja = jsonObject.getJSONArray("resultset_web");
                for (int j = 0; j < ja.length(); j++) {
                    JSONObject k = ja.getJSONObject(j);
                    String dispurl = filterBold(k.getString("url"));
                    qinfo.addUrl(dispurl);
                }
            }
        }
        calculateSpecificity(mainCat);
    }
