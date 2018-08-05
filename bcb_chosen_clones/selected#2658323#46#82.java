    static JSONObject executeMethod(HttpClient httpClient, HttpMethod method, int timeout) throws HttpRequestFailureException, HttpException, IOException, HttpRequestTimeoutException {
        try {
            method.getParams().setSoTimeout(timeout * 1000);
            int status = -1;
            JSONObject result = null;
            for (int i = 0; i < RETRY; i++) {
                System.out.println("Execute method[" + method.getURI() + "](try " + (i + 1) + ")");
                status = httpClient.executeMethod(method);
                if (status == HttpStatus.SC_OK) {
                    InputStream inputStream = method.getResponseBodyAsStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    IOUtils.copy(inputStream, baos);
                    String response = new String(baos.toByteArray(), "UTF-8");
                    System.out.println(response);
                    result = JSONObject.fromString(response);
                    if (result.has("status")) {
                        String lingrStatus = result.getString("status");
                        if ("ok".equals(lingrStatus)) {
                            break;
                        } else {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                } else {
                    throw new HttpRequestFailureException(status);
                }
            }
            return result;
        } catch (SocketTimeoutException e) {
            throw new HttpRequestTimeoutException(e);
        } finally {
            method.releaseConnection();
        }
    }
