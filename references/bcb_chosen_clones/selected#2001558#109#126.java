    private String calculateCredential(Account account) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        try {
            md5.update(account.getUsername().getBytes("UTF-8"));
            md5.update(account.getCryptPassword().getBytes("UTF-8"));
            md5.update(String.valueOf(account.getObjectId()).getBytes("UTF-8"));
            md5.update(account.getUid().getBytes("UTF-8"));
            byte[] digest = md5.digest();
            return TextUtils.calculateMD5(digest);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
