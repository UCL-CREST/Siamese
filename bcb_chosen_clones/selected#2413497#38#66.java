    public void postMail(String recipients[], String subject, String message, String from, boolean html) throws MessagingException {
        boolean debug = false;
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        Authenticator auth = new SMTPAuthenticator();
        Session session = Session.getDefaultInstance(props, auth);
        session.setDebug(debug);
        Message msg = new MimeMessage(session);
        InternetAddress addressFrom = new InternetAddress(from);
        msg.setFrom(addressFrom);
        if (recipients == null || recipients.length == 0 || recipients[0] == null || recipients[0].equals("")) {
            InternetAddress[] addressTo = new InternetAddress[1];
            addressTo[0] = new InternetAddress(paraDefault);
        } else {
            InternetAddress[] addressTo = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                addressTo[i] = new InternetAddress(recipients[i]);
            }
            msg.setRecipients(Message.RecipientType.TO, addressTo);
        }
        msg.setSubject(subject);
        if (html) {
            msg.setContent(message, "text/html");
        } else {
            msg.setContent(message, "text/plain");
        }
        Transport.send(msg);
    }
