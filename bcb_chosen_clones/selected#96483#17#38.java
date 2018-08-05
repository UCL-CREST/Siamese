    public static void postMail(String subject, String message, String from, String... recipients) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST_NAME);
            Session session = Session.getDefaultInstance(props, null);
            session.setDebug(debug);
            Message msg = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress(from);
            msg.setFrom(addressFrom);
            InternetAddress[] addressTo = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                addressTo[i] = new InternetAddress(recipients[i]);
            }
            msg.setRecipients(Message.RecipientType.TO, addressTo);
            msg.addHeader("MyHeaderName", "myHeaderValue");
            msg.setSubject(subject);
            msg.setContent(message, "text/plain");
            Transport.send(msg);
        } catch (MessagingException e) {
            System.err.println("Can't send mail.");
        }
    }
