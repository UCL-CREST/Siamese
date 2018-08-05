    protected byte[] getHashedID(String ID) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(ID.getBytes());
            byte[] digest = md5.digest();
            byte[] bytes = new byte[WLDB_ID_SIZE];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = digest[i];
            }
            return bytes;
        } catch (NoSuchAlgorithmException exception) {
            System.err.println("Java VM is not compatible");
            exit();
            return null;
        }
    }
