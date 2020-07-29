    protected String calcAuthResponse(String challenge) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(securityPolicy);
        md.update(challenge.getBytes());
        for (int i = 0, n = password.length; i < n; i++) {
            md.update((byte) password[i]);
        }
        byte[] digest = md.digest();
        StringBuffer digestText = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            int v = (digest[i] < 0) ? digest[i] + 256 : digest[i];
            String hex = Integer.toHexString(v);
            if (hex.length() == 1) {
                digestText.append("0");
            }
            digestText.append(hex);
        }
        return digestText.toString();
    }
