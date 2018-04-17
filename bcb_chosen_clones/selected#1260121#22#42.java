    public String greetServer(String input) throws IllegalArgumentException {
        if (!FieldVerifier.isValidName(input)) {
            throw new IllegalArgumentException("Name must be at least 4 characters long");
        }
        LOG.info("Start GreetingServiceImpl");
        String serverInfo = getServletContext().getServerInfo();
        String userAgent = getThreadLocalRequest().getHeader("User-Agent");
        JID jid = new JID("wwwpillume@appspot.com");
        String msgBody = "Someone has sent you a gift";
        Message msg = new MessageBuilder().withRecipientJids(jid).withBody(msgBody).build();
        boolean messageSent = false;
        XMPPService xmpp = XMPPServiceFactory.getXMPPService();
        SendResponse status = xmpp.sendMessage(msg);
        messageSent = (status.getStatusMap().get(jid) == SendResponse.Status.SUCCESS);
        if (!messageSent) {
            LOG.info("CANT SEND MESSAGE");
        }
        input = escapeHtml(input);
        userAgent = escapeHtml(userAgent);
        return "Hello, " + input + "!<br><br>I am running " + serverInfo + ".<br><br>It looks like you are using:<br>" + userAgent;
    }
