    private static BufferedInputStream getHTTPConnection(String sUrl) {
        URL url = null;
        BufferedInputStream bis = null;
        try {
            url = new URL(sUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(60000);
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            connection.connect();
            String encoding = connection.getContentEncoding();
            if (!Utilities.isEmpty(encoding) && "gzip".equalsIgnoreCase(encoding)) {
                bis = new BufferedInputStream(new GZIPInputStream(connection.getInputStream()), IO_BUFFER_SIZE);
            } else if (!Utilities.isEmpty(encoding) && "deflate".equalsIgnoreCase(encoding)) {
                bis = new BufferedInputStream(new InflaterInputStream(connection.getInputStream(), new Inflater(true)), IO_BUFFER_SIZE);
            } else {
                bis = new BufferedInputStream(connection.getInputStream(), IO_BUFFER_SIZE);
            }
        } catch (Exception e) {
            LogUtil.e(Constants.TAG, e.getMessage());
        }
        return bis;
    }
