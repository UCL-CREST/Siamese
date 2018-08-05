    public static String getMD5(String s) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(s.toLowerCase().getBytes());
            return HexString.bufferToHex(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error grave al inicializar MD5");
            e.printStackTrace();
            return "!!";
        }
    }
