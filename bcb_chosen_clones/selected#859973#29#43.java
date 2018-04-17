    @Override
    public boolean isPasswordValid(String encPass, String rawPass, Object salt) throws DataAccessException {
        try {
            MessageDigest digest = MessageDigest.getInstance(digestAlgorithm);
            digest.reset();
            digest.update(((String) salt).getBytes("UTF-8"));
            String encodedRawPass = new String(digest.digest(rawPass.getBytes("UTF-8")));
            return encodedRawPass.equals(encPass);
        } catch (Throwable e) {
            throw new DataAccessException("Error al codificar la contrase�a", e) {

                private static final long serialVersionUID = -302443565702455874L;
            };
        }
    }
