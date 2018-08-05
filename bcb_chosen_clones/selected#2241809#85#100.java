    public void MessagesReceived(List msgList) throws Exception {
        for (int i = 0; i < msgList.size(); i++) {
            InboundMessage im = (InboundMessage) msgList.get(i);
            Message msg = new MimeMessage(mailSession);
            msg.setFrom();
            msg.addRecipient(RecipientType.TO, new InternetAddress(getProperty("to")));
            msg.setSubject(updateTemplateString(messageSubject, im));
            if (messageBody != null) {
                msg.setText(updateTemplateString(messageBody, im));
            } else {
                msg.setText(im.toString());
            }
            msg.setSentDate(im.getDate());
            Transport.send(msg);
        }
    }
