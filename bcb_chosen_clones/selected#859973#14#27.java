    @Override
    public String encodePassword(String rawPass, Object salt) throws DataAccessException {
        try {
            MessageDigest digest = MessageDigest.getInstance(digestAlgorithm);
            digest.reset();
            digest.update(((String) salt).getBytes("UTF-8"));
            return new String(digest.digest(rawPass.getBytes("UTF-8")));
        } catch (Throwable e) {
            throw new DataAccessException("Error al codificar la contrase�a", e) {

                private static final long serialVersionUID = 3880106673612870103L;
            };
        }
    }
