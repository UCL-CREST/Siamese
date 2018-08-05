    public boolean isPasswordValid(String encPass, String rawPass, Object salt) throws DataAccessException {
        boolean bMatch = false;
        try {
            String strSalt = (String) salt;
            byte[] baSalt = Hex.decodeHex(strSalt.toCharArray());
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.update(rawPass.getBytes(CHAR_ENCODING));
            md.update(baSalt);
            byte[] baCalculatedHash = md.digest();
            byte[] baStoredHash = Hex.decodeHex(encPass.toCharArray());
            bMatch = MessageDigest.isEqual(baCalculatedHash, baStoredHash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bMatch;
    }
