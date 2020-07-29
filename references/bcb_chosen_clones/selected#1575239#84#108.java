    void copyTo(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        if (shouldMock()) {
            return;
        }
        assert httpRequest != null;
        assert httpResponse != null;
        final long start = System.currentTimeMillis();
        try {
            final URLConnection connection = openConnection(url, headers);
            connection.setRequestProperty("Accept-Language", httpRequest.getHeader("Accept-Language"));
            connection.connect();
            try {
                InputStream input = connection.getInputStream();
                if ("gzip".equals(connection.getContentEncoding())) {
                    input = new GZIPInputStream(input);
                }
                httpResponse.setContentType(connection.getContentType());
                TransportFormat.pump(input, httpResponse.getOutputStream());
            } finally {
                close(connection);
            }
        } finally {
            LOGGER.info("http call done in " + (System.currentTimeMillis() - start) + " ms for " + url);
        }
    }
