    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        XMPPService xmpp = XMPPServiceFactory.getXMPPService();
        Message message = xmpp.parseMessage(req);
        JID fromJid = message.getFromJid();
        String body = message.getBody();
        String respMsg = null;
        if (body.equals("/list")) {
            respMsg = getPlaneList();
        } else if (body.equals("/help")) {
            respMsg = "Welcome to the Plane-Crazy Chatbot!\nThe following commands are supported: \n /list \n /help";
        } else {
            respMsg = "Command '" + body + "' not supported! \nEnter '/help' for list of commands.";
        }
        JID tojid = new JID(fromJid.getId());
        Message msg = new MessageBuilder().withRecipientJids(tojid).withBody(respMsg).build();
        boolean messageSent = false;
        xmpp = XMPPServiceFactory.getXMPPService();
        if (xmpp.getPresence(tojid).isAvailable()) {
            SendResponse status = xmpp.sendMessage(msg);
            messageSent = (status.getStatusMap().get(tojid) == SendResponse.Status.SUCCESS);
        }
    }
