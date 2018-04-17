    public String get(String url) {
        String buf = null;
        StringBuilder resultBuffer = new StringBuilder(512);
        if (debug.DEBUG) debug.logger("gov.llnl.tox.util.href", "get(url)>> " + url);
        try {
            URL theURL = new URL(url);
            URLConnection urlConn = theURL.openConnection();
            urlConn.setDoOutput(true);
            urlConn.setReadTimeout(timeOut);
            BufferedReader urlReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            do {
                buf = urlReader.readLine();
                if (buf != null) {
                    resultBuffer.append(buf);
                    resultBuffer.append("\n");
                }
            } while (buf != null);
            urlReader.close();
            if (debug.DEBUG) debug.logger("gov.llnl.tox.util.href", "get(output)>> " + resultBuffer.toString());
            int xmlNdx = resultBuffer.lastIndexOf("?>");
            if (xmlNdx == -1) result = resultBuffer.toString(); else result = resultBuffer.substring(xmlNdx + 2);
        } catch (Exception e) {
            result = debug.logger("gov.llnl.tox.util.href", "error: get >> ", e);
        }
        return (result);
    }
