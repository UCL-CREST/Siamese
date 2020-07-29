    public boolean log(String message) {
        String sendMessage = null;
        if (message == null) {
            sendMessage = "null";
        } else {
            sendMessage = message;
        }
        JID jid = new JID(email);
        Message msg = new MessageBuilder().withRecipientJids(jid).withBody(sendMessage).build();
        boolean messageSent = false;
        if (xmpp.getPresence(jid).isAvailable()) {
            SendResponse status = xmpp.sendMessage(msg);
            messageSent = (status.getStatusMap().get(jid) == SendResponse.Status.SUCCESS);
        }
        return messageSent;
    }
