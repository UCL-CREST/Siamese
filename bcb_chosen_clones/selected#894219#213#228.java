    public void setPassword(String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes("UTF-8"));
            byte[] digest = md.digest();
            String encodedPassword = Base64.encode(digest);
            this.password = encodedPassword;
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.SEVERE, "Password creation failed", e);
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            logger.log(Level.SEVERE, "Password creation failed", e);
            throw new RuntimeException(e);
        }
    }
