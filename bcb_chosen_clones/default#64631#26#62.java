    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("usage: java msgmultisend <to> <from> <smtp> true|false");
            return;
        }
        String to = args[0];
        String from = args[1];
        String host = args[2];
        boolean debug = Boolean.valueOf(args[3]).booleanValue();
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        Session session = Session.getInstance(props, null);
        session.setDebug(debug);
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = { new InternetAddress(to) };
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject("JavaMail APIs Multipart Test");
            msg.setSentDate(new Date());
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(msgText1);
            MimeBodyPart mbp2 = new MimeBodyPart();
            mbp2.setText(msgText2, "us-ascii");
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);
            mp.addBodyPart(mbp2);
            msg.setContent(mp);
            Transport.send(msg);
        } catch (MessagingException mex) {
            mex.printStackTrace();
            Exception ex = null;
            if ((ex = mex.getNextException()) != null) {
                ex.printStackTrace();
            }
        }
    }
