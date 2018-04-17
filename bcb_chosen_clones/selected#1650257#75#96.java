    protected synchronized Participant createTestParticipant(Account account) {
        LocalAccount groupLeader = getGroupLeader();
        Group group = new Group("testGroup", account);
        byte[] groupLeaderSignedPublicKey;
        try {
            Cipher cipher = Cipher.getInstance(DataConstants.ASYNC_CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, groupLeader.getPrivateKey());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            CipherOutputStream cos = new CipherOutputStream(bos, cipher);
            try {
                cos.write(account.getPublicKey().getEncoded());
            } finally {
                cos.close();
            }
            groupLeaderSignedPublicKey = bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Participant participant = new Participant(account, account.getUserName() + "_in_" + group.getName(), group, groupLeaderSignedPublicKey);
        group.add(participant);
        return participant;
    }
