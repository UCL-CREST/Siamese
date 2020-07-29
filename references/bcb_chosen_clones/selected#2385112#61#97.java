    public static TestResponse get(String urlString, String accept) throws IOException {
        HttpURLConnection httpCon = null;
        byte[] result = null;
        byte[] errorResult = null;
        try {
            URL url = new URL(urlString);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setRequestMethod("GET");
            httpCon.setRequestProperty("Accept", accept);
            BufferedInputStream in = new BufferedInputStream(httpCon.getInputStream());
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int next = in.read();
            while (next > -1) {
                os.write(next);
                next = in.read();
            }
            os.flush();
            result = os.toByteArray();
            os.close();
        } catch (IOException e) {
        } finally {
            InputStream errorStream = httpCon.getErrorStream();
            if (errorStream != null) {
                BufferedInputStream errorIn = new BufferedInputStream(errorStream);
                ByteArrayOutputStream errorOs = new ByteArrayOutputStream();
                int errorNext = errorIn.read();
                while (errorNext > -1) {
                    errorOs.write(errorNext);
                    errorNext = errorIn.read();
                }
                errorOs.flush();
                errorResult = errorOs.toByteArray();
                errorOs.close();
            }
            return new TestResponse(httpCon.getResponseCode(), errorResult, result);
        }
    }
