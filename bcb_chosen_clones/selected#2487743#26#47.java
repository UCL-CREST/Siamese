    public static String getMD5Hash(String original) {
        StringBuffer sb = new StringBuffer();
        try {
            StringReader sr = null;
            int crypt_byte = 0;
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(original.getBytes());
            byte[] digest = md.digest();
            sr = new StringReader(new String(digest, "ISO8859_1"));
            while ((crypt_byte = sr.read()) != -1) {
                String hexString = Integer.toHexString(crypt_byte);
                if (crypt_byte < 16) {
                    hexString = "0" + hexString;
                }
                sb.append(hexString);
            }
        } catch (NoSuchAlgorithmException nsae) {
        } catch (IOException ioe) {
        }
        return sb.toString();
    }
