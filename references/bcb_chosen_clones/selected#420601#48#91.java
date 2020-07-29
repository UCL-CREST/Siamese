    public Vector Get() throws Exception {
        String query_str = BuildYahooQueryString();
        if (query_str == null) return null;
        Vector result = new Vector();
        HttpURLConnection urlc = null;
        try {
            URL url = new URL(URL_YAHOO_QUOTE + "?" + query_str + "&" + FORMAT);
            urlc = (HttpURLConnection) url.openConnection();
            urlc.setRequestMethod("GET");
            urlc.setDoOutput(true);
            urlc.setDoInput(true);
            urlc.setUseCaches(false);
            urlc.setAllowUserInteraction(false);
            urlc.setRequestProperty("Content-type", "text/html;charset=UTF-8");
            if (urlc.getResponseCode() == 200) {
                InputStream in = urlc.getInputStream();
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    String msg = null;
                    while ((msg = reader.readLine()) != null) {
                        ExchangeRate rate = ParseYahooData(msg);
                        if (rate != null) result.add(rate);
                    }
                } finally {
                    if (reader != null) try {
                        reader.close();
                    } catch (Exception e1) {
                    }
                    if (in != null) try {
                        in.close();
                    } catch (Exception e1) {
                    }
                }
                return result;
            }
        } finally {
            if (urlc != null) try {
                urlc.disconnect();
            } catch (Exception e) {
            }
        }
        return null;
    }
