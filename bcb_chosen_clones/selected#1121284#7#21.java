    public byte[] getCoded(String name, String pass) {
        byte[] digest = null;
        if (pass != null && 0 < pass.length()) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                md.update(name.getBytes());
                md.update(pass.getBytes());
                digest = md.digest();
            } catch (Exception e) {
                e.printStackTrace();
                digest = null;
            }
        }
        return digest;
    }
