    public static void sendToFrontlineSupport(String fromName, String fromEmailAddress, String attachment) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", FrontlineSMSConstants.FRONTLINE_SUPPORT_EMAIL_SERVER);
        Session session = Session.getInstance(props, null);
        MimeMessage msg = new MimeMessage(session);
        try {
            InternetAddress emailAddress = InternetAddress.getLocalAddress(session);
            if (emailAddress == null) emailAddress = new InternetAddress();
            if (fromName != null && fromName.length() > 0) emailAddress.setPersonal(fromName);
            if (fromEmailAddress != null && fromEmailAddress.length() > 0) emailAddress.setAddress(fromEmailAddress);
            msg.setFrom(emailAddress);
        } catch (UnsupportedEncodingException ex) {
            msg.setFrom();
        }
        msg.setRecipients(Message.RecipientType.TO, FrontlineSMSConstants.FRONTLINE_SUPPORT_EMAIL);
        msg.setSubject("FrontlineSMS log files");
        msg.setSentDate(new Date());
        Multipart multipart = new MimeMultipart();
        StringBuilder sb = new StringBuilder();
        appendSystemProperties(sb);
        appendCommProperties(sb);
        appendPluginProperties(sb);
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(sb.toString());
        multipart.addBodyPart(messageBodyPart);
        if (attachment != null) {
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(attachment);
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            attachmentBodyPart.setFileName(FrontlineSMSConstants.ZIPPED_LOGS_FILENAME);
            multipart.addBodyPart(attachmentBodyPart);
        }
        msg.setContent(multipart);
        Transport.send(msg);
    }
