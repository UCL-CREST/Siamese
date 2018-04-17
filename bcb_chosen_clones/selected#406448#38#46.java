    public String read(String url) throws IOException {
        URL myurl = new URL(url);
        BufferedReader in = new BufferedReader(new InputStreamReader(myurl.openStream()));
        StringBuffer sb = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null) sb.append(inputLine);
        in.close();
        return sb.toString();
    }
