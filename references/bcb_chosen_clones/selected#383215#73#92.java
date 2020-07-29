    public static String getMd5Password(final String password) {
        String response = null;
        try {
            final MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(password.getBytes());
            final byte[] md5Byte = algorithm.digest();
            final StringBuffer buffer = new StringBuffer();
            for (final byte b : md5Byte) {
                if ((b <= 15) && (b >= 0)) {
                    buffer.append("0");
                }
                buffer.append(Integer.toHexString(0xFF & b));
            }
            response = buffer.toString();
        } catch (final NoSuchAlgorithmException e) {
            ProjektUtil.LOG.error("No digester MD5 found in classpath!", e);
        }
        return response;
    }
