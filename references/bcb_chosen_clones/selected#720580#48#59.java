    public void sendEmail(String from, String to, String subject, String body) {
        Session session = Session.getDefaultInstance(config, null);
        MimeMessage message = new MimeMessage(session);
        try {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject, ENCODING);
            message.setText(body, ENCODING);
            Transport.send(message);
        } catch (MessagingException ex) {
            System.err.println("Cannot send email. " + ex);
        }
    }
