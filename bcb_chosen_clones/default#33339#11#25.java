    public void sendEmail(String aFromEmailAddr, String aToEmailAddr, String aSubject, String aBody, String aSmtpHost) {
        Properties a = new Properties();
        a.setProperty("mail.host", aSmtpHost);
        Session session = Session.getDefaultInstance(a, null);
        MimeMessage message = new MimeMessage(session);
        try {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(aToEmailAddr));
            message.setSubject(aSubject);
            message.setFrom(new InternetAddress(aFromEmailAddr));
            message.setText(aBody);
            Transport.send(message);
        } catch (MessagingException ex) {
            System.err.println("Cannot send email. " + ex);
        }
    }
