    private String getMD5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] data = md.digest();
            return convertToHex(data);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
