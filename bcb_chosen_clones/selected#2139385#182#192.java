    public String getSHA1(String input) {
        byte[] output = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(input.getBytes());
            output = md.digest();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        return StringUtils.byte2hex(output);
    }
