    public void sendMessage(String subject, String messageBody) {
        Date today = new Date();
        Session session = Session.getDefaultInstance(mailProperties, null);
        Message message = new MimeMessage(session);
        try {
            InternetAddress fromAddress = new InternetAddress(mailProperties.getProperty("mailer.mail.from"));
            message.setFrom(fromAddress);
            InternetAddress[] toAddress = InternetAddress.parse(mailProperties.getProperty("mailer.mail.to"), false);
            message.setRecipients(Message.RecipientType.TO, toAddress);
            message.setSubject(subject);
            message.setText(messageBody);
            message.setSentDate(today);
            Transport.send(message);
            LOGGER.info("Sent mail to " + mailProperties.getProperty("mailer.mail.to"));
        } catch (MessagingException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
