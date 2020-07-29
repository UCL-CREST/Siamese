    protected String decrypt(final String data, final String key) throws CryptographicFailureException {
        Validate.notNull(data, "Provided data cannot be null.");
        Validate.notNull(key, "Provided key name cannot be null.");
        final PrivateKey pk = getPrivateKey(key);
        if (pk == null) {
            throw new CryptographicFailureException("PrivateKeyNotFound", String.format("Cannot find private key '%s'", key));
        }
        try {
            final Cipher cipher = Cipher.getInstance(pk.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, pk);
            final ByteArrayInputStream bin = new ByteArrayInputStream(Base64.decodeBase64(data.getBytes()));
            final CipherInputStream cin = new CipherInputStream(bin, cipher);
            final ByteArrayOutputStream bout = new ByteArrayOutputStream();
            IOUtils.copy(cin, bout);
            return new String(bout.toByteArray());
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
