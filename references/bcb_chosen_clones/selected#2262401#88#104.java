    public void postMail(List recipients, String subject, String message, String from) throws MessagingException {
        boolean debug = false;
        Authenticator auth = new SMTPAuthenticator();
        Session session = Session.getDefaultInstance(getProperties(), auth);
        session.setDebug(debug);
        Message msg = new MimeMessage(session);
        InternetAddress addressFrom = new InternetAddress(from);
        msg.setFrom(addressFrom);
        InternetAddress[] addressTo = new InternetAddress[recipients.size()];
        for (int i = 0; i < recipients.size(); i++) {
            addressTo[i] = new InternetAddress((String) recipients.get(i));
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);
        msg.setSubject(subject);
        msg.setContent(message, "text/plain");
        Transport.send(msg);
    }
