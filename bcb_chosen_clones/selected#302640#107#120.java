    public static String MD5(String text) throws ProducteevSignatureException {
        try {
            MessageDigest md;
            md = MessageDigest.getInstance(ALGORITHM);
            byte[] md5hash;
            md.update(text.getBytes("utf-8"), 0, text.length());
            md5hash = md.digest();
            return convertToHex(md5hash);
        } catch (NoSuchAlgorithmException nsae) {
            throw new ProducteevSignatureException("No such algorithm : " + ALGORITHM, nsae);
        } catch (UnsupportedEncodingException e) {
            throw new ProducteevSignatureException("No such algorithm : " + ALGORITHM, e);
        }
    }
