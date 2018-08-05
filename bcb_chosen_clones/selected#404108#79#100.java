    private void sendMailOne(String recipient, String subject, String message, String from) throws MessagingException {
        boolean debug = false;
        Properties props = new Properties();
        props.put("mail.smtp.host", Portlets.getPortalConfig("smtp"));
        props.put("mail.mime.charset", Portlets.getPortalConfig("charset"));
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(debug);
        Message msg = new MimeMessage(session);
        InternetAddress addressFrom = new InternetAddress(from);
        msg.setFrom(addressFrom);
        InternetAddress addressTo = new InternetAddress(recipient);
        if (privacy.compareTo(MailListPortlet.PUBLIC) == 0) {
            msg.setRecipient(Message.RecipientType.TO, addressTo);
        } else if (privacy.compareTo(MailListPortlet.PRIVATE) == 0) {
            msg.setRecipient(Message.RecipientType.BCC, addressTo);
        } else {
            System.out.println("Napačen tip vidnosti naslovov!");
        }
        msg.setSubject(subject);
        msg.setContent(message, "text/plain; charset=" + Portlets.getPortalConfig("charset"));
        Transport.send(msg);
    }
