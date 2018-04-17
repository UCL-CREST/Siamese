    public static String generateStringSHA256(String content) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ScannerChecksum.class.getName()).log(Level.SEVERE, null, ex);
        }
        md.update(content.getBytes());
        byte byteData[] = md.digest();
        @SuppressWarnings("StringBufferMayBeStringBuilder") StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        @SuppressWarnings("StringBufferMayBeStringBuilder") StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
