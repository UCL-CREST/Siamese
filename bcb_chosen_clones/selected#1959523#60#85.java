    public void sendMail(String from, String to, String subject, String body) {
        try {
            MimeMessage message = new MimeMessage(session);
            InternetAddress internetAddress = new InternetAddress(from);
            message.setFrom(internetAddress);
            message.setReplyTo(new InternetAddress[] { internetAddress });
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(body);
            message.setSentDate(new Date());
            message.saveChanges();
            if (!Environment.getInstance().getStringSetting(Environment.PROPERTY_SMTP_USERNAME).equals("") && !!Environment.getInstance().getStringSetting(Environment.PROPERTY_SMTP_PASSWORD).equals("")) {
                String username = Environment.getInstance().getStringSetting(Environment.PROPERTY_SMTP_USERNAME);
                String password = Environment.getInstance().getStringSetting(Environment.PROPERTY_SMTP_PASSWORD);
                String smtphost = Environment.getInstance().getStringSetting(Environment.PROPERTY_SMTP_HOST);
                Transport tr = session.getTransport("smtp");
                tr.connect(smtphost, username, password);
                tr.sendMessage(message, message.getAllRecipients());
                tr.close();
            } else {
                Transport.send(message);
            }
        } catch (MessagingException e) {
            logger.warn("Mail error", e);
        }
    }
