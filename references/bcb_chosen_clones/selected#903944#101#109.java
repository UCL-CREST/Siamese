    byte[] calculateDigest(String value) {
        try {
            MessageDigest mg = MessageDigest.getInstance("SHA1");
            mg.update(value.getBytes());
            return mg.digest();
        } catch (Exception e) {
            throw Bark.unchecker(e);
        }
    }
