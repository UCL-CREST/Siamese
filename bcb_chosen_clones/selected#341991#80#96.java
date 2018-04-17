    public void sendEmail(String address, String replyTo, String subject, String body) {
        Session session = Session.getDefaultInstance(props.getMailProperties(), null);
        MimeMessage message = new MimeMessage(session);
        try {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));
            if (replyTo != null && (!replyTo.equals(""))) {
                message.addFrom(new InternetAddress[] { new InternetAddress(replyTo) });
                message.addHeader("Reply-To", replyTo);
            }
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
        } catch (MessagingException ex) {
            logger.error("NotificationManager: Cannot send email to '" + address + "' with subject '" + subject + "'");
            ex.printStackTrace();
        }
    }
