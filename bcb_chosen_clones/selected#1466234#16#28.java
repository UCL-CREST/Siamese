    public static String encriptarContrasena(String contrasena) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(contrasena.getBytes("UTF-8"));
        byte[] digestBytes = md.digest();
        String hex = null;
        for (int i = 0; i < digestBytes.length; i++) {
            hex = Integer.toHexString(0xFF & digestBytes[i]);
            if (hex.length() < 2) sb.append("0");
            sb.append(hex);
        }
        return new String(sb);
    }
