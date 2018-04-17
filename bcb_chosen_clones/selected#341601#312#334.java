    @Override
    public User login(String username, String password) {
        User user = null;
        try {
            user = (User) em.createQuery("Select o from User o where o.username = :username").setParameter("username", username).getSingleResult();
        } catch (NoResultException e) {
            throw new NestedException(e.getMessage(), e);
        }
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(password.getBytes("UTF-8"));
            byte[] hash = digest.digest();
            BigInteger bigInt = new BigInteger(1, hash);
            String hashtext = bigInt.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            if (hashtext.equals(user.getPassword())) return user;
        } catch (Exception e) {
            throw new NestedException(e.getMessage(), e);
        }
        return null;
    }
