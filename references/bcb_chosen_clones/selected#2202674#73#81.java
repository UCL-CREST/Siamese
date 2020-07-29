    public Epg unmarshallFromUrl(URL url) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String tmp = null;
        StringBuilder buffer = new StringBuilder();
        while ((tmp = reader.readLine()) != null) {
            buffer.append(tmp);
        }
        return unmarshall(buffer.toString().getBytes());
    }
