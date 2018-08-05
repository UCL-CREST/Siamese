    public static final String md5(String name) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
        md.reset();
        md.update(name.getBytes());
        byte b[] = md.digest();
        String encoded = "";
        for (int i = 0; i < b.length; i++) {
            int b2 = (int) b[i] % 16;
            if (b2 < 0) b2 += 16;
            int b1 = (int) b[i];
            if (b1 < 0) b1 += 16 * 16;
            b1 -= b2;
            b1 /= 16;
            if (b1 <= 9) encoded += (char) ((int) '0' + (int) b1); else encoded += (char) ((int) 'a' + (int) b1 - (int) 10);
            if (b2 <= 9) encoded += (char) ((int) '0' + (int) b2); else encoded += (char) ((int) 'a' + (int) b2 - (int) 10);
        }
        return encoded;
    }
