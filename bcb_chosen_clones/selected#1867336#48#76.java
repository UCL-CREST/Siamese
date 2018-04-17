    public boolean sendMessage(String smtpHost, String fromAddress, String fromName, String to, String subject, String text) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST_NAME);
            props.put("mail.smtp.auth", "true");
            props.put("mail.debug", "true");
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.socketFactory.port", SMTP_PORT);
            props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.put("mail.smtp.socketFactory.fallback", "false");
            Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {

                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(BaseSystem.getSettings().getProperty("gmail.userid"), BaseSystem.getSettings().getProperty("gmail.password"));
                }
            });
            Message msg = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress(fromAddress);
            msg.setFrom(addressFrom);
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setSubject(subject);
            msg.setContent(text, "text/html");
            Transport.send(msg);
            return true;
        } catch (Exception e) {
            Logger.log(e);
            return false;
        }
    }
