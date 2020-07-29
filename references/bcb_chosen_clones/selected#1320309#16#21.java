    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        XMPPService xmppService = XMPPServiceFactory.getXMPPService();
        Message message = xmppService.parseMessage(req);
        JID fromId = message.getFromJid();
        xmppService.sendMessage(new MessageBuilder().withBody("Your reply: " + message.getBody()).withRecipientJids(fromId).build());
    }
