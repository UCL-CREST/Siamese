    public static String hashString(String password) {
        String hashword = null;
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA");
            sha.update(password.getBytes("UTF-8"));
            BigInteger hash = new BigInteger(1, sha.digest());
            hashword = hash.toString(16);
        } catch (NoSuchAlgorithmException nsae) {
            log.error(nsae);
        } catch (UnsupportedEncodingException e) {
            log.error(e);
        }
        return pad(hashword, 32, '0');
    }
