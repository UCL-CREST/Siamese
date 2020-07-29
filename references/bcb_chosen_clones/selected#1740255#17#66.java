    public static void proxyRequest(IPageContext context, Writer writer, String proxyPath) throws IOException {
        URLConnection connection = new URL(proxyPath).openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(false);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
        connection.setReadTimeout(30000);
        connection.setConnectTimeout(5000);
        Enumeration<String> e = context.httpRequest().getHeaderNames();
        while (e.hasMoreElements()) {
            String name = e.nextElement();
            if (name.equalsIgnoreCase("HOST") || name.equalsIgnoreCase("Accept-Encoding") || name.equalsIgnoreCase("Authorization")) continue;
            Enumeration<String> headers = context.httpRequest().getHeaders(name);
            while (headers.hasMoreElements()) {
                String header = headers.nextElement();
                connection.setRequestProperty(name, header);
            }
        }
        if (connection instanceof HttpURLConnection) {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod(context.httpRequest().getMethod());
            if ("POST".equalsIgnoreCase(context.httpRequest().getMethod()) || "PUT".equalsIgnoreCase(context.httpRequest().getMethod())) {
                Enumeration<String> names = context.httpRequest().getParameterNames();
                StringBuilder body = new StringBuilder();
                while (names.hasMoreElements()) {
                    String key = names.nextElement();
                    for (String value : context.parameters(key)) {
                        if (body.length() > 0) {
                            body.append('&');
                        }
                        try {
                            body.append(key).append("=").append(URLEncoder.encode(value, "UTF-8"));
                        } catch (UnsupportedEncodingException ex) {
                        }
                    }
                }
                if (body.length() > 0) {
                    connection.setDoOutput(true);
                    OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                    out.write(body.toString());
                    out.close();
                }
            }
        }
        try {
            IOUtils.copy(connection.getInputStream(), writer);
        } catch (IOException ex) {
            writer.write("<span>SSI Error: " + ex.getMessage() + "</span>");
        }
    }
