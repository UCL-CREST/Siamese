    public static String getSHA1(String s) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            sha1.update(s.toLowerCase().getBytes());
            return HexString.bufferToHex(sha1.digest());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error grave al inicializar SHA1");
            e.printStackTrace();
            return "!!";
        }
    }
