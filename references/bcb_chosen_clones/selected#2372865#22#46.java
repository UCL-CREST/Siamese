    public static String sendSoapMsg(String SOAPUrl, byte[] b, String SOAPAction) throws IOException {
        log.finest("HTTP REQUEST SIZE " + b.length);
        if (SOAPAction.startsWith("\"") == false) SOAPAction = "\"" + SOAPAction + "\"";
        URL url = new URL(SOAPUrl);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestProperty("SOAPAction", SOAPAction);
        httpConn.setRequestProperty("Content-Type", "text/xml; charset=\"utf-8\"");
        httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
        httpConn.setRequestProperty("Cache-Control", "no-cache");
        httpConn.setRequestProperty("Pragma", "no-cache");
        httpConn.setRequestMethod("POST");
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        OutputStream out = httpConn.getOutputStream();
        out.write(b);
        out.close();
        InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
        BufferedReader in = new BufferedReader(isr);
        StringBuffer response = new StringBuffer(1024);
        String inputLine;
        while ((inputLine = in.readLine()) != null) response.append(inputLine);
        in.close();
        log.finest("HTTP RESPONSE SIZE: " + response.length());
        return response.toString();
    }
