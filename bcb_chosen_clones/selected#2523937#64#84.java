    @Override
    public String getMessageDigest() throws SarasvatiLoadException {
        if (messageDigest == null) {
            Collections.sort(nodes);
            Collections.sort(externals);
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA1");
                digest.update(name.getBytes());
                for (XmlNode node : nodes) {
                    node.addToDigest(digest);
                }
                for (XmlExternal external : externals) {
                    external.addToDigest(digest);
                }
                messageDigest = SvUtil.getHexString(digest.digest());
            } catch (NoSuchAlgorithmException nsae) {
                throw new SarasvatiException("Unable to load SHA1 algorithm", nsae);
            }
        }
        return messageDigest;
    }
