    public Parameters getParameters(HttpExchange http) throws IOException {
        ParametersImpl params = new ParametersImpl();
        String query = null;
        if (http.getRequestMethod().equalsIgnoreCase("GET")) {
            query = http.getRequestURI().getRawQuery();
        } else if (http.getRequestMethod().equalsIgnoreCase("POST")) {
            InputStream in = new MaxInputStream(http.getRequestBody());
            if (in != null) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                IOUtils.copyTo(in, bytes);
                query = new String(bytes.toByteArray());
                in.close();
            }
        } else {
            throw new IOException("Method not supported " + http.getRequestMethod());
        }
        if (query != null) {
            for (String s : query.split("[&]")) {
                s = s.replace('+', ' ');
                int eq = s.indexOf('=');
                if (eq > 0) {
                    params.add(URLDecoder.decode(s.substring(0, eq), "UTF-8"), URLDecoder.decode(s.substring(eq + 1), "UTF-8"));
                }
            }
        }
        return params;
    }
