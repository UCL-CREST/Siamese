    public void send() throws FileNotFoundException, SendFailedException, MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", mailsmtp);
        Session session = Session.getDefaultInstance(props, null);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(mailfrom));
        InternetAddress all = new InternetAddress();
        InternetAddress[] addresses = all.parse(mailto);
        msg.setRecipients(Message.RecipientType.TO, addresses);
        msg.setSubject(mailsubject);
        msg.setSentDate(new Date());
        msg.setText(mailbody);
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(mailbody);
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        if (attachments != null) {
            for (int i = 0; i < attachments.length; i++) {
                messageBodyPart = new MimeBodyPart();
                File file = new File(attachments[i]);
                if (file.canRead()) {
                    DataSource source = new FileDataSource(file);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(attachments[i]);
                    multipart.addBodyPart(messageBodyPart);
                }
            }
        }
        msg.setContent(multipart);
        Transport.send(msg);
    }
