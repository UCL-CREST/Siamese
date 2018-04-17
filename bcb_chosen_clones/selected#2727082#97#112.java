    private void sendJabber(String msgBody) {
        log.info("Zacatek sendJabber");
        JID jid = new JID("tonda.kmoch@gmail.com");
        Message msg2 = new MessageBuilder().withRecipientJids(jid).withBody(msgBody).build();
        boolean messageSent = false;
        XMPPService xmpp = XMPPServiceFactory.getXMPPService();
        if (xmpp.getPresence(jid).isAvailable()) {
            SendResponse status = xmpp.sendMessage(msg2);
            messageSent = (status.getStatusMap().get(jid) == SendResponse.Status.SUCCESS);
        } else {
            log.info("Uzivatel neni dostupny");
        }
        if (messageSent) {
            log.info("Uspesne odeslano");
        }
    }
