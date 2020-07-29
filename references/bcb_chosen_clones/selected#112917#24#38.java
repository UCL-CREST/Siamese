    public PollSetMessage(String username, String question, String title, String[] choices) {
        this.username = username;
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        String id = username + String.valueOf(System.nanoTime());
        m.update(id.getBytes(), 0, id.length());
        voteId = new BigInteger(1, m.digest()).toString(16);
        this.question = question;
        this.title = title;
        this.choices = choices;
    }
