    private String getData(String method, String arg) {
        try {
            URL url;
            String str;
            StringBuilder strBuilder;
            BufferedReader stream;
            url = new URL(API_BASE_URL + "/2.1/" + method + "/en/xml/" + API_KEY + "/" + URLEncoder.encode(arg, "UTF-8"));
            stream = new BufferedReader(new InputStreamReader(url.openStream()));
            strBuilder = new StringBuilder();
            while ((str = stream.readLine()) != null) {
                strBuilder.append(str);
            }
            stream.close();
            return strBuilder.toString();
        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }
