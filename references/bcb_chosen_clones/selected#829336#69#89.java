    public static void sendOnlyMal(String from, String to, String subject, String message) throws MessagingException {
        MimeMessage msg = new MimeMessage(session);
        InternetAddress ifrom = null;
        InternetAddress ito = null;
        try {
            ifrom = new InternetAddress(from);
        } catch (Exception e) {
            throw new MessagingException("From address " + from + " is wrong (" + e + ")");
        }
        try {
            ito = new InternetAddress(to);
        } catch (Exception e) {
            throw new MessagingException("To address " + to + " is wrong (" + e + ")");
        }
        msg.setFrom(ifrom);
        msg.setRecipient(MimeMessage.RecipientType.TO, ito);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setContent(message, "text/plain");
        Transport.send(msg);
    }
