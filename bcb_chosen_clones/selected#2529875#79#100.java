        public final String hashPassword(final String password) {
            try {
                if (salt == null) {
                    salt = new byte[16];
                    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
                    sr.setSeed(System.currentTimeMillis());
                    sr.nextBytes(salt);
                }
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(salt);
                md.update(password.getBytes("UTF-8"));
                byte[] hash = md.digest();
                for (int i = 0; i < (1999); i++) {
                    md.reset();
                    hash = md.digest(hash);
                }
                return byteToString(hash, 60);
            } catch (Exception exception) {
                log.error(exception);
                return null;
            }
        }
