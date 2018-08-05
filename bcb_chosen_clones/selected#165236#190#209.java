    private static void sendMail(String toAddress, String subject, String content) {
        try {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            Message msg = new MimeMessage(session);
            StringBuffer buffer = new StringBuffer();
            buffer.append("Hi, ").append("\r\n\r\n");
            buffer.append(content);
            buffer.append("Thanks again. admin@" + SystemProperty.applicationId.get() + ".appspot.com");
            String msgBody = buffer.toString();
            msg.setSubject(subject);
            msg.setFrom(new InternetAddress("admin@" + SystemProperty.applicationId.get() + ".appspotmail.com"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress, "Mr/Ms. User"));
            msg.addRecipient(Message.RecipientType.CC, new InternetAddress("yinqiwen@gmail.com", "Mr. Admin"));
            msg.setText(msgBody);
            Transport.send(msg);
        } catch (Exception e) {
            logger.error("Failed to send mail to user:" + toAddress, e);
        }
    }
