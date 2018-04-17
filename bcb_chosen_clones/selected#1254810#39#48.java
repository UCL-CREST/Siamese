    public ChatClient registerPlayer(int playerId, String playerLogin) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.reset();
        md.update(playerLogin.getBytes("UTF-8"), 0, playerLogin.length());
        byte[] accountToken = md.digest();
        byte[] token = generateToken(accountToken);
        ChatClient chatClient = new ChatClient(playerId, token);
        players.put(playerId, chatClient);
        return chatClient;
    }
