    private void storeDBToServer(Hashtable data) throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(new GZIPOutputStream(bout));
        oout.writeObject(data);
        oout.flush();
        oout.close();
        byte[] encodedData = Base64.encodeBase64(bout.toByteArray());
        String base64String = new String(encodedData, "ASCII");
        base64String = base64String.replace('+', '-');
        base64String = base64String.replace('/', '_');
        base64String = base64String.replace('=', '.');
        String encodedString = base64String;
        URL url = new URL(Main.GAME_SERVER_URL + STORE_DB_SCRIPT);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setUseCaches(false);
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setDoInput(true);
        bout = new ByteArrayOutputStream();
        OutputStreamWriter textout = new OutputStreamWriter(bout, "ASCII");
        textout.write("password=" + Main.instance().getSecuritySubsystem().getPassword());
        textout.write("&serial=" + Main.instance().getClientSerNumber());
        textout.write("&data=");
        textout.write(encodedString);
        textout.flush();
        encodedData = bout.toByteArray();
        con.setRequestProperty("Content-Length", Integer.toString(encodedData.length));
        con.setFixedLengthStreamingMode(encodedData.length);
        con.getOutputStream().write(encodedData);
        getLogger().fine("Server response: " + con.getResponseMessage());
        CRC32 crc32 = new CRC32();
        crc32.update(encodedString.getBytes());
        long origincrc = crc32.getValue();
        long reccrc = 0;
        try {
            String response = IOUtils.toString(con.getInputStream());
            if (StringUtils.isBlank(response)) throw new Exception("No CRC response from server");
            response = response.replaceAll("[^0-9]", "");
            reccrc = Long.parseLong(response);
        } catch (NumberFormatException e) {
        }
        if (origincrc != reccrc) throw new Exception("CRCs are not equal ");
    }
