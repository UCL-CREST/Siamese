    public static String encryptPassword(String password) throws PasswordException {
        String hash = null;
        if (password != null && !password.equals("")) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(password.getBytes("UTF-8"));
                byte raw[] = md.digest();
                hash = (new BASE64Encoder()).encode(raw);
            } catch (NoSuchAlgorithmException nsae) {
                throw new PasswordException(PasswordException.SYSTEM_ERROR);
            } catch (UnsupportedEncodingException uee) {
                throw new PasswordException(PasswordException.SYSTEM_ERROR);
            }
        }
        return hash;
    }
