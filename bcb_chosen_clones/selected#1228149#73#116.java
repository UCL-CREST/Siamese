    public String digest(String password, String digestType, String inputEncoding) throws CmsPasswordEncryptionException {
        MessageDigest md;
        String result;
        try {
            if (DIGEST_TYPE_PLAIN.equals(digestType.toLowerCase())) {
                result = password;
            } else if (DIGEST_TYPE_SSHA.equals(digestType.toLowerCase())) {
                byte[] salt = new byte[4];
                byte[] digest;
                byte[] total;
                if (m_secureRandom == null) {
                    m_secureRandom = SecureRandom.getInstance("SHA1PRNG");
                }
                m_secureRandom.nextBytes(salt);
                md = MessageDigest.getInstance(DIGEST_TYPE_SHA);
                md.reset();
                md.update(password.getBytes(inputEncoding));
                md.update(salt);
                digest = md.digest();
                total = new byte[digest.length + salt.length];
                System.arraycopy(digest, 0, total, 0, digest.length);
                System.arraycopy(salt, 0, total, digest.length, salt.length);
                result = new String(Base64.encodeBase64(total));
            } else {
                md = MessageDigest.getInstance(digestType);
                md.reset();
                md.update(password.getBytes(inputEncoding));
                result = new String(Base64.encodeBase64(md.digest()));
            }
        } catch (NoSuchAlgorithmException e) {
            CmsMessageContainer message = Messages.get().container(Messages.ERR_UNSUPPORTED_ALGORITHM_1, digestType);
            if (LOG.isErrorEnabled()) {
                LOG.error(message.key(), e);
            }
            throw new CmsPasswordEncryptionException(message, e);
        } catch (UnsupportedEncodingException e) {
            CmsMessageContainer message = Messages.get().container(Messages.ERR_UNSUPPORTED_PASSWORD_ENCODING_1, inputEncoding);
            if (LOG.isErrorEnabled()) {
                LOG.error(message.key(), e);
            }
            throw new CmsPasswordEncryptionException(message, e);
        }
        return result;
    }
