    public static String hashValue(String password, String salt) throws TeqloException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
            md.update(password.getBytes("UTF-8"));
            md.update(salt.getBytes("UTF-8"));
            byte raw[] = md.digest();
            char[] encoded = (new BASE64Encoder()).encode(raw).toCharArray();
            int length = encoded.length;
            while (length > 0 && encoded[length - 1] == '=') length--;
            for (int i = 0; i < length; i++) {
                if (encoded[i] == '+') encoded[i] = '*'; else if (encoded[i] == '/') encoded[i] = '-';
            }
            return new String(encoded, 0, length);
        } catch (Exception e) {
            throw new TeqloException("Security", "password", e, "Could not process password");
        }
    }
