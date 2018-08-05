    public void onTestRunCompletion(String statusURL, long runId) {
        String msgBody = buildMessageBody(statusURL, runId);
        Message msg = new MessageBuilder().withRecipientJids(jid).withBody(msgBody).build();
        XMPPService xmpp = XMPPServiceFactory.getXMPPService();
        if (xmpp.getPresence(jid).isAvailable()) {
            SendResponse status = xmpp.sendMessage(msg);
            SendResponse.Status sendStatus = status.getStatusMap().get(jid);
            logger.fine("xmpp send to " + jid + " was successful.");
            if (sendStatus != SendResponse.Status.SUCCESS) {
                logger.warning("xmpp send to " + jid + " failed with status " + sendStatus);
            }
        } else {
            logger.warning("Could not send xmpp notification to " + jid + " because user is not available.");
        }
    }
