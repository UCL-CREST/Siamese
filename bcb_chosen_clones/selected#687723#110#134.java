    protected String encrypt(final String data, final String key) throws CryptographicFailureException {
        Validate.notNull(data, "Provided data cannot be null.");
        Validate.notNull(key, "Provided key name cannot be null.");
        final PublicKey pk = getPublicKey(key);
        if (pk == null) {
            throw new CryptographicFailureException("PublicKeyNotFound", String.format("Cannot find public key '%s'", key));
        }
        try {
            final Cipher cipher = Cipher.getInstance(pk.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, pk);
            final ByteArrayInputStream bin = new ByteArrayInputStream(data.getBytes());
            final CipherInputStream cin = new CipherInputStream(bin, cipher);
            final ByteArrayOutputStream bout = new ByteArrayOutputStream();
            IOUtils.copy(cin, bout);
            return new String(Base64.encodeBase64(bout.toByteArray()));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(String.format("Cannot find instance of algorithm '%s'", pk.getAlgorithm()), e);
        } catch (NoSuchPaddingException e) {
            throw new IllegalStateException(String.format("Cannot build instance of algorithm '%s'", pk.getAlgorithm()), e);
        } catch (InvalidKeyException e) {
            throw new IllegalStateException(String.format("Cannot build instance of algorithm '%s'", pk.getAlgorithm()), e);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot build in-memory cipher copy", e);
        }
    }
