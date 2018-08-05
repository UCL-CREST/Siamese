    public void postMail(String smtpHost, String subject, String message, Date date) throws MessagingException {
        boolean debug = true;
        Properties props = new Properties();
        props.put(PROP_MAIL_SMTP_HOST, smtpHost);
        Session session = Session.getInstance(props);
        session.setDebug(debug);
        Message msg = new MimeMessage(session);
        InternetAddress addressFrom = new InternetAddress(getSender());
        msg.setFrom(addressFrom);
        if (date == null) {
            date = new Date();
        }
        msg.setSentDate(date);
        InternetAddress[] addressTo = new InternetAddress[recipients.size()];
        for (int idx = 0; idx < addressTo.length; idx++) {
            addressTo[idx] = new InternetAddress(recipients.get(idx));
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);
        msg.setSubject(subject);
        msg.setContent(message, "text/plain");
        Transport.send(msg);
    }
