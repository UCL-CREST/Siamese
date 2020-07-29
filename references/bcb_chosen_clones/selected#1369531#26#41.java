    protected String getPostRequestContent(String urlText, String postParam) throws Exception {
        URL url = new URL(urlText);
        HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
        urlcon.setRequestMethod("POST");
        urlcon.setUseCaches(false);
        urlcon.setDoOutput(true);
        PrintStream ps = new PrintStream(urlcon.getOutputStream());
        ps.print(postParam);
        ps.close();
        urlcon.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlcon.getInputStream()));
        String line = reader.readLine();
        reader.close();
        urlcon.disconnect();
        return line;
    }
