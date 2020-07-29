    public byte[] authClient(String host, String user, String passwd, String realm, String serverChallenge) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStream b64os = new BASE64EncoderStream(bos, Integer.MAX_VALUE);
        SecureRandom random;
        try {
            random = new SecureRandom();
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            if (debugout != null) debugout.println("DEBUG DIGEST-MD5: " + ex);
            throw new IOException(ex.toString());
        }
        StringBuffer result = new StringBuffer();
        uri = "smtp/" + host;
        String nc = "00000001";
        String qop = "auth";
        byte[] bytes = new byte[32];
        int resp;
        if (debugout != null) debugout.println("DEBUG DIGEST-MD5: Begin authentication ...");
        Hashtable map = tokenize(serverChallenge);
        if (realm == null) {
            String text = (String) map.get("realm");
            realm = text != null ? new StringTokenizer(text, ",").nextToken() : host;
        }
        String nonce = (String) map.get("nonce");
        random.nextBytes(bytes);
        b64os.write(bytes);
        b64os.flush();
        String cnonce = bos.toString();
        bos.reset();
        md5.update(md5.digest(ASCIIUtility.getBytes(user + ":" + realm + ":" + passwd)));
        md5.update(ASCIIUtility.getBytes(":" + nonce + ":" + cnonce));
        clientResponse = toHex(md5.digest()) + ":" + nonce + ":" + nc + ":" + cnonce + ":" + qop + ":";
        md5.update(ASCIIUtility.getBytes("AUTHENTICATE:" + uri));
        md5.update(ASCIIUtility.getBytes(clientResponse + toHex(md5.digest())));
        result.append("username=\"" + user + "\"");
        result.append(",realm=\"" + realm + "\"");
        result.append(",qop=" + qop);
        result.append(",nc=" + nc);
        result.append(",nonce=\"" + nonce + "\"");
        result.append(",cnonce=\"" + cnonce + "\"");
        result.append(",digest-uri=\"" + uri + "\"");
        result.append(",response=" + toHex(md5.digest()));
        if (debugout != null) debugout.println("DEBUG DIGEST-MD5: Response => " + result.toString());
        b64os.write(ASCIIUtility.getBytes(result.toString()));
        b64os.flush();
        return bos.toByteArray();
    }
