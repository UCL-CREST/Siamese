    private InputStream urlToInputStream(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("User-Agent", IE);
        conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
        conn.connect();
        String encoding = conn.getContentEncoding();
        if ((encoding != null) && encoding.equalsIgnoreCase("gzip")) return new GZIPInputStream(conn.getInputStream()); else if ((encoding != null) && encoding.equalsIgnoreCase("deflate")) return new InflaterInputStream(conn.getInputStream(), new Inflater(true)); else return conn.getInputStream();
    }
