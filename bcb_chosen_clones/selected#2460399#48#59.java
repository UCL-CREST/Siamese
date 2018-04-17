    public void send(Object content) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("notification-admin@unimelb.edu.au"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
            message.setSubject("SensorWeb Client Notification");
            message.setText(content.toString());
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
