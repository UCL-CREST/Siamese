    public static String getMessageDigest(String input) {
        if (input == null) {
            log.warn("Returning SHA-1 null value for null input");
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(input.getBytes("UTF-8"));
            byte[] bytes = md.digest();
            return new BASE64Encoder().encode(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
