    public static void sendMailMessage(MailMessage mailMessage) throws Exception {
        logger.info("sendMailMessage - sending email - " + mailMessage.toShortString());
        Properties props = new Properties();
        Session mailSession = Session.getDefaultInstance(props, null);
        if (logger.isLoggable(Level.FINE)) logger.fine("sendMailMessage - got the Mail Session");
        Message msg = new MimeMessage(mailSession);
        msg.setFrom(mailMessage.getFrom());
        msg.addRecipients(Message.RecipientType.TO, mailMessage.getTo());
        msg.setSubject(mailMessage.getSubject());
        msg.setText(mailMessage.getBody().getContentAsString());
        if (logger.isLoggable(Level.FINE)) logger.fine("sendMailMessage - The message is ready to be sent");
        Transport.send(msg);
        logger.info("sendMailMessage - The message is sent");
    }
