    public static String generate(String username, String password) throws PersistenceException {
        String output = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.reset();
            md.update(username.getBytes());
            md.update(password.getBytes());
            byte[] rawhash = md.digest();
            output = byteToBase64(rawhash);
        } catch (Exception e) {
            throw new PersistenceException("error, could not generate password");
        }
        return output;
    }
