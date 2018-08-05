    private Element sendEmail(Document doc) {
        Properties config = new Properties();
        config.put("mail.host", "mail.forms.ge");
        config.put("mail.from", "info@forms.ge");
        config.put("mail.user", "info@forms.ge");
        config.put("mail.password", "erty2sami");
        Session session = Session.getDefaultInstance(config, null);
        MimeMessage message = new MimeMessage(session);
        try {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("dimakura@gmail.com"));
            message.setSubject("Test from Java");
            message.setText("This is a test message sent from java. Be happy!");
            Transport.send(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new SuccessSkin().convert(doc, "message sent");
    }
