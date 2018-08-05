    public SM2Client(String umn, String authorizationID, String protocol, String serverName, Map props, CallbackHandler handler) {
        super(SM2_MECHANISM + "-" + umn, authorizationID, protocol, serverName, props, handler);
        this.umn = umn;
        complete = false;
        state = 0;
        if (sha == null) try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException x) {
            cat.error("SM2Client()", x);
            throw new RuntimeException(String.valueOf(x));
        }
        sha.update(String.valueOf(umn).getBytes());
        sha.update(String.valueOf(authorizationID).getBytes());
        sha.update(String.valueOf(protocol).getBytes());
        sha.update(String.valueOf(serverName).getBytes());
        sha.update(String.valueOf(properties).getBytes());
        sha.update(String.valueOf(Thread.currentThread().getName()).getBytes());
        uid = new BigInteger(1, sha.digest()).toString(26);
        Ec = null;
    }
