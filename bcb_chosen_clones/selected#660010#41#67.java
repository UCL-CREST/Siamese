    public void sendMessage(String msg) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SendMessageSSL.this.username, SendMessageSSL.this.password);
            }
        });
        try {
            for (String recipient : this.recipients) {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("from@no-spam.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
                message.setSubject("");
                message.setText(msg);
                Transport.send(message);
            }
            log.info("Message was sent!");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
