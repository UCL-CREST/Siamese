    public boolean isPasswordCorrect(String attempt) {
        try {
            MessageDigest digest = MessageDigest.getInstance(attempt);
            digest.update(salt);
            digest.update(attempt.getBytes("UTF-8"));
            byte[] attemptHash = digest.digest();
            return attemptHash.equals(hash);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(UserRecord.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserRecord.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
