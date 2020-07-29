        public final String hashPassword(String password) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(password.getBytes());
                byte[] hash = md.digest();
                return "{SHA}" + byteToString(hash, 60);
            } catch (NoSuchAlgorithmException nsae) {
                log.error("Error getting password hash - " + nsae.getMessage());
                return null;
            }
        }
