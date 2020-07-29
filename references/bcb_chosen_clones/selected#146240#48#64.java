    public void update() {
        try {
            String passwordMD5 = new String();
            if (this.password != null && this.password.length() > 0) {
                MessageDigest md = MessageDigest.getInstance("md5");
                md.update(this.password.getBytes());
                byte[] digest = md.digest();
                for (int i = 0; i < digest.length; i++) {
                    passwordMD5 += Integer.toHexString((digest[i] >> 4) & 0xf);
                    passwordMD5 += Integer.toHexString((digest[i] & 0xf));
                }
            }
            this.authCode = new String(Base64Encoder.encode(new String(this.username + ";" + passwordMD5).getBytes()));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
