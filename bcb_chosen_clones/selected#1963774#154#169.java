    private static String computeSHA(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(input.getBytes("UTF-8"));
            byte[] code = md.digest();
            return convertToHex(code);
        } catch (NoSuchAlgorithmException e) {
            log.error("Algorithm SHA-1 not found!", e);
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            log.error("Encoding problem: UTF-8 not supported!", e);
            e.printStackTrace();
            return null;
        }
    }
