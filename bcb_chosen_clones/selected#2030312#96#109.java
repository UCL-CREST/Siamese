    public static String getResourceFromURL(URL url, String acceptHeader) throws java.io.IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setUseCaches(false);
        urlConnection.setRequestProperty("Accept", acceptHeader);
        urlConnection.setInstanceFollowRedirects(true);
        BufferedReader input = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String content = "";
        String line;
        while ((line = input.readLine()) != null) {
            content += line;
        }
        input.close();
        return content;
    }
