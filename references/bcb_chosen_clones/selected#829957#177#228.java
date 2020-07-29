    private boolean authenticate(Module module) throws Exception {
        SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
        rand.setSeed(System.currentTimeMillis());
        byte[] challenge = new byte[16];
        rand.nextBytes(challenge);
        String b64 = Util.base64(challenge);
        Util.writeASCII(out, RSYNCD_AUTHREQD + b64 + "\n");
        String reply = Util.readLine(in);
        if (reply.indexOf(" ") < 0) {
            Util.writeASCII(out, AT_ERROR + ": bad response\n");
            if (remoteVersion < 25) Util.writeASCII(out, RSYNCD_EXIT + "\n");
            socket.close();
            throw new IOException("bad response");
        }
        String user = reply.substring(0, reply.indexOf(" "));
        String response = reply.substring(reply.indexOf(" ") + 1);
        if (!module.users.contains(user)) {
            Util.writeASCII(out, AT_ERROR + ": user " + user + " not allowed\n");
            if (remoteVersion < 25) Util.writeASCII(out, RSYNCD_EXIT + "\n");
            socket.close();
            throw new IOException("user " + user + " not allowed");
        }
        LineNumberReader secrets = new LineNumberReader(new FileReader(module.secretsFile));
        MessageDigest md4 = MessageDigest.getInstance("BrokenMD4");
        String line;
        while ((line = secrets.readLine()) != null) {
            if (line.startsWith(user + ":")) {
                String passwd = line.substring(line.lastIndexOf(":") + 1);
                md4.update(new byte[4]);
                md4.update(passwd.getBytes("US-ASCII"));
                md4.update(b64.getBytes("US-ASCII"));
                String hash = Util.base64(md4.digest());
                if (hash.equals(response)) {
                    secrets.close();
                    return true;
                } else {
                    Util.writeASCII(out, AT_ERROR + ": auth failed on module " + module.name + "\n");
                    if (remoteVersion < 25) Util.writeASCII(out, RSYNCD_EXIT + "\n");
                    socket.close();
                    secrets.close();
                    logger.error("auth failed on module " + module.name);
                    return false;
                }
            }
        }
        Util.writeASCII(out, AT_ERROR + ": auth failed on module " + module.name + "\n");
        if (remoteVersion < 25) Util.writeASCII(out, RSYNCD_EXIT + "\n");
        socket.close();
        secrets.close();
        logger.error("auth failed on module " + module.name);
        return false;
    }
