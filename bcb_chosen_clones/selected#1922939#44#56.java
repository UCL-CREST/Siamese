    protected String getRequestContent(String urlText, String method) throws Exception {
        URL url = new URL(urlText);
        HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
        urlcon.setRequestProperty("Referer", REFERER_STR);
        urlcon.setRequestMethod(method);
        urlcon.setUseCaches(false);
        urlcon.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlcon.getInputStream()));
        String line = reader.readLine();
        reader.close();
        urlcon.disconnect();
        return line;
    }
