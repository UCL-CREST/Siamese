    public static String generatePassword(String userKey, int applicationId, String applicationKey) {
        String nonce = generateNonce();
        String createDate = fmtDate.format(new Date());
        String keyDigest = null;
        MessageDigest sha1 = null;
        try {
            sha1 = MessageDigest.getInstance("SHA1");
            sha1.update(nonce.getBytes("UTF-8"));
            sha1.update(createDate.getBytes("UTF-8"));
            sha1.update(userKey.getBytes("UTF-8"));
            sha1.update(applicationKey.getBytes("UTF-8"));
            keyDigest = getHexaDecimal(sha1.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(applicationId);
        sb.append(',');
        sb.append(nonce);
        sb.append(',');
        sb.append(createDate);
        sb.append(',');
        sb.append(keyDigest);
        return sb.toString();
    }
