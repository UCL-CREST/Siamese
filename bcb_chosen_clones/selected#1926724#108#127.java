    public void send(String _to, String subject, String text) throws Exception {
        if (session == null) {
            Properties p = new Properties();
            p.put("mail.host", mailhost);
            p.put("mail.user", mailuser);
            session = Session.getDefaultInstance(p, null);
            Properties properties = session.getProperties();
            String key = "mail.smtp.localhost";
            String prop = properties.getProperty(key);
            if (prop == null) properties.put(key, localhost); else System.out.println(key + ": " + prop);
        }
        MimeMessage msg = new MimeMessage(session);
        msg.setText(text);
        msg.setSubject(subject);
        Address fromAddr = new InternetAddress(mailuser);
        msg.setFrom(fromAddr);
        Address toAddr = new InternetAddress(_to);
        msg.addRecipient(Message.RecipientType.TO, toAddr);
        Transport.send(msg);
    }
