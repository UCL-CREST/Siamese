    public static String scramble(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer();
            for (byte b : md.digest()) sb.append(Integer.toString(b & 0xFF, 16));
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            return null;
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
