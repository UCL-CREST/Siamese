    public void sendEmail(Email email, String address) throws MessagingException {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("darmoweaplikacjejava@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(address));
            message.setSubject(email.getSubject());
            message.setText(email.getContent());
            Transport.send(message);
        } catch (MessagingException e) {
            logger.log(Level.WARNING, e.toString());
            throw e;
        }
    }
