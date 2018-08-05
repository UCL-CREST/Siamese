    protected static String encodePassword(String raw_password) throws DatabaseException {
        String clean_password = validatePassword(raw_password);
        try {
            MessageDigest md = MessageDigest.getInstance(DEFAULT_PASSWORD_DIGEST);
            md.update(clean_password.getBytes(DEFAULT_PASSWORD_ENCODING));
            String digest = new String(Base64.encodeBase64(md.digest()));
            if (log.isDebugEnabled()) log.debug("encodePassword: digest=" + digest);
            return digest;
        } catch (UnsupportedEncodingException e) {
            throw new DatabaseException("encoding-problem with password", e);
        } catch (NoSuchAlgorithmException e) {
            throw new DatabaseException("digest-problem encoding password", e);
        }
    }
