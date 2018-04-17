    public static Set<JID> sendMessage(String message, List<JID> recipients) {
        if (recipients == null || recipients.isEmpty()) {
            return Collections.emptySet();
        }
        SendResponse response = null;
        try {
            response = xmpp.sendMessage(new MessageBuilder().withBody(message).withRecipientJids(recipients.toArray(new JID[] {})).build());
        } catch (RuntimeException e) {
            return Collections.emptySet();
        }
        if (response == null) {
            logger.severe("XMPP.sendMessage() response is null!");
            return Collections.emptySet();
        }
        Set<JID> errorJIDs = Sets.newHashSet();
        for (JID jid : recipients) {
            Status status = response.getStatusMap().get(jid);
            if (status != Status.SUCCESS) {
                errorJIDs.add(jid);
                StringBuilder sb = new StringBuilder().append("sendMessage unsuccessful! ").append("status: ").append(status.name()).append(" from: ").append(" / to: ").append(jid.getId());
                logger.severe(sb.toString());
            }
        }
        return errorJIDs;
    }
