    public Attributes getAttributes() throws SchemaViolationException, NoSuchAlgorithmException, UnsupportedEncodingException {
        BasicAttributes outAttrs = new BasicAttributes(true);
        BasicAttribute oc = new BasicAttribute("objectclass", "inetOrgPerson");
        oc.add("organizationalPerson");
        oc.add("person");
        outAttrs.put(oc);
        if (lastName != null && firstName != null) {
            outAttrs.put("sn", lastName);
            outAttrs.put("givenName", firstName);
            outAttrs.put("cn", firstName + " " + lastName);
        } else {
            throw new SchemaViolationException("user must have surname");
        }
        if (password != null) {
            MessageDigest sha = MessageDigest.getInstance("md5");
            sha.reset();
            sha.update(password.getBytes("utf-8"));
            byte[] digest = sha.digest();
            String hash = Base64.encodeBase64String(digest);
            outAttrs.put("userPassword", "{MD5}" + hash);
        }
        if (email != null) {
            outAttrs.put("mail", email);
        }
        return (Attributes) outAttrs;
    }
