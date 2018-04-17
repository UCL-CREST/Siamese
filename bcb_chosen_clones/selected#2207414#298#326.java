    private String postXml(String url, String soapAction, String xml) {
        try {
            URLConnection conn = new URL(url).openConnection();
            if (conn instanceof HttpURLConnection) {
                HttpURLConnection hConn = (HttpURLConnection) conn;
                hConn.setRequestMethod("POST");
            }
            conn.setConnectTimeout(this.connectionTimeout);
            conn.setReadTimeout(this.connectionTimeout);
            conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            conn.setRequestProperty("Accept", "application/soap+xml, text/*");
            if (soapAction != null) {
                conn.setRequestProperty("SOAPAction", soapAction);
            }
            conn.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(xml);
            out.close();
            BufferedReader resp = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder buf = new StringBuilder();
            String str;
            while ((str = resp.readLine()) != null) {
                buf.append(str);
            }
            return buf.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
