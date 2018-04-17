    protected synchronized void doLogin(long timeout, String eventMask) throws IOException, AuthenticationFailedException, TimeoutException {
        ChallengeAction challengeAction;
        ManagerResponse challengeResponse;
        String challenge;
        String key;
        LoginAction loginAction;
        ManagerResponse loginResponse;
        if (socket == null) {
            connect();
        }
        synchronized (protocolIdentifier) {
            if (protocolIdentifier.value == null) {
                try {
                    protocolIdentifier.wait(timeout);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            if (protocolIdentifier.value == null) {
                disconnect();
                if (reader != null && reader.getTerminationException() != null) {
                    throw reader.getTerminationException();
                } else {
                    throw new TimeoutException("Timeout waiting for protocol identifier");
                }
            }
        }
        challengeAction = new ChallengeAction("MD5");
        try {
            challengeResponse = sendAction(challengeAction);
        } catch (Exception e) {
            disconnect();
            throw new AuthenticationFailedException("Unable to send challenge action", e);
        }
        if (challengeResponse instanceof ChallengeResponse) {
            challenge = ((ChallengeResponse) challengeResponse).getChallenge();
        } else {
            disconnect();
            throw new AuthenticationFailedException("Unable to get challenge from Asterisk. ChallengeAction returned: " + challengeResponse.getMessage());
        }
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("MD5");
            if (challenge != null) {
                md.update(challenge.getBytes());
            }
            if (password != null) {
                md.update(password.getBytes());
            }
            key = ManagerUtil.toHexString(md.digest());
        } catch (NoSuchAlgorithmException ex) {
            disconnect();
            throw new AuthenticationFailedException("Unable to create login key using MD5 Message Digest", ex);
        }
        loginAction = new LoginAction(username, "MD5", key, eventMask);
        try {
            loginResponse = sendAction(loginAction);
        } catch (Exception e) {
            disconnect();
            throw new AuthenticationFailedException("Unable to send login action", e);
        }
        if (loginResponse instanceof ManagerError) {
            disconnect();
            throw new AuthenticationFailedException(loginResponse.getMessage());
        }
        state = CONNECTED;
        logger.info("Successfully logged in");
        version = determineVersion();
        writer.setTargetVersion(version);
        logger.info("Determined Asterisk version: " + version);
        ConnectEvent connectEvent = new ConnectEvent(this);
        connectEvent.setProtocolIdentifier(getProtocolIdentifier());
        connectEvent.setDateReceived(DateUtil.getDate());
        fireEvent(connectEvent);
    }
