    private void replyToMessage(Message message, String body) {
        Message reply = new MessageBuilder().withRecipientJids(message.getFromJid()).withMessageType(MessageType.NORMAL).withBody(body).build();
        xmppService.sendMessage(reply);
    }
