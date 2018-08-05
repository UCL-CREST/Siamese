    private void prepareDigestFromTextArea() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        log.println("\nCalculating digest ...\n");
        java.security.MessageDigest md5 = java.security.MessageDigest.getInstance("MD5");
        md5.update(dataArea.getText().getBytes("UTF8"));
        byte[] digest = md5.digest();
        log.println("digest:\n" + formatAsHexString(digest));
        log.println("Done.");
        setEncodedDigest(encodeFromBytes(digest));
    }
