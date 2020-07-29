    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println("usage: java sendfile <to> <from> <smtp> <file> true|false");
            System.exit(1);
        }
        String to = args[0];
        String from = args[1];
        String host = args[2];
        String filename = args[3];
        boolean debug = Boolean.valueOf(args[4]).booleanValue();
        String msgText1 = "Sending a file.\n";
        String subject = "Sending a file";
        Properties props = System.getProperties();
        props.put("mail.smtp.host", host);
        Session session = Session.getInstance(props, null);
        session.setDebug(debug);
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = { new InternetAddress(to) };
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(msgText1);
            MimeBodyPart mbp2 = new MimeBodyPart();
            mbp2.attachFile(filename);
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);
            mp.addBodyPart(mbp2);
            msg.setContent(mp);
            msg.setSentDate(new Date());
            Transport.send(msg);
        } catch (MessagingException mex) {
            mex.printStackTrace();
            Exception ex = null;
            if ((ex = mex.getNextException()) != null) {
                ex.printStackTrace();
            }
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }
