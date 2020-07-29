    public boolean mailSender() {
        boolean bea = false;
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", smtpServer);
        if (needSSL == 1) {
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
            props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
        }
        if (!smtpPort.trim().equals("")) {
            props.setProperty("mail.smtp.port", smtpPort);
            props.setProperty("mail.smtp.socketFactory.port", smtpPort);
        }
        if (needAuth) {
            props.setProperty("mail.smtp.auth", "true");
        } else {
            props.setProperty("mail.smtp.auth", "false");
        }
        Session session = Session.getDefaultInstance(props, new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpID, smtpPass);
            }
        });
        try {
            MimeMessage msg = new MimeMessage(session);
            InternetAddress address = new InternetAddress(fromEmail);
            msg.setFrom(address);
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            if (toEmail2 != null && !toEmail2.equals("")) {
                msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(toEmail2, false));
            }
            msg.setSubject(subject);
            msg.setText(content);
            msg.setSentDate(new Date());
            Transport.send(msg);
            message = "Email发送成功......";
            bea = true;
        } catch (MessagingException e) {
            message = e.toString();
            bea = false;
        }
        return bea;
    }
