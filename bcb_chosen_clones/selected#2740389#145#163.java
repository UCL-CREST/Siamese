    public static String generateHash(String string, String algoritmo) {
        try {
            MessageDigest md = MessageDigest.getInstance(algoritmo);
            md.update(string.getBytes());
            byte[] result = md.digest();
            int firstPart;
            int lastPart;
            StringBuilder sBuilder = new StringBuilder();
            for (int i = 0; i < result.length; i++) {
                firstPart = ((result[i] >> 4) & 0xf) << 4;
                lastPart = result[i] & 0xf;
                if (firstPart == 0) sBuilder.append("0");
                sBuilder.append(Integer.toHexString(firstPart | lastPart));
            }
            return sBuilder.toString();
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }
