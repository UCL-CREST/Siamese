    @Override
    public void handle(HttpExchange http) throws IOException {
        Headers reqHeaders = http.getRequestHeaders();
        Headers respHeader = http.getResponseHeaders();
        respHeader.add("Content-Type", "text/plain");
        http.sendResponseHeaders(200, 0);
        PrintWriter console = new PrintWriter(System.err);
        PrintWriter web = new PrintWriter(http.getResponseBody());
        PrintWriter out = new PrintWriter(new YWriter(web, console));
        out.println("### " + new Date() + " ###");
        out.println("Method: " + http.getRequestMethod());
        out.println("Protocol: " + http.getProtocol());
        out.println("RemoteAddress.HostName: " + http.getRemoteAddress().getHostName());
        for (String key : reqHeaders.keySet()) {
            out.println("* \"" + key + "\"");
            for (String v : reqHeaders.get(key)) {
                out.println("\t" + v);
            }
        }
        InputStream in = http.getRequestBody();
        if (in != null) {
            out.println();
            IOUtils.copyTo(new InputStreamReader(in), out);
            in.close();
        }
        out.flush();
        out.close();
    }
