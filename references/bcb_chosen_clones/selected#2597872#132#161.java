    protected byte[] getTSAResponse(byte[] requestBytes) throws Exception {
        URL url = new URL(tsaURL);
        URLConnection tsaConnection;
        tsaConnection = (URLConnection) url.openConnection();
        tsaConnection.setDoInput(true);
        tsaConnection.setDoOutput(true);
        tsaConnection.setUseCaches(false);
        tsaConnection.setRequestProperty("Content-Type", "application/timestamp-query");
        tsaConnection.setRequestProperty("Content-Transfer-Encoding", "binary");
        if ((tsaUsername != null) && !tsaUsername.equals("")) {
            String userPassword = tsaUsername + ":" + tsaPassword;
            tsaConnection.setRequestProperty("Authorization", "Basic " + Base64.encodeBytes(userPassword.getBytes()));
        }
        OutputStream out = tsaConnection.getOutputStream();
        out.write(requestBytes);
        out.close();
        InputStream inp = tsaConnection.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = inp.read(buffer, 0, buffer.length)) >= 0) {
            baos.write(buffer, 0, bytesRead);
        }
        byte[] respBytes = baos.toByteArray();
        String encoding = tsaConnection.getContentEncoding();
        if (encoding != null && encoding.equalsIgnoreCase("base64")) {
            respBytes = Base64.decode(new String(respBytes));
        }
        return respBytes;
    }
