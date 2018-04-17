    @Override
    public String sendMessageToMetafora(String messageBody, String recipients) {
        if (recipients == null) {
            return null;
        }
        String[] recipientsArray = recipients.split(",");
        JID[] jids = new JID[recipientsArray.length];
        int index = 0;
        for (String recipient : recipientsArray) {
            jids[index] = new JID(recipient.trim());
            index++;
        }
        Message message = new MessageBuilder().withRecipientJids(jids).withBody(messageBody).withFromJid(fromJID).build();
        SendResponse response = metaforaXMPP.sendMessage(message);
        Status statusCode = response.getStatusMap().get(jids[0]);
        boolean messageSent = (statusCode == SendResponse.Status.SUCCESS);
        if (messageSent) {
            return null;
        } else {
            String error = "Failed to send message. Status: " + statusCode.toString() + ". Message: " + messageBody;
            ServerUtils.severe(error);
            return error;
        }
    }
