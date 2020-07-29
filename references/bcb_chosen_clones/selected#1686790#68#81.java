    private byte[] getMD5(String string) throws IMException {
        byte[] buffer = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(string.getBytes("utf-8"));
            buffer = md.digest();
            buffer = getHexString(buffer);
        } catch (NoSuchAlgorithmException e) {
            throw new IMException(e);
        } catch (UnsupportedEncodingException ue) {
            throw new IMException(ue);
        }
        return buffer;
    }
