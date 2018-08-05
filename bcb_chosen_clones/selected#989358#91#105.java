    public void visit(AuthenticationMD5Password message) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(((String) properties.get("password") + (String) properties.get("user")).getBytes("iso8859-1"));
            String newValue = toHexString(md5.digest()) + new String(message.getSalt(), "iso8859-1");
            md5.reset();
            md5.update(newValue.getBytes("iso8859-1"));
            newValue = toHexString(md5.digest());
            PasswordMessage mes = new PasswordMessage("md5" + newValue);
            byte[] data = encoder.encode(mes);
            out.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
