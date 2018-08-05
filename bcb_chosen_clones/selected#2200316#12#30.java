    public static void sendMail(String mail, String subject, String body) {
        try {
            InternetAddress ToAddress = new InternetAddress(mail, "", "ISO-2022-JP");
            InternetAddress FromAddress = new InternetAddress("takemura2@gmail.com", "パチンコ管理登録", "ISO-2022-JP");
            InternetAddress bccAddress = new InternetAddress("takemura2@gmail.com", "パチンコ管理登録", "ISO-2022-JP");
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(FromAddress);
            message.addRecipient(Message.RecipientType.TO, ToAddress);
            message.addRecipient(Message.RecipientType.BCC, bccAddress);
            message.setSubject(subject, "ISO-2022-JP");
            message.setText(body);
            Transport.send(message);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
