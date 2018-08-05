    public static boolean inviaEmail(String to, String from, String host, String msgText, String subject, String cc) {
        boolean debug = false;
        Properties props = System.getProperties();
        props.put("mail.smtp.host", host);
        if (debug) {
            props.put("mail.debug", "true");
        }
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(debug);
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = { new InternetAddress(to) };
            msg.setRecipients(Message.RecipientType.TO, address);
            if (cc != null && !cc.equals("")) {
                InternetAddress[] ccAddress = { new InternetAddress(cc) };
                msg.setRecipients(Message.RecipientType.CC, ccAddress);
            }
            msg.setSubject(subject);
            msg.setSentDate(new java.util.Date());
            msg.setText(msgText);
            Transport.send(msg);
            return true;
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return false;
        }
    }
