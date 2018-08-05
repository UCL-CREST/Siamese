    public void setNewPassword(String password) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            final String encrypted = "{MD5}" + new String(Base64Encoder.encode(digest.digest()));
            setUserPassword(encrypted.getBytes());
            this.newPassword = password;
            firePropertyChange("newPassword", "", password);
            firePropertyChange("password", new byte[0], getUserPassword());
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("Can't encrypt user's password", e);
        }
    }
