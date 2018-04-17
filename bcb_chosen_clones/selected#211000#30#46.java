    @Override
    public void dispatchContent(InputStream is) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Sending content message over JMS");
        }
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IOUtils.copy(is, bos);
        this.send(new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                BytesMessage message = session.createBytesMessage();
                message.writeBytes(bos.toByteArray());
                return message;
            }
        });
    }
