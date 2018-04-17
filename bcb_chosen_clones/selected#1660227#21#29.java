    public static void javaMailSend(String from, String to, String subject, String content) throws Exception {
        Session session = Session.getDefaultInstance(defaultSMTPConfig);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setContent(content, "text/html; charset=UTF-8");
        Transport.send(message);
    }
