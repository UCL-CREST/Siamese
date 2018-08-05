    protected void sendAccountMail(String to, String passwd, boolean isCreate) {
        String appid = SystemProperty.applicationId.get();
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        try {
            Message msg = new MimeMessage(session);
            StringBuffer buffer = new StringBuffer();
            buffer.append("Hi, ").append(to).append("\r\n\r\n");
            if (isCreate) {
                buffer.append("You account on ").append(appid + ".appspot.com").append(" has been created.").append("\r\n");
                buffer.append("    Username:").append(to).append("\r\n");
                buffer.append("    Password:").append(passwd).append("\r\n");
                msg.setSubject("Your account has been activated");
            } else {
                msg.setSubject("Your account has been deleted.");
                buffer.append("You account on ").append(appid + ".appspot.com").append(" has been deleted.").append("\r\n");
            }
            buffer.append("Thanks again for registering, admin@" + appid + ".appspot.com");
            String msgBody = buffer.toString();
            msg.setFrom(new InternetAddress("admin@" + appid + ".appspotmail.com"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to, "Mr. User"));
            msg.setText(msgBody);
            Transport.send(msg);
        } catch (Exception e) {
            logger.error("Failed to send mail to user:" + to, e);
        }
    }
