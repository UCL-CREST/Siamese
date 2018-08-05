    public void postMail(String recipients[], String subject, String message, String from) {
        try {
            Session session = getSession();
            if (from == null) from = DEFAULT_FROM;
            Message msg = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress(from);
            msg.setFrom(addressFrom);
            InternetAddress[] addressTo = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                addressTo[i] = new InternetAddress(recipients[i]);
            }
            msg.setRecipients(Message.RecipientType.TO, addressTo);
            msg.setSubject(subject);
            msg.setContent(message, "text/plain");
            msg.setSentDate(new Date());
            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
