	public static boolean sendMessage(String message, String receipient) {
		//Create Message
		JID jid = new JID(receipient);
        Message msg = new MessageBuilder()
            .withRecipientJids(jid)
            .withBody(message)
            .build();

        //Send Message
        XMPPService xmpp = XMPPServiceFactory.getXMPPService();
        SendResponse status = xmpp.sendMessage(msg);
        
        //Get and return if successful
        boolean messageSent = false;
        messageSent = (status.getStatusMap().get(jid) == SendResponse.Status.SUCCESS);
        return messaageSent;
	}
