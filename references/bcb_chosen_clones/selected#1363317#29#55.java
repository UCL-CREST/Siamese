    public void handleTransition(Defect defect, Map params, Connection connection) throws StateChangeException {
        try {
            MimeMessage message = new MimeMessage(getSession());
            message.setSubject("[sbugs] Defect updated: #" + defect.getId());
            StringBuffer buffer = new StringBuffer(200);
            buffer.append("Defect #");
            buffer.append(defect.getId());
            buffer.append(" has been updated. \nHeadline: ");
            buffer.append(defect.getHeadline());
            buffer.append(" New state: ");
            buffer.append(defect.getState());
            message.setText(buffer.toString());
            Address senderAddress = createSenderAddress(params, connection);
            message.setFrom(senderAddress);
            Address[] recipients = createRecipientAddresses(defect, params, connection);
            if (recipients.length == 0) {
                return;
            }
            for (int i = 0; i < recipients.length; i++) {
                message.addRecipient(Message.RecipientType.TO, recipients[i]);
            }
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new StateChangeException(e.getMessage());
        }
    }
