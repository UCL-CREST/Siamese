    protected String getOldHash(String text) {
        String hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(text.getBytes("UTF-8"));
            byte[] digestedBytes = md.digest();
            hash = HexUtils.convert(digestedBytes);
        } catch (NoSuchAlgorithmException e) {
            log.log(Level.SEVERE, "Error creating SHA password hash:" + e.getMessage());
            hash = text;
        } catch (UnsupportedEncodingException e) {
            log.log(Level.SEVERE, "UTF-8 not supported!?!");
        }
        return hash;
    }
