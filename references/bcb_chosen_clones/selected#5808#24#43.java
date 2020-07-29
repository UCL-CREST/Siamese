    public synchronized void sendMail(String subject, String body, String recipients) throws Exception {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("resources/MailSender.properties");
        if (input != null) {
            Properties props = new Properties();
            props.load(input);
            Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {

                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(sender, password);
                }
            });
            MimeMessage message = new MimeMessage(session);
            message.setSender(new InternetAddress(sender));
            message.setSubject(subject, "text/plain");
            message.setContent(body, "text/plain");
            if (recipients.indexOf(',') > 0) message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients)); else message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
            Transport.send(message);
        } else System.out.println("MailSender Properties File not found!");
    }
