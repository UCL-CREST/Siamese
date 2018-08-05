                public void send(Buffer buf) {
                    Message msg = new MessageBuilder().withRecipientJids(jid).withMessageType(MessageType.CHAT).withBody(Base64.encodeToString(buf.getRawBuffer(), buf.getReadIndex(), buf.readableBytes(), false)).build();
                    {
                        int retry = ServerConfigurationService.getServerConfig().getFetchRetryCount();
                        while (SendResponse.Status.SUCCESS != xmpp.sendMessage(msg).getStatusMap().get(jid) && retry-- > 0) {
                            logger.error("Failed to send response, try again!");
                        }
                    }
                }
