    @Override
    public void sendMessage(JID jid, String message) {
        Message msg = new MessageBuilder().withRecipientJids(jid).withBody(message).build();
        boolean messageSent = false;
        XMPPService xmpp = XMPPServiceFactory.getXMPPService();
        if (xmpp.getPresence(jid).isAvailable()) {
            SendResponse status = xmpp.sendMessage(msg);
            messageSent = (status.getStatusMap().get(jid) == SendResponse.Status.SUCCESS);
        }
        if (!messageSent) {
            log.error("Cannot send a message using xmpp to : " + jid.toString());
        }
    }
