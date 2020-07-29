    private static String myCrypt(String password, String seed) throws RuntimeException {
        String out = null;
        int count = 8;
        MessageDigest digester;
        if (!seed.substring(0, 3).equals("$H$")) {
            byte[] randomBytes = new byte[6];
            java.util.Random randomGenerator = new java.util.Random();
            randomGenerator.nextBytes(randomBytes);
            seed = genSalt(randomBytes);
        }
        String salt = seed.substring(4, 12);
        if (salt.length() != 8) {
            throw new RuntimeException("Error hashing password - Invalid seed.");
        }
        byte[] sha1Hash = new byte[40];
        try {
            digester = MessageDigest.getInstance("SHA-1");
            digester.update((salt + password).getBytes("iso-8859-1"), 0, (salt + password).length());
            sha1Hash = digester.digest();
            do {
                byte[] CombinedBytes = new byte[sha1Hash.length + password.length()];
                System.arraycopy(sha1Hash, 0, CombinedBytes, 0, sha1Hash.length);
                System.arraycopy(password.getBytes("iso-8859-1"), 0, CombinedBytes, sha1Hash.length, password.getBytes("iso-8859-1").length);
                digester.update(CombinedBytes, 0, CombinedBytes.length);
                sha1Hash = digester.digest();
            } while (--count > 0);
            out = seed.substring(0, 12);
            out += encode64(sha1Hash);
        } catch (NoSuchAlgorithmException Ex) {
            log.error("Error hashing password.", Ex);
        } catch (UnsupportedEncodingException Ex) {
            log.error("Error hashing password.", Ex);
        }
        if (out == null) {
            throw new RuntimeException("Error hashing password - out = null");
        }
        return out;
    }
