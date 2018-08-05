    public byte[] getDigest(OMProcessingInstruction pi, String digestAlgorithm) throws OMException {
        byte[] digest = new byte[0];
        try {
            MessageDigest md = MessageDigest.getInstance(digestAlgorithm);
            md.update((byte) 0);
            md.update((byte) 0);
            md.update((byte) 0);
            md.update((byte) 7);
            md.update(pi.getTarget().getBytes("UnicodeBigUnmarked"));
            md.update((byte) 0);
            md.update((byte) 0);
            md.update(pi.getValue().getBytes("UnicodeBigUnmarked"));
            digest = md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new OMException(e);
        } catch (UnsupportedEncodingException e) {
            throw new OMException(e);
        }
        return digest;
    }
