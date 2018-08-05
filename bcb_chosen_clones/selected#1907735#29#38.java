    public int doCheck(URL url) throws IOException {
        long start = (System.currentTimeMillis());
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
        }
        in.close();
        long end = (System.currentTimeMillis());
        return (int) (end - start);
    }
