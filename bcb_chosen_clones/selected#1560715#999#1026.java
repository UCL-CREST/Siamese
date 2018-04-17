    private boolean passwordMatches(String user, String plainPassword, String scrambledPassword) {
        MessageDigest md;
        byte[] temp_digest, pass_digest;
        byte[] hex_digest = new byte[35];
        byte[] scrambled = scrambledPassword.getBytes();
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(plainPassword.getBytes("US-ASCII"));
            md.update(user.getBytes("US-ASCII"));
            temp_digest = md.digest();
            Utils.bytesToHex(temp_digest, hex_digest, 0);
            md.update(hex_digest, 0, 32);
            md.update(salt.getBytes());
            pass_digest = md.digest();
            Utils.bytesToHex(pass_digest, hex_digest, 3);
            hex_digest[0] = (byte) 'm';
            hex_digest[1] = (byte) 'd';
            hex_digest[2] = (byte) '5';
            for (int i = 0; i < hex_digest.length; i++) {
                if (scrambled[i] != hex_digest[i]) {
                    return false;
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return true;
    }
