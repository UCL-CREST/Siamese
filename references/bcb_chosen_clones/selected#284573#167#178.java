    public static URL getAuthenticationURL(String apiKey, String permission, String sharedSecret) throws Exception {
        String apiSig = sharedSecret + "api_key" + apiKey + "perms" + permission;
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(apiSig.getBytes(), 0, apiSig.length());
        apiSig = new BigInteger(1, m.digest()).toString(16);
        StringBuffer buffer = new StringBuffer();
        buffer.append("http://flickr.com/services/auth/?");
        buffer.append("api_key=" + apiKey);
        buffer.append("&").append("perms=").append(permission);
        buffer.append("&").append("api_sig=").append(apiSig);
        return new URL(buffer.toString());
    }
