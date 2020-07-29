    private void onDhReply(final SshDhReply msg) throws GeneralSecurityException, IOException {
        if ((this.keyPair == null) || this.connection.isServer()) throw new SshException("%s: unexpected %s", this.connection.uri, msg.getType());
        final BigInteger k;
        {
            final DHPublicKeySpec remoteKeySpec = new DHPublicKeySpec(new BigInteger(msg.f), P1, G);
            final KeyFactory dhKeyFact = KeyFactory.getInstance("DH");
            final DHPublicKey remotePubKey = (DHPublicKey) dhKeyFact.generatePublic(remoteKeySpec);
            final KeyAgreement dhKex = KeyAgreement.getInstance("DH");
            dhKex.init(this.keyPair.getPrivate());
            dhKex.doPhase(remotePubKey, true);
            k = new BigInteger(dhKex.generateSecret());
        }
        final MessageDigest md = createMessageDigest();
        final byte[] h;
        {
            updateByteArray(md, SshVersion.LOCAL.toString().getBytes());
            updateByteArray(md, this.connection.getRemoteSshVersion().toString().getBytes());
            updateByteArray(md, this.keyExchangeInitLocal.getPayload());
            updateByteArray(md, this.keyExchangeInitRemote.getPayload());
            updateByteArray(md, msg.hostKey);
            updateByteArray(md, ((DHPublicKey) this.keyPair.getPublic()).getY().toByteArray());
            updateByteArray(md, msg.f);
            updateBigInt(md, k);
            h = md.digest();
        }
        if (this.sessionId == null) this.sessionId = h;
        this.keyExchangeInitLocal = null;
        this.keyExchangeInitRemote = null;
        this.h = h;
        this.k = k;
        this.connection.send(new SshKeyExchangeNewKeys());
    }
