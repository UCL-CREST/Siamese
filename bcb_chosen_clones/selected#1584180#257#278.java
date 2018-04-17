    public void testCryptHash() {
        Log.v("Test", "[*] testCryptHash()");
        String testStr = "Hash me";
        byte messageDigest[];
        MessageDigest digest = null;
        try {
            digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(testStr.getBytes());
            messageDigest = digest.digest();
            digest.digest(testStr.getBytes());
            digest = java.security.MessageDigest.getInstance("SHA1");
            digest.update(testStr.getBytes());
            messageDigest = digest.digest();
            digest = null;
            digest = java.security.MessageDigest.getInstance("SHA1");
            digest.update(imei.getBytes());
            messageDigest = digest.digest();
            hashedImei = this.toHex(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
