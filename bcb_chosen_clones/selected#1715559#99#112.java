    private String sendMessage(HttpURLConnection connection, String reqMessage) throws IOException {
        if (msgLog.isTraceEnabled()) msgLog.trace("Outgoing SOAPMessage\n" + reqMessage);
        BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream());
        out.write(reqMessage.getBytes("UTF-8"));
        out.close();
        InputStream inputStream = null;
        if (connection.getResponseCode() < 400) inputStream = connection.getInputStream(); else inputStream = connection.getErrorStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        IOUtils.copyStream(baos, inputStream);
        inputStream.close();
        String response = new String(baos.toByteArray(), "UTF-8");
        if (msgLog.isTraceEnabled()) msgLog.trace("Incoming Response SOAPMessage\n" + response);
        return response;
    }
