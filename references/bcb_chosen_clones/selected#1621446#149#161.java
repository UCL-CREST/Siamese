    @Override
    public void flush() {
        String message = _message.get();
        if (message != null && message.length() > 0 && _thresholdReached.get() && _atLeastOneLogRecord.get()) {
            XMPPService xmpp = XMPPServiceFactory.getXMPPService();
            MessageBuilder msgBuilder = new MessageBuilder();
            if (_sender != null) msgBuilder.withFromJid(_sender);
            Message msg = msgBuilder.withRecipientJids(_recipients).withBody(_message.get()).build();
            xmpp.sendMessage(msg);
        }
        _message.set("");
        _atLeastOneLogRecord.set(false);
    }
