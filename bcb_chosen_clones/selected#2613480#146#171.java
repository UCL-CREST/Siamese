    public String encryptLdapPassword(String algorithm) {
        String sEncrypted = _password;
        if ((_password != null) && (_password.length() > 0)) {
            algorithm = Val.chkStr(algorithm);
            boolean bMD5 = algorithm.equalsIgnoreCase("MD5");
            boolean bSHA = algorithm.equalsIgnoreCase("SHA") || algorithm.equalsIgnoreCase("SHA1") || algorithm.equalsIgnoreCase("SHA-1");
            if (bSHA || bMD5) {
                String sAlgorithm = "MD5";
                if (bSHA) {
                    sAlgorithm = "SHA";
                }
                try {
                    MessageDigest md = MessageDigest.getInstance(sAlgorithm);
                    md.update(getPassword().getBytes("UTF-8"));
                    sEncrypted = "{" + sAlgorithm + "}" + (new BASE64Encoder()).encode(md.digest());
                } catch (NoSuchAlgorithmException e) {
                    sEncrypted = null;
                    e.printStackTrace(System.err);
                } catch (UnsupportedEncodingException e) {
                    sEncrypted = null;
                    e.printStackTrace(System.err);
                }
            }
        }
        return sEncrypted;
    }
