    public static void main(String[] args) throws Exception {
        String to = "john.lindberg@gmail.com";
        String from = to;
        String server = null;
        String subject = "Test";
        String messageText = "Test text";
        String username = null;
        String password = null;
        if (server == null) {
            server = lookupMailHosts(to.substring(to.indexOf('@') + 1))[0];
        }
        System.out.println(server);
        System.exit(0);
        Properties props = new Properties();
        props.put("mail.smtp.host", server);
        Authenticator auth;
        if (username != null && password != null) {
            props.put("mail.smtp.auth", "true");
            auth = new SMTPAuthenticator(username, password);
        } else auth = null;
        Session session = Session.getDefaultInstance(props, auth);
        session.setDebug(true);
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(messageText);
        Transport.send(message);
    }
