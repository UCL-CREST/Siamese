    @Override
    public final byte[] getDigest() {
        try {
            final MessageDigest hashing = MessageDigest.getInstance("SHA-256");
            final Charset utf16 = Charset.forName("UTF-16");
            for (final CollationKey wordKey : this.words) {
                hashing.update(wordKey.toByteArray());
            }
            hashing.update(this.locale.toString().getBytes(utf16));
            hashing.update(ByteUtils.toBytesLE(this.collator.getStrength()));
            hashing.update(ByteUtils.toBytesLE(this.collator.getDecomposition()));
            return hashing.digest();
        } catch (final NoSuchAlgorithmException e) {
            FileBasedDictionary.LOG.severe(e.toString());
            return new byte[0];
        }
    }
