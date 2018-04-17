    private String encryptPassword(String password) {
        String result = password;
        if (password != null) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.reset();
                md5.update(password.getBytes());
                BigInteger hash = new BigInteger(1, md5.digest());
                result = hash.toString(16);
                if ((result.length() % 2) != 0) {
                    result = "0" + result;
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                getLogger().error("Cannot generate MD5", e);
            }
        }
        return result;
    }
