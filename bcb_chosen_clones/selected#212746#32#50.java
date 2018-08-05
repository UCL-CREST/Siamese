    public void doSend() {
        Properties props = new Properties();
        props.put("mail.smtp.host", IConstant.AAU_SMTP);
        session = Session.getDefaultInstance(props, null);
        session.setDebug(false);
        try {
            mesg = new MimeMessage(session);
            mesg.setFrom(new InternetAddress("fred@cs.aau.dk"));
            InternetAddress toAddress = new InternetAddress(message_recip);
            mesg.addRecipient(Message.RecipientType.TO, toAddress);
            mesg.setSubject(message_subject);
            mesg.setText(message_body);
            Transport.send(mesg);
        } catch (MessagingException ex) {
            while ((ex = (MessagingException) ex.getNextException()) != null) {
                ex.printStackTrace();
            }
        }
    }
