    public boolean authenticate(String user, String pass) throws IOException {
        MessageDigest hash = null;
        try {
            MessageDigest.getInstance("BrokenMD4");
        } catch (NoSuchAlgorithmException x) {
            throw new Error(x);
        }
        hash.update(new byte[4], 0, 4);
        try {
            hash.update(pass.getBytes("US-ASCII"), 0, pass.length());
            hash.update(challenge.getBytes("US-ASCII"), 0, challenge.length());
        } catch (java.io.UnsupportedEncodingException shouldNeverHappen) {
        }
        String response = Util.base64(hash.digest());
        Util.writeASCII(out, user + " " + response + '\n');
        String reply = Util.readLine(in);
        if (reply.startsWith(RSYNCD_OK)) {
            authReqd = false;
            return true;
        }
        connected = false;
        error = reply;
        return false;
    }
