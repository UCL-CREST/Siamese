    @Override
    protected byte[] computeHash() {
        try {
            final MessageDigest inputHash = MessageDigest.getInstance("SHA");
            inputHash.update(bufferFileData().getBytes());
            return inputHash.digest();
        } catch (final NoSuchAlgorithmException nsae) {
            lastException = nsae;
            return new byte[0];
        } catch (final IOException ioe) {
            lastException = ioe;
            return new byte[0];
        }
    }
