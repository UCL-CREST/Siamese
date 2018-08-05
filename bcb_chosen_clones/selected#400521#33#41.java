    public String encrypt(String pstrPlainText) throws Exception {
        if (pstrPlainText == null) {
            return "";
        }
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(pstrPlainText.getBytes("UTF-8"));
        byte raw[] = md.digest();
        return (new BASE64Encoder()).encode(raw);
    }
