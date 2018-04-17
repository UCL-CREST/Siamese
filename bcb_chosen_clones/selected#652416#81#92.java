    private void send(String fromJid, String msgBody) {
        XMPPService xmpp = XMPPServiceFactory.getXMPPService();
        JID jid = new JID(fromJid);
        Message msg = new MessageBuilder().withRecipientJids(jid).withBody(msgBody).build();
        if (xmpp.getPresence(jid).isAvailable()) {
            SendResponse status = xmpp.sendMessage(msg);
            boolean messageSent = (status.getStatusMap().get(jid) == SendResponse.Status.SUCCESS);
            if (!messageSent) {
                status = xmpp.sendMessage(msg);
            }
        }
    }
