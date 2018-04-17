    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        XMPPService xmpp = XMPPServiceFactory.getXMPPService();
        Message msg = xmpp.parseMessage(req);
        JID jid = msg.getFromJid();
        String body = msg.getBody();
        LOG.info(jid.getId() + " --> JEliza: " + body);
        Key key = KeyFactory.createKey("chatData", ":" + jid.getId());
        Entity lastLineEntity = null;
        try {
            lastLineEntity = DatastoreServiceFactory.getDatastoreService().get(key);
        } catch (EntityNotFoundException e) {
            lastLineEntity = new Entity(key);
            lastLineEntity.setProperty(LINE, "");
        }
        final String lastLine = (String) lastLineEntity.getProperty(LINE);
        final StringBuilder response = new StringBuilder();
        final ElizaParse parser = new ElizaParse() {

            @Override
            public void PRINT(String s) {
                if (lastLine.trim().length() > 0 && s.startsWith("HI! I'M ELIZA")) {
                    return;
                }
                response.append(s);
                response.append('\n');
            }
        };
        parser.lastline = lastLine;
        parser.handleLine(body);
        body = response.toString();
        LOG.info(jid.getId() + " <-- JEliza: " + body);
        msg = new MessageBuilder().withRecipientJids(jid).withBody(body).build();
        xmpp.sendMessage(msg);
        if (parser.exit) {
            lastLineEntity.setProperty(LINE, "");
        } else {
            lastLineEntity.setProperty(LINE, parser.lastline);
        }
        DatastoreServiceFactory.getDatastoreService().put(lastLineEntity);
    }
