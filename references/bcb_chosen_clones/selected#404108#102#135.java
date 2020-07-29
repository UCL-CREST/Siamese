    private void sendMailMany(Set recipients, String subject, String message, String from) throws MessagingException {
        boolean debug = false;
        List emails = new ArrayList();
        for (Iterator iter = recipients.iterator(); iter.hasNext(); ) {
            User user = (User) iter.next();
            emails.add(user.getEmail());
        }
        String[] rec = new String[emails.size()];
        for (int i = 0; i < emails.size(); i++) {
            rec[i] = (String) ((emails.toArray())[i]);
        }
        Properties props = new Properties();
        props.put("mail.smtp.host", Portlets.getPortalConfig("smtp"));
        props.put("mail.mime.charset", Portlets.getPortalConfig("charset"));
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(debug);
        Message msg = new MimeMessage(session);
        InternetAddress addressFrom = new InternetAddress(from);
        msg.setFrom(addressFrom);
        InternetAddress[] addressTo = new InternetAddress[rec.length];
        for (int i = 0; i < rec.length; i++) {
            addressTo[i] = new InternetAddress(rec[i]);
        }
        if (privacy.compareTo(MailListPortlet.PUBLIC) == 0) {
            msg.setRecipients(Message.RecipientType.TO, addressTo);
        } else if (privacy.compareTo(MailListPortlet.PRIVATE) == 0) {
            msg.setRecipients(Message.RecipientType.BCC, addressTo);
        } else {
            System.out.println("Napačen tip vidnosti naslovov!");
        }
        msg.setSubject(subject);
        msg.setContent(message, "text/plain; charset=" + Portlets.getPortalConfig("charset"));
        Transport.send(msg);
    }
