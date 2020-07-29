    public void sendEmail() {
        System.out.println("...Emailling document");
        System.out.println("......getting email environment settings");
        Properties props = System.getProperties();
        props.put("mail.smtp.host", smtpServer);
        Session session = Session.getInstance(props, null);
        try {
            System.out.println("......creating message");
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = { new InternetAddress(to) };
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(body);
            MimeBodyPart mbp2 = new MimeBodyPart();
            FileDataSource fds = new FileDataSource(filename);
            mbp2.setDataHandler(new DataHandler(fds));
            mbp2.setFileName(fds.getName());
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);
            mp.addBodyPart(mbp2);
            msg.setContent(mp);
            msg.setSentDate(new Date());
            System.out.println("......sending message");
            Transport.send(msg);
            System.out.println("......connection closed");
        } catch (MessagingException mex) {
            System.err.println("MESSAGE EXCEPTION CREATED");
        }
    }
