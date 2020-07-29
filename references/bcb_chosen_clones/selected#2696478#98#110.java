    private void sendMail(String subject, String message, List<String> to, List<String> cc) throws AddressException, MessagingException {
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(TEST_MAIL_ACCOUNT));
        for (String toAddress : to) {
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
        }
        for (String ccAddress : cc) {
            msg.addRecipient(Message.RecipientType.CC, new InternetAddress(ccAddress));
        }
        msg.setSubject(subject);
        msg.setText(message);
        Transport.send(msg);
    }
