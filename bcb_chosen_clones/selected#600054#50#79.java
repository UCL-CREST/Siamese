    public boolean authorize(String username, String password, String filename) {
        open(filename);
        boolean isAuthorized = false;
        StringBuffer encPasswd = null;
        try {
            MessageDigest mdAlgorithm = MessageDigest.getInstance("MD5");
            mdAlgorithm.update(password.getBytes());
            byte[] digest = mdAlgorithm.digest();
            encPasswd = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                password = Integer.toHexString(255 & digest[i]);
                if (password.length() < 2) {
                    password = "0" + password;
                }
                encPasswd.append(password);
            }
        } catch (NoSuchAlgorithmException ex) {
        }
        String encPassword = encPasswd.toString();
        String tempPassword = getPassword(username);
        System.out.println("epass" + encPassword);
        System.out.println("pass" + tempPassword);
        if (tempPassword.equals(encPassword)) {
            isAuthorized = true;
        } else {
            isAuthorized = false;
        }
        close();
        return isAuthorized;
    }
