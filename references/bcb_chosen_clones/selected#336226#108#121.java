    private boolean verifyPassword(String password, byte[] hash) {
        boolean returnValue = false;
        try {
            MessageDigest msgDigest = MessageDigest.getInstance("SHA-1");
            msgDigest.update(password.getBytes("UTF-8"));
            byte[] digest = msgDigest.digest();
            returnValue = Arrays.equals(hash, digest);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(AuthentificationState.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AuthentificationState.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnValue;
    }
