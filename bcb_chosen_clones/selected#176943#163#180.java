    protected InputStream callApiMethod(String apiUrl, int expected) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            for (String headerName : requestHeaders.keySet()) {
                request.setRequestProperty(headerName, requestHeaders.get(headerName));
            }
            request.connect();
            if (request.getResponseCode() != expected) {
                Error error = readResponse(Error.class, getWrappedInputStream(request.getErrorStream(), GZIP_ENCODING.equalsIgnoreCase(request.getContentEncoding())));
                throw createBingSearchApiClientException(error);
            } else {
                return getWrappedInputStream(request.getInputStream(), GZIP_ENCODING.equalsIgnoreCase(request.getContentEncoding()));
            }
        } catch (IOException e) {
            throw new BingSearchException(e);
        }
    }
