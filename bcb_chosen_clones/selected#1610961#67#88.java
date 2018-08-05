    public void sendReport(File attachment, int numberOfBitstreams) throws IOException, javax.mail.MessagingException {
        String server = ConfigurationManager.getProperty("mail.server");
        Properties props = System.getProperties();
        props.put("mail.smtp.host", server);
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage msg = new MimeMessage(session);
        Multipart multipart = new MimeMultipart();
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText("This is the checksum checker report see attachement for details \n" + numberOfBitstreams + " Bitstreams found with POSSIBLE issues");
        multipart.addBodyPart(messageBodyPart);
        messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(attachment);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName("checksum_checker_report.txt");
        multipart.addBodyPart(messageBodyPart);
        msg.setContent(multipart);
        msg.setFrom(new InternetAddress(ConfigurationManager.getProperty("mail.from.address")));
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(ConfigurationManager.getProperty("mail.admin")));
        msg.setSentDate(new Date());
        msg.setSubject("Checksum checker Report - " + numberOfBitstreams + " Bitstreams found with POSSIBLE issues");
        Transport.send(msg);
    }
