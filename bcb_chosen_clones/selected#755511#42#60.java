    public static void sendSMS(String subject, String message) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        String msgBody = message;
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("yarikx@yarikx.org.ua"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(MyMail));
            msg.setSubject(subject);
            msg.setText(msgBody);
            Address[] ads = { new InternetAddress("yarikx@yarikx.org.ua") };
            msg.setReplyTo(ads);
            Transport.send(msg);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
