    @Override
    public User saveUser(User user) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(user.getPassword().getBytes("UTF-8"));
            byte[] hash = digest.digest();
            BigInteger bigInt = new BigInteger(1, hash);
            String hashtext = bigInt.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            user.setPassword(hashtext);
            user.setDataRegjistrimit(new Date());
            return em.merge(user);
        } catch (Exception e) {
            throw new NestedException(e.getMessage(), e);
        }
    }
