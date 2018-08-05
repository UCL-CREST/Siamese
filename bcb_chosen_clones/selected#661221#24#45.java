    public static void sendMail(String recipient, String subject, String message) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", hostName);
            Session session = Session.getDefaultInstance(props, null);
            session.setDebug(true);
            Message msg = new MimeMessage(session);
            InternetAddress addressFrom;
            addressFrom = new InternetAddress(fromEmail);
            msg.setFrom(addressFrom);
            InternetAddress[] addressTo = new InternetAddress[1];
            addressTo[0] = new InternetAddress(recipient);
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            msg.setSubject(subject);
            msg.setContent(message, "text/plain");
            Transport.send(msg);
        } catch (AddressException e) {
            log.error("Error encountered sending email.", e);
        } catch (MessagingException e) {
            log.error("Error encountered sending email.", e);
        }
    }
