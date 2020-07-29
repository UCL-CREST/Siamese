    public static void main(String arg[]) throws Exception {
        int idCounter = 0;
        boolean debug = false;
        Properties props = new Properties();
        props.put("mail.smtp.host", "207.15.48.16");
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(debug);
        Message msg = new MimeMessage(session);
        InternetAddress from = new InternetAddress("anant.gowerdhan@momed.com");
        InternetAddress to = new InternetAddress("anant.gowerdhan@momed.com");
        msg.setFrom(from);
        msg.setRecipient(Message.RecipientType.TO, to);
        msg.setSubject("Testing Domino");
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText("Hi, how are you");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        msg.setContent(multipart);
        Transport.send(msg);
    }
