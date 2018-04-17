    private String encryptUserPassword(int userId, String password) {
        password = password.trim();
        if (password.length() == 0) {
            return "";
        } else {
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("SHA");
            } catch (NoSuchAlgorithmException ex) {
                throw new BoardRuntimeException(ex);
            }
            md.update(String.valueOf(userId).getBytes());
            md.update(password.getBytes());
            byte b[] = md.digest();
            StringBuffer sb = new StringBuffer(1 + b.length * 2);
            for (int i = 0; i < b.length; i++) {
                int ii = b[i];
                if (ii < 0) {
                    ii = 256 + ii;
                }
                sb.append(getHexadecimalValue2(ii));
            }
            return sb.toString();
        }
    }
