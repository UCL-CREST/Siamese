    @Override
    public void update(String mail, String email, String pwd, String firstname, String lastname) throws NamingException, NoSuchAlgorithmException, UnsupportedEncodingException {
        jndiManagerConnection connection = new jndiManagerConnection();
        Attributes attrs = new BasicAttributes();
        attrs.put("sn", lastname);
        attrs.put("givenName", firstname);
        attrs.put("cn", firstname + " " + lastname);
        if (!pwd.isEmpty()) {
            MessageDigest sha = MessageDigest.getInstance("md5");
            sha.reset();
            sha.update(pwd.getBytes("utf-8"));
            byte[] digest = sha.digest();
            String hash = Base64.encodeBase64String(digest);
            attrs.put("userPassword", "{MD5}" + hash);
        }
        DirContext ctx = connection.getLDAPDirContext();
        ctx.modifyAttributes("mail=" + mail + "," + dn, DirContext.REPLACE_ATTRIBUTE, attrs);
        if (!mail.equals(email)) {
            String newName = "mail=" + email + "," + dn;
            String oldName = "mail=" + mail + "," + dn;
            ctx.rename(oldName, newName);
        }
    }
