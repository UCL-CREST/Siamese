    public void sendMail(String smtpHost, String username, String password, String senderAddress, String recipientsAddress, String subject, String text) {
        MailAuthenticator auth = new MailAuthenticator(username, password);
        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(properties, auth);
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(senderAddress));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientsAddress, false));
            msg.setSubject(subject);
            msg.setText(text);
            msg.setHeader("Test", "Test");
            msg.setSentDate(new Date());
            Transport.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
