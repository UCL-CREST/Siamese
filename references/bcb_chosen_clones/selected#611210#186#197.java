    public static void sendNotificationByXMPP(Contact contact, Alert alert, String headline, String msgBody) {
        XMPPService xmpp = XMPPServiceFactory.getXMPPService();
        com.google.appengine.api.xmpp.Message msg = new MessageBuilder().withRecipientJids(contact.getXMPP()).withBody(msgBody).build();
        try {
            xmpp.sendMessage(msg);
        } catch (Exception ex) {
            msgBody += "\nYou are receiving this by email because there was a problem sending via XMPP";
            msgBody += "\nMake sure to add aimeemonitoring@appspot.com to your friends list in your XMPP client";
            sendNotificationByEmail(contact, alert, headline, msgBody, contact.getSMS());
            sendNotificationByEmail(contact, alert, headline, msgBody, contact.getEmail().getEmail());
        }
    }
