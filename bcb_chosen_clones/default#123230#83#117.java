    private void sendAlert(String subjectText, String msgText) throws MessagingException {
        System.getProperties().put("mail.smtp.host", getPropHandle().get(MAILSERVER_PROP).toString());
        if ((emailAccountUsername != null) && (!emailAccountUsername.equals(""))) {
            System.getProperties().put("mail.smtp.auth", "true");
        }
        Session session = Session.getInstance(System.getProperties(), null);
        Message msg = new MimeMessage(session);
        if (fromEmail != null) {
            msg.setFrom(fromEmail);
        } else {
            msg.setFrom();
        }
        msg.setRecipients(Message.RecipientType.TO, email);
        msg.setSubject(subjectText);
        Date now = new Date();
        String localhost;
        try {
            localhost = java.net.InetAddress.getLocalHost().getHostName();
        } catch (java.net.UnknownHostException uhe) {
            uhe.printStackTrace();
            localhost = "unknown";
        }
        msg.setText(msgText);
        msg.setHeader("X-Mailer", this.getClass().getName());
        msg.setSentDate(now);
        if ((emailAccountUsername == null) || (emailAccountUsername.equals(""))) {
            Transport.send(msg);
        } else {
            System.err.println("Using email username/password");
            Transport transport = session.getTransport("smtp");
            transport.connect(getPropHandle().get(MAILSERVER_PROP).toString(), emailAccountUsername, emailAccountPassword);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
        }
    }
