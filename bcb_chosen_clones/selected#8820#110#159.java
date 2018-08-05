    private Tuple execute(final HttpMethodBase method, int numTries) throws IOException {
        final Timer timer = Metric.newTimer("RestClientImpl.execute");
        try {
            final int sc = httpClient.executeMethod(method);
            if (sc < OK_MIN || sc > OK_MAX) {
                throw new RestException("Unexpected status code: " + sc + ": " + method.getStatusText() + " -- " + method, sc);
            }
            final InputStream in = method.getResponseBodyAsStream();
            try {
                final StringWriter writer = new StringWriter(2048);
                IOUtils.copy(in, writer, method.getResponseCharSet());
                return new Tuple(sc, writer.toString());
            } finally {
                in.close();
            }
        } catch (NullPointerException e) {
            if (numTries < 3) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ie) {
                    Thread.interrupted();
                }
                return execute(method, numTries + 1);
            }
            throw new IOException("Failed to connet to " + url + " [" + method + "]", e);
        } catch (SocketException e) {
            if (numTries < 3) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ie) {
                    Thread.interrupted();
                }
                return execute(method, numTries + 1);
            }
            throw new IOException("Failed to connet to " + url + " [" + method + "]", e);
        } catch (IOException e) {
            if (numTries < 3) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ie) {
                    Thread.interrupted();
                }
                return execute(method, numTries + 1);
            }
            throw e;
        } finally {
            method.releaseConnection();
            timer.stop();
        }
    }
