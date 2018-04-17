    private String getLatestVersion(URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(con.getInputStream())));
        String lines = "";
        String line = null;
        while ((line = br.readLine()) != null) {
            lines += line;
        }
        con.disconnect();
        return lines;
    }
