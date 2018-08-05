    public static Properties addAttributes(Node node, String[] names, Properties props, LogEvent evt) throws ConfigurationException {
        if (props == null) props = new Properties();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            for (int i = 0; i < names.length; i++) {
                String value = addProperty(names[i], props, node, evt);
                if (value != null) {
                    md.update(names[i].getBytes());
                    md.update(value.getBytes());
                }
            }
            byte[] digest = md.digest();
            evt.addMessage("digest " + ISOUtil.hexString(digest));
            props.put(DIGEST_PROPERTY, digest);
        } catch (NoSuchAlgorithmException e) {
            throw new ConfigurationException(e);
        }
        return props;
    }
