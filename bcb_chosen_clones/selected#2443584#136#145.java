    public void doMail(String mailTo, String mailFrom) throws Exception {
        Message msg = new MimeMessage(myMailSession);
        InternetAddress addressFrom = new InternetAddress(mailFrom);
        msg.setFrom(addressFrom);
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
        msg.setSubject("Jetty Mail Test Succeeded");
        msg.setContent("The test of Jetty Mail @ " + new Date() + " has been successful.", "text/plain");
        msg.addHeader("Date", dateFormat.format(new Date()));
        Transport.send(msg);
    }
