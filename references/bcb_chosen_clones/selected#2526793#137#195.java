    public static String getDigestResponse(String user, String password, String method, String requri, String authstr) {
        String realm = "";
        String nonce = "";
        String opaque = "";
        String algorithm = "";
        String qop = "";
        StringBuffer digest = new StringBuffer();
        String cnonce;
        String noncecount;
        String pAuthStr = authstr;
        int ptr = 0;
        String response = "";
        int i = 0;
        StringTokenizer st = new StringTokenizer(pAuthStr, ",");
        StringTokenizer stprob = null;
        String str = null;
        String key = null;
        String value = null;
        Properties probs = new Properties();
        while (st.hasMoreTokens()) {
            String nextToken = st.nextToken();
            stprob = new StringTokenizer(nextToken, "=");
            key = stprob.nextToken();
            value = stprob.nextToken();
            if (value.charAt(0) == '"' || value.charAt(0) == '\'') {
                value = value.substring(1, value.length() - 1);
            }
            probs.put(key, value);
        }
        digest.append("Digest username=\"" + user + "\", ");
        digest.append("realm=\"");
        digest.append(probs.getProperty("realm"));
        digest.append("\", ");
        digest.append("nonce=\"");
        digest.append(probs.getProperty("nonce"));
        digest.append("\", ");
        digest.append("uri=\"" + requri + "\", ");
        cnonce = "abcdefghi";
        noncecount = "00000001";
        String toDigest = user + ":" + realm + ":" + password;
        byte[] digestbuffer = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(toDigest.getBytes());
            digestbuffer = md.digest();
        } catch (Exception e) {
            System.err.println("Error creating digest request: " + e);
            return null;
        }
        digest.append("qop=\"auth\", ");
        digest.append("cnonce=\"" + cnonce + "\", ");
        digest.append("nc=" + noncecount + ", ");
        digest.append("response=\"" + response + "\"");
        if (probs.getProperty("opaque") != null) {
            digest.append(", opaque=\"" + probs.getProperty("opaque") + "\"");
        }
        System.out.println("SipProtocol: Digest calculated.");
        return digest.toString();
    }
