    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String strStatus = "";
            XMPPService xmpp = XMPPServiceFactory.getXMPPService();
            Message msg = xmpp.parseMessage(req);
            JID fromJid = msg.getFromJid();
            String body = msg.getBody();
            log.info("Received a message from " + fromJid + " and body = " + body);
            String msgBody = "You sent me : " + body;
            Message replyMessage = new MessageBuilder().withRecipientJids(fromJid).withBody(msgBody).build();
            boolean messageSent = false;
            if (xmpp.getPresence(fromJid).isAvailable()) {
                SendResponse status = xmpp.sendMessage(replyMessage);
                messageSent = (status.getStatusMap().get(fromJid) == SendResponse.Status.SUCCESS);
            }
            if (messageSent) {
                strStatus = "Message has been sent successfully";
            } else {
                strStatus = "Message could not be sent";
            }
            log.info(strStatus);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }
