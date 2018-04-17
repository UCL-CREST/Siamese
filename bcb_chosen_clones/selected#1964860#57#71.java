    public void processXmppMessage(Message msg) {
        JID fromJid = msg.getFromJid();
        String jid = fromJid.getId();
        try {
            byte[] raw = Base64.decodeFast(msg.getBody());
            ChannelDataBuffer buffer = ChannelDataBuffer.wrap(raw);
            RpcChannelData recv = new RpcChannelData(buffer, new XmppAddress(jid));
            processIncomingData(recv);
        } catch (Exception e) {
            CharArrayWriter writer = new CharArrayWriter();
            e.printStackTrace(new PrintWriter(writer));
            msg = new MessageBuilder().withRecipientJids(fromJid).withBody(writer.toString()).build();
            xmpp.sendMessage(msg);
        }
    }
