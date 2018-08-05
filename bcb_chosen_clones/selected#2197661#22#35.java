    static final String md5(String text) throws RtmApiException {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] md5hash = new byte[32];
            md.update(text.getBytes("UTF-8"), 0, text.length());
            md5hash = md.digest();
            return convertToHex(md5hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RtmApiException("Md5 error: NoSuchAlgorithmException - " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new RtmApiException("Md5 error: UnsupportedEncodingException - " + e.getMessage());
        }
    }
