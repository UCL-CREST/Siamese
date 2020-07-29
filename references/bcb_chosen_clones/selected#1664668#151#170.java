    public byte[] getDigest(OMAttribute attribute, String digestAlgorithm) throws OMException {
        byte[] digest = new byte[0];
        if (!(attribute.getLocalName().equals("xmlns") || attribute.getLocalName().startsWith("xmlns:"))) try {
            MessageDigest md = MessageDigest.getInstance(digestAlgorithm);
            md.update((byte) 0);
            md.update((byte) 0);
            md.update((byte) 0);
            md.update((byte) 2);
            md.update(getExpandedName(attribute).getBytes("UnicodeBigUnmarked"));
            md.update((byte) 0);
            md.update((byte) 0);
            md.update(attribute.getAttributeValue().getBytes("UnicodeBigUnmarked"));
            digest = md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new OMException(e);
        } catch (UnsupportedEncodingException e) {
            throw new OMException(e);
        }
        return digest;
    }
