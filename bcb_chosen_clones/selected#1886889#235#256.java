    public void apop(String user, char[] secret) throws IOException, POP3Exception {
        if (timestamp == null) {
            throw new CommandNotSupportedException("No timestamp from server - APOP not possible");
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(timestamp.getBytes());
            if (secret == null) secret = new char[0];
            byte[] digest = md.digest(new String(secret).getBytes("ISO-8859-1"));
            mutex.lock();
            sendCommand("APOP", new String[] { user, digestToString(digest) });
            POP3Response response = readSingleLineResponse();
            if (!response.isOK()) {
                throw new POP3Exception(response);
            }
            state = TRANSACTION;
        } catch (NoSuchAlgorithmException e) {
            throw new POP3Exception("Installed JRE doesn't support MD5 - APOP not possible");
        } finally {
            mutex.release();
        }
    }
