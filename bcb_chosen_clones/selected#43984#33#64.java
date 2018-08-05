    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Message message = xmpp.parseMessage(req);
        try {
            final JID jid = message.getFromJid();
            byte[] raw = Base64.decodeFast(message.getBody());
            Buffer buffer = Buffer.wrapReadableContent(raw);
            EventHeaderTags tags = new EventHeaderTags();
            Event event = GAEEventHelper.parseEvent(buffer, tags);
            final GAEServerConfiguration cfg = ServerConfigurationService.getServerConfig();
            EventSendService sendService = new EventSendService() {

                public int getMaxDataPackageSize() {
                    return cfg.getMaxXMPPDataPackageSize();
                }

                public void send(Buffer buf) {
                    Message msg = new MessageBuilder().withRecipientJids(jid).withMessageType(MessageType.CHAT).withBody(Base64.encodeToString(buf.getRawBuffer(), buf.getReadIndex(), buf.readableBytes(), false)).build();
                    {
                        int retry = ServerConfigurationService.getServerConfig().getFetchRetryCount();
                        while (SendResponse.Status.SUCCESS != xmpp.sendMessage(msg).getStatusMap().get(jid) && retry-- > 0) {
                            logger.error("Failed to send response, try again!");
                        }
                    }
                }
            };
            event.setAttachment(new Object[] { tags, sendService });
            EventDispatcher.getSingletonInstance().dispatch(event);
        } catch (Throwable e) {
            logger.warn("Failed to process message", e);
        }
    }
