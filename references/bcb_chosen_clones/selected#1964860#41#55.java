    @Override
    protected void send(RpcChannelData data) throws IOException {
        if (logger.isInfoEnabled()) {
            logger.info("Send message to " + data.address.toPrintableString());
        }
        XmppAddress dataaddress = (XmppAddress) data.address;
        JID jid = new JID(dataaddress.getJid());
        Message msg = new MessageBuilder().withRecipientJids(jid).withMessageType(MessageType.CHAT).withBody(Base64.encodeToString(ChannelDataBuffer.asByteArray(data.content), false)).build();
        {
            int retry = RETRY;
            while (SendResponse.Status.SUCCESS != xmpp.sendMessage(msg).getStatusMap().get(jid) && retry-- > 0) {
                logger.error("Failed to send response, try again!");
            }
        }
    }
