    public void register(MinecraftSession session, String username, String verificationKey) {
        if (Configuration.getConfiguration().isVerifyingNames()) {
            long salt = HeartbeatManager.getHeartbeatManager().getSalt();
            String hash = new StringBuilder().append(String.valueOf(salt)).append(username).toString();
            MessageDigest digest;
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("No MD5 algorithm!");
            }
            digest.update(hash.getBytes());
            if (!verificationKey.equals(new BigInteger(1, digest.digest()).toString(16))) {
                session.getActionSender().sendLoginFailure("Illegal name.");
                return;
            }
        }
        char[] nameChars = username.toCharArray();
        for (char nameChar : nameChars) {
            if (nameChar < ' ' || nameChar > '\177') {
                session.getActionSender().sendLoginFailure("Invalid name!");
                return;
            }
        }
        for (Player p : playerList.getPlayers()) {
            if (p.getName().equalsIgnoreCase(username)) {
                p.getSession().getActionSender().sendLoginFailure("Logged in from another computer.");
                break;
            }
        }
        final Player player = new Player(session, username);
        if (!playerList.add(player)) {
            player.getSession().getActionSender().sendLoginFailure("Too many players online!");
            return;
        }
        session.setPlayer(player);
        final Configuration c = Configuration.getConfiguration();
        session.getActionSender().sendLoginResponse(Constants.PROTOCOL_VERSION, c.getName(), c.getMessage(), false);
        LevelGzipper.getLevelGzipper().gzipLevel(session);
    }
