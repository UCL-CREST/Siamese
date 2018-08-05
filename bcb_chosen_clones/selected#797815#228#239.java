    private String cookieString(String url, String ip) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update((url + "&&" + ip + "&&" + salt.toString()).getBytes());
            java.math.BigInteger hash = new java.math.BigInteger(1, md.digest());
            return hash.toString(16);
        } catch (NoSuchAlgorithmException e) {
            filterConfig.getServletContext().log(this.getClass().getName() + " error " + e);
            return null;
        }
    }
