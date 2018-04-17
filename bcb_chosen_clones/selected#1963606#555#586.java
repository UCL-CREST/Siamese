    private boolean authenticate(String reply) {
        String user = reply.substring(0, reply.indexOf(" "));
        String resp = reply.substring(reply.indexOf(" ") + 1);
        if (!module.users.contains(user)) {
            error = "so such user " + user;
            return false;
        }
        try {
            LineNumberReader secrets = new LineNumberReader(new FileReader(module.secretsFile));
            String line;
            while ((line = secrets.readLine()) != null) {
                if (line.startsWith(user + ":")) {
                    MessageDigest md4 = MessageDigest.getInstance("BrokenMD4");
                    md4.update(new byte[4]);
                    md4.update(line.substring(line.indexOf(":") + 1).getBytes("US-ASCII"));
                    md4.update(challenge.getBytes("US-ASCII"));
                    String hash = Util.base64(md4.digest());
                    if (hash.equals(resp)) {
                        secrets.close();
                        return true;
                    }
                }
            }
            secrets.close();
        } catch (Exception e) {
            logger.fatal(e.toString());
            error = "server configuration error";
            return false;
        }
        error = "authentication failure for module " + module.name;
        return false;
    }
