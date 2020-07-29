    public void send(String msgBody) throws Exception {
        String to = "simonsu@ncic.com.tw";
        String subject = "JMail Test!";
        String from = "simonsu@ncic.com.tw";
        String mailhost = "tphqms3.ncic.corp";
        String mailer = "JMail Mailler";
        Properties props = new Properties();
        props = System.getProperties();
        props.put("mail.smtp.host", mailhost);
        Session session = Session.getDefaultInstance(props, null);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
        msg.setSubject(subject);
        msg.setContent(msgBody, "text/html");
        msg.setSentDate(new Date());
        Transport.send(msg);
    }
