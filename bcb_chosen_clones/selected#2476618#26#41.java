    public void actionPerformed(ActionEvent e) {
        String digest = null;
        try {
            MessageDigest m = MessageDigest.getInstance("sha1");
            m.reset();
            String pw = String.copyValueOf(this.login.getPassword());
            m.update(pw.getBytes());
            byte[] digestByte = m.digest();
            BigInteger bi = new BigInteger(digestByte);
            digest = bi.toString();
            System.out.println(digest);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        this.model.login(this.login.getHost(), this.login.getPort(), this.login.getUser(), digest);
    }
