    static byte[] getPassword(final String name, final String password) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            messageDigest.update(name.getBytes());
            messageDigest.update(password.getBytes());
            return messageDigest.digest();
        } catch (final NoSuchAlgorithmException e) {
            throw new JobException(e);
        }
    }
