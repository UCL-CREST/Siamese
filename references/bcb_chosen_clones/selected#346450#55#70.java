    public static void email(final String subject, final String textContents, final String emailAddress) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "25");
        Session session = Session.getInstance(props, new EmailAuth("posdiagnostic@gmail.com", "patientos"));
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("posdiagnostic@gmail.com"));
        InternetAddress[] address = { new InternetAddress(emailAddress) };
        msg.setRecipients(Message.RecipientType.TO, address);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setText(textContents);
        Transport.send(msg);
    }
