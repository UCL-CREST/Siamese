    public void Send(String to, String subject, String msg) throws AddressException, MessagingException {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", this.getHost());
        props.put("mail.smtp.port", this.getPort());
        props.put("mail.smtp.auth", "true");
        Authenticator auth = this.getSma();
        Session session = Session.getDefaultInstance(props, auth);
        Transport transport = session.getTransport("smtp");
        transport.connect(host, this.getSma().getUsername(), this.getSma().getPassword());
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(this.getFrom()));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(msg);
        Transport.send(message);
    }
